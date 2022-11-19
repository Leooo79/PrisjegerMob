
package no.usn.rygleo.prisjegermobv1.ui

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.core.util.toRange
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.usn.rygleo.prisjegermobv1.API
import no.usn.rygleo.prisjegermobv1.data.PriserPrButikk
import no.usn.rygleo.prisjegermobv1.data.VarerUiState
import no.usn.rygleo.prisjegermobv1.roomDB.*
import java.util.*
import kotlin.random.Random.Default.nextInt


/**
 * Klassen er en viewModel og inneholder logikk for App Prisjeger.
 * Kommuniserer med screens (composables), sortert i filer tilsvarende skjermer i App
 * Kommuniserer med datakilder: backend API (interface RestApi) og lokal DB (pakke roomDB)
 * Data hentes regelmessig fra server og emittes i App som livedata fra lokal DB.
 * Data: brukere, varenavn, priser, butikker, handlelister.
 */
class PrisjegerViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Variabler for backend API - innlogging og data server
     */
    // VARIABLER FOR STATUSENDRINGER VED OPPKOBLING MOT API
    private val _status = MutableLiveData<String>()
    val status: LiveData<String> = _status

    // VARIABLER FOR Å LESE INN VARELISTE FRA API
    private val _hentVarerAPI = MutableLiveData<Array<String>>()
    val hentVarerAPI: LiveData<Array<String>> = _hentVarerAPI


    // VARIABLER FOR Å LESE INN BUTIKKLISTE FRA API
    private val _butikkerAPI = MutableLiveData<Array<String>>()
    val butikkerAPI: LiveData<Array<String>> = _butikkerAPI


    // VARIABLER FOR Å LESE INN PRISER FRA API
    private val _priserPrButikk = MutableLiveData<PriserPrButikk>()
    val priserPrButikk: LiveData<PriserPrButikk> = _priserPrButikk


    // VARIABLER FOR Å LESE INN HANDLELISTE FRA API
    private val _handlelisteAPI = MutableLiveData<Map<String, Int>>()
    val handlelisteAPI: LiveData<Map<String, Int>> = _handlelisteAPI


    // VARIABLER FOR Å LESE INN HANDLELISTE FRA API
    private val _alleHandlelisterAPI = MutableLiveData<Map<String, Int>>()
    val alleHandlelisterAPI: LiveData<Map<String, Int>> = _alleHandlelisterAPI


    // VARIABLER FOR LOGIN API
    private val _brukerAPI = MutableLiveData<Map<String, String>>()
    val brukerAPI: LiveData<Map<String, String>> = _brukerAPI

    //variabel som holder på brukernavn 
    private val _brukernavn = mutableStateOf("")
    var brukernavn = _brukernavn

    //variabel for å sjekke om bruker er logget inn 
    private val  _isLoggedIn = mutableStateOf(false)
    val isLoggedIn = _isLoggedIn

    //variabel for regstrerAPI
    private val _registrerAPI = MutableLiveData<String>()
    var registrerAPI: LiveData<String> = _registrerAPI

    //sessionId som benyttes for at serveren skal kunne loggføre sesjonen som redigerer
    //handlelister. Dette er nødvendig for logikken til livedata fra server.
    val _sessionId = mutableStateOf("")
    var sessionId = _sessionId



    /**
     * Variabler for lokal database (Room)
     */
    // tar imot Flow fra Room lokal DB
    lateinit var alleVarer: LiveData<List<Varer>>
        private set

    // Variabel for uthenting og lagring av listenavn fra lokal DB
    lateinit var alleListenavn: LiveData<Array<String>>
        private set

    // Referanse (companion-objekt) til DAO for handlelister (Varer) i lokal database
    val varerDAO: VarerDAO
    val brukerDAO: BrukerDAO // for bruker





    /**
     * Statevariabeler i egen klasse for å ivareta endringer i state utover LiveData
     */
    // Default listenavn og butikk TODO: alle brukere må ha "MinHandleliste" som default
    var currentListenavn = "MinHandleliste" // VARIABEL FOR INNEVÆRENDE HANDLELISTENAVN
    var currentButikk = "Velg butikk" // VARIABEL FOR INNEVÆRENDE BUTIKK
    private val _uiStateNy = MutableStateFlow(
        VarerUiState(
            listenavn = currentListenavn,
            sortert = false,
            butikknavn = currentButikk
        )
    )
    val uiStateNy: StateFlow<VarerUiState> = _uiStateNy.asStateFlow()




    /**
     * Statevariabler for at TopAppBar skal kunne vise betinget innhold.
     * Disse benyttes for å holde oversikt over hvilken visning som er i bruk
     * slik at de rette elementene kan vises.
     */
    var activeNavItem = mutableStateOf("")
    fun setAktiv(newActiveNavItem: String) {
        activeNavItem.value = newActiveNavItem
    }
    // Booleans som benyttes for å vise innstillinger for handleliste
    val valgDialog = mutableStateOf(false) // vise valgmuligheter?
    val vilSletteDialog = mutableStateOf(false) // vise bekreftelse sletting
    val butikkDialog = mutableStateOf(false) // vise detaljer om sum pr butikk
    val filtrerEtterAntall = mutableStateOf(false) // vis kun varer med antall < 0
    val handleModus = mutableStateOf(false) // Handleliste i handlemodus?




    /**
     * Kode som kjøres ved oppstart (instansiering av vM)
     * Etablerer lokal database ved første gangs kjøring
     * Oppdaterer alle data fra server
     *
     */
    init {
        varerDAO = AppDatabase
            .getRoomDb(application).varerDAO() // etablerer lokal DB om ikke finnes
        brukerDAO = AppDatabase
            .getRoomDb(application).brukerDAO() // etablerer lokal DB om ikke finnes
        oppdaterAlleDataFraApi() // henter alle data fra server
        kontrollerForBruker() // kontrollerer om bruker er lagret i lokal DB
    }




    /** FUNKSJONER FOR Å HENTE DATA FRA SERVER OG OVERFØRE TIL LOKAL DB **************************/



    /**
     * Hjelepefunksjon for å oppdatere alle data fra server til lokal DB/ visning
     * OBS: kostbar og tidkrevende
     *
     * Kall på data fra server som forutsetter innlogging kjøres kun på isLoggedIn
     *
     * Kjører når:
     * init, ved navigasjon til handleliste, ved "oppdater handlelister" i HandlelisteScreen,
     * ved opprettelse av ny handleliste,
     */
    fun oppdaterAlleDataFraApi() {
        _status.value = "oppdaterAlleDataFraApi prøver å oppdatere alle data fra server"
        println(status.value)
        try {
            getAPIVarer() // henter varenavn fra server
            getAPIButikker() // henter butikknavn fra server
            getAPIPriserPrButikk() // henter priser fra server
            oppdaterVarerFraApi() // overfører varenavn til lokal DB
            if (isLoggedIn.value) { // handlelister krever innlogging :
                oppdaterListeFraApi() // oppdaterer alle handlelister fra server til lokal DB
                getLokaleVarer(currentListenavn) // emitter alle handlelister fra lokal DB, inkludert antall = 0
                getAlleListenavn() // henter unike handlelistenavn fra lokal DB
            }
            _status.value = "Vellykket, oppdaterAlleDataFraApi gjennomført"
            println(status.value)
        } catch (e:Exception) {
            _status.value = "Feil oppdaterAlleDataFraApi: ${e.message}"
            println(status.value)
        }
    }

    /**
     * Funksjon som sjekker om data må oppdateres, og iverksetter oppdatering dersom
     * backend meller fra om endret data.
     */
    var oppdateringAktiv = mutableStateOf(false) // sett denne til false for å skru av oppdateringer

    @RequiresApi(Build.VERSION_CODES.N)
    fun seEtterOppdateringer() = viewModelScope.launch {
        if (!oppdateringAktiv.value) { // forhindrer dobbel kjøring
            oppdateringAktiv.value = true
            while (oppdateringAktiv.value) {
                if (måEndre()) oppdaterAlleDataFraApi()
                delay(30000) // hvor mange millisekunder det skal være mellom oppdateringer
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    var sisteTidspunkt = mutableStateOf(nåTid()) // initialisering av tidspunkt
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun måEndre(): Boolean {
        _status.value = "Sjekker om API data trenger oppfriskning..."
        println(status.value)
        try {
            println("Siste tidspunkt: " + sisteTidspunkt.value)
            var svar = API.retrofitService.sjekkOppdatert(
                sisteTidspunkt.value,
                brukernavn.value,
                currentListenavn,
                sessionId.value
            )
            sisteTidspunkt.value = nåTid()
            println(
                "Nytt tidspunkt: " + sisteTidspunkt.value +
                ", bruker: " + brukernavn.value +
                ", session:" + sessionId.value
            )
            return svar // TODO: Det skal være en egen metode her for å lese av svaret
        } catch (e: Exception) {
            _status.value = "Klarte ikke sjekke om data trenger oppfriskning: ${e.message}"
            println(status.value)
            return false
        }
    }

    /**
     * Hjelpefunksjon som lager en sessionId i form av en tilfeldig String
     */
    fun lagSession(lengde: Int): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        var nySession = ""
        for (i in 0..lengde) {
            nySession += chars[nextInt(chars.length)] //plukker ut et tilfeldig tegn
        }
        return nySession
    }

    /**
     * Hjelpefunksjon som returnerer tidsstempel i ISO format ("yyyy-mm-hh-dd hh:mm:ss")
     */
    @RequiresApi(Build.VERSION_CODES.N)
    fun nåTid(): String {
        val tz = TimeZone.getTimeZone("GMT+02:00")
        val time = Calendar.getInstance(tz).time
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return formatter.format(time)
    }


    /**
     * funksjon for å registrere ny bruker
     */
    fun postAPIRegistrer(epost: String, passord: String) {
        val map = mapOf("epost" to epost, "passord" to passord)
        _status.value = "postAPIRegistrer forsøker å opprette ny bruker på server"
        println(status.value)
        viewModelScope.launch {
            try {
                _registrerAPI.value = API.retrofitService.registrerBruker(map)
                registrerAPI = _registrerAPI
                _status.value = "Vellykket, postAPIRegitrer opprettet ny bruker"
                println(status.value)
            } catch (e: Exception) {
                _status.value = "Feil postAPIRegistrer: ${e.message}"
                println(status.value)
            }
        }
    }




    /**
     * Funksjon for å sette globalt brukernavn
     */
    fun settBrukernavn (brukerNavn : String){
        _brukernavn.value = brukerNavn
    }




    /**
     * Funksjonen henter inn Array av varenavn (String) fra backend API via interface RestApi
     * Kjøres ved oppstart
     *
     */
    private fun getAPIVarer() = viewModelScope.launch {
        _status.value = "getAPIVarer prøver å hente varenavn fra API"
        println(status.value)
        try {
            _hentVarerAPI.value = API.retrofitService.getVareliste()
            _status.value = "Vellykket, getAPIVarer hentet varenavn"
            println(status.value)
        } catch (e: Exception) {
            _status.value = "Feil getAPIVarer: ${e.message}"
            println(status.value)
        }
    }





    /**
     * Funksjonen henter inn Array av butikknavn (String) fra backend API via interface RestApi
     * Kjøres ved oppstart og legger alle butikknavn inn i Array
     *
     */
    private fun getAPIButikker() = viewModelScope.launch {
        _status.value = "getAPIButikker prøver å hente butikknavn fra API"
        println(status.value)
        try {
            _butikkerAPI.value = API.retrofitService.getButikkliste()
            _status.value = "Vellykket, getAPIButikker hentet butikknavn"
            println(status.value)
        } catch (e: Exception) {
            _status.value = "Feil getAPIButikker: ${e.message}"
            println(status.value)
        }
    }





    /**
     * Funksjonen henter inn Array av priser pr vare pr butikk (++)
     * Kjøres ved oppstart
     */
    fun getAPIPriserPrButikk() = viewModelScope.launch {
        _status.value = "getAPIPriserPrButikk prøver å hente butikknavn fra API"
        println(status.value)
        try {
            _priserPrButikk.value = API.retrofitService.getPrisPrButikk()
            _status.value = "Vellykket, getAPIPriserPrButikk hentet"
            println(status.value)
        } catch (e: Exception) {
            _status.value = "Feil getAPIPriserPrButikk: ${e.message}"
            println(status.value)
        }
    }





    /**
     * Funksjonen sender brukernavn og passord til server som returnerer status
     */
    fun postAPILogin(epost: String, passord: String) {
        val map = mapOf("epost" to epost, "passord" to passord)
        viewModelScope.launch {
            _status.value = "postAPILogin prøver å logge inn bruker"
            println(status.value)
            try {
                _brukerAPI.value = API.retrofitService.login(map)
                if(brukerAPI.value?.get("melding").equals("innlogget")){
                    _isLoggedIn.value = true
                    // TODO: endret verdi for oppdatering av brukernavn:
                //    _brukernavn.value = brukerAPI.value?.get("bruker").toString()
                    _brukernavn.value = epost
                    // TODO: lagrer bruker i lokal DB
                    brukerDAO.insert(Bruker(epost, lagSession(30))) // insert av ny bruker til lokal DB
                    _status.value = "Vellykket, bruker innlogget og lagret" // vellykket
                    println(status.value)
                }
            } catch (e: Exception) {
                _status.value = "Feil postAPILogin: ${e.message}"
                println(status.value)
            }
        }
    }
    //funksjon for å logge ut
    fun postAPILoggout(){
        _isLoggedIn.value=false
    }






    /**
     * Funksjonen overfører alle varenavn hentet fra server til lokal database
     * Inserter class Varer med antall =0 til Entity Varer
     */
    fun oppdaterVarerFraApi() {
        println("oppdaterVarerFraApi kjører")
        _status.value = "oppdaterVarerFraApi prøver å oppdatere lokal DB (varenavn) fra API"
        println(status.value)
        try {
            for (varenavn in hentVarerAPI.value!!) {
                varerDAO.insertAll( // insert til lokal DB, duplikater ignoreres
                    Varer( // class Varer
                    currentListenavn, // tittel/ listenavn
                    varenavn, // varenavn
                    0 // antall
                    )
                )
            }
            _status.value = "Vellykket, varenavn i lokal DB oppdatert"
            println(status.value)
        } catch (e: Exception) {
            _status.value = "Feil oppdaterVarerFraApi: ${e.message}"
            println(status.value)
        }
    }






    /**
     * Funksjonen overfører handlelister fra API og inserter Varer til lokal DB
     * OBS! Erstatter eksisterende duplikater, server eier sannheten om handlelister
     * TODO: Denne kan potensielt lytte på endringer på server, men må testes mer
     * TODO: DENNE METODEN HAR VÆRT SKYLD I MYE BUGS, BLE KJØRT FOR OFTE/ KONTINUERLIG
     */
    fun oppdaterListeFraApi() = viewModelScope.launch {
        _status.value = "oppdaterListeFraApi kjører"
        println(status.value)
        if (!isLoggedIn.value) { // handlelister forutsetter innlogging
            _status.value = "oppdaterListeFraApi: Ingen bruker innlogget"
            println(status.value)
        } else {
            _status.value = "oppdaterListeFraApi prøver å oppdatere lokal DB (handlelister) fra API"
            try { // henter alle unike handlelistenavn fra server
                val alleHandlelister = API.retrofitService.getHandlelister(brukernavn.value)
                for (lister in alleHandlelister) { // henter alle handlelisterader fra server
                    val komplettListe = API.retrofitService.getHandleliste(brukernavn.value, lister)
                    for (varer in komplettListe) {   // server eier sannheten :
                        varerDAO.insertAllForce( // insert lokal DB, duplikater erstattes
                            Varer( // class Varer
                                lister, // listenavn/ tittel
                                varer.key, // varenavn
                                varer.value // antall
                            )
                        )
                    }
                }
                _status.value = "Vellykket, handlelister i lokal DB oppdatert"
                println(status.value)
            } catch (e: Exception) {
                _status.value = "Feil oppdaterListeFraApi: ${e.message}"
                println(status.value)
            }
        }
    }





    /**
     * Funksjonen oppdaterer en enkelt handleliste fra server
     * TODO: IKKE I BRUK PR 12.11.2022
     */
    fun oppdaterEnListeFraApi() = viewModelScope.launch {
        println("OppdaterEnListeFraApi kjører")
        _status.value = "Prøver å oppdatere lokal DB (handlelister) fra API"
        try { // henter alle unike handlelistenavn fra server
            val komplettListe = API.retrofitService.getHandleliste(brukernavn.value, currentListenavn)
            for (varer in komplettListe) { // for alle lister:
                varerDAO.insertAllForce(Varer( // insert REPLACE - server eier sannheten
                    currentListenavn, // listenavn/ tittel
                    varer.key, // varenavn
                    varer.value // antall
                ))
            }
            _status.value = "Vellykket, handlelister i lokal DB oppdatert"
        } catch (e: Exception) {
            _status.value = "Feil oppdaterListeFraApi: ${e.message}"
        }
    }







    /**
     * Funksjonen henter ut enhetspris for en enkelt vare pr butikk
     * Benyttes i HandlelisteScreen for visning av detaljer pr vare
     */
    fun finnPrisPrVare(butikknavn: String, varenavn: String) : String {
        var pris = ""
        val indeksForButikkNavn = indeksForButikk(butikknavn)
        _status.value = "finnPrisPrVare prøver å vise enhetspris"
        println(status.value)
        try { // lopper ut enhetspris fra serverdata
            for (priser in priserPrButikk.value?.varer!!) {
                if (priser.key == varenavn) {
                    priserPrButikk.value?.varer?.get(priser.key)
                        ?.get(indeksForButikkNavn)?.let {
                            pris = it // ny enhetspris
                        }
                }
            }
            _status.value = "Vellykket, enhetspris vises"
            println(status.value)
        } catch (e: Exception) {
            _status.value = "Feil finnPrisPrVare: ${e.message}"
            println(status.value)
            return "0.0"
        }
        return pris
    }







    /**
     * Funksjonen henter ut sum pr handleliste pr butikk
     * Benyttes i HandlelisteScreen
     */
    fun finnSumPrButikk(butikknavn: String) : String {
        var pris = 0.0
        val indeksForButikkNavn = indeksForButikk(butikknavn)
        _status.value = "Prøver å vise sum pr handleliste pr butikk (detaljer)"
        println(status.value)
        try { // looper alle varer i lokal DB og alle priser fra server
            for (varer in alleVarer.value!!) {
                for (priser in priserPrButikk.value?.varer!!) { // hvis rett vare og liste,
                    if (priser.key == varer.varenavn && varer.listenavn == currentListenavn) {
                        priserPrButikk.value?.varer?.get(priser.key)
                            ?.get(indeksForButikkNavn)?.toDouble().let { // hent rett butikk (indeks)
                                pris += it?.times(varer.antall) ?: 0.0 // aggregerer antall*pris
                            }
                    }
                }
            }
            _status.value = "Vellykket, viser sum pr handleliste pr butikk"
            println(status.value)
        } catch (e: Exception) {
            _status.value = "Feil finnSumPrButikk: ${e.message}"
            println(status.value)
            return "0.0"
        }
        return (Math.round(pris * 100.00) / 100.0).toString()+",-" // avrunding 2 des.
    }









    /** METODER FOR Å OPPDATERE STATE/ DEFAULTVERDIER  *******************************************/


    /**
     * Funksjonen kalles fra composables for å oppdatere hvilken handleliste
     * som vises. Nytt kall på lokal DB + endrer statevariabel listenavn
     * for rekomposisjon
     */
    fun setListeNavn(nyttListeNavn: String) {
        currentListenavn = nyttListeNavn
        oppdaterListenavn() // for rekomposisjon
    }





    /**
     * Tar inn nytt current butikknavn og kaller på oppdatering av state
     */
    fun setButikknavn(nyttButikknavn: String) {
        println("setButikknavn kjører")
        currentButikk = nyttButikknavn
        oppdaterButikknavn() // for rekomposisjon
    }





    /**
     * Hjelpemetode for å oppdatere state på listenavn -> rekomposisjon
     */
    private fun oppdaterListenavn() {
        _uiStateNy.update { currentState ->
            currentState.copy(
                listenavn = currentListenavn,
            )
        }
    }






    /**
     * Lager kopi av current butikknavn
     */
    private fun oppdaterButikknavn() {
        _uiStateNy.update { currentState ->
            currentState.copy(
                butikknavn = currentButikk,
            )
        }
    }






    /**
     * Hjelpemetode for å oppdatere state på sortert -> rekomposisjon
     */
    private fun setSortert() {
        _uiStateNy.update { currentState ->
            currentState.copy(
                sortert = true,
            )
        }
    }






    /**
     * Hjelpemetode for å oppdatere state på sortert -> rekomposisjon
     */
    private fun setUsortert() {
        _uiStateNy.update { currentState ->
            currentState.copy(
                sortert = false,
            )
        }
    }





    /** METODER FOR LOKAL DATABASE + OPPDATERING AV SERVER **************************************/




    /**
     * Hjelpefunksjon som sjekker om bruker er lagret i lokal DB
     * Slik at bruker slipper å logge inn på nytt
     * OBS! Vil kaste og fange unntak dersom bruker ikke er opprettet på telefon (lokal DB)
     */
    private fun kontrollerForBruker() {
        _status.value = "Prøver å kontrollere lokal DB for brukere"
        println(status.value)
        try {
            if (!brukerDAO.getBruker().brukerNavn.isEmpty()) {
                _isLoggedIn.value = true
                _brukernavn.value = brukerDAO.getBruker().brukerNavn
                _sessionId.value = brukerDAO.getBruker().sessionId
                _status.value = "Vellykket, bruker funnet i lokal DB"
                println(status.value)
            }
        } catch (e:Exception) {
            // OBS: unntak dersom bruker ikke er opprettet
            _status.value = "Mulig feil kontrollerForBruker: ${e.message}"
            println(status.value)
        }
    }



/*
    /**
     * Funksjonen etablerer og bytter ut innhold i LiveData -> LazyColumn fra lokal DB
     * Bytter til varer med antall > 0
     * TODO: ikke i bruk, filter sorterer på antall
     * TODO: kanskje aktuell for å hente data til kartvisning?
     */
    fun getSortertLokaleVarer() {
        println("getSortertLokaleVarer kjører")
        viewModelScope.launch {
            _status.value = "Henter valgte varer fra lokal DB"
            println(status.value)
            try {
                alleVarer = varerDAO.getAlleValgteVarer().asLiveData() // ny spørring lokal DB
                _status.value = "Vellykket, sorterte varer hentet"
                println(status.value)
                setSortert() // rekomposisjon
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
                println(status.value)
            }
        }
    }


 */




    /**
     * Funksjonen etablerer og bytter ut innhold i LiveData -> LazyColumn fra lokal DB
     * Henter alle varer uavhengig av antall
     */
    fun getLokaleVarer(listenavn: String) = viewModelScope.launch {
        _status.value = "getLokaleVarer prøver å vise varer fra lokal DB"
        println(status.value)
        try {
            alleVarer = varerDAO.getAlleVarer(listenavn).asLiveData() // ny spørring lokal DB
            _status.value = "Vellykket, varer fra lokal Db hentet"
            println(status.value)
            setUsortert() // rekomposisjon
        } catch (e: Exception) {
            _status.value = "Feil: ${e.message}"
            println(status.value)
        }
    }






    /**
     * Funksjonen henter ut unike listenavn fra lokal DB
     *
     */
    fun getAlleListenavn() = viewModelScope.launch {
        _status.value = "Henter unike listenavn fra lokal DB"
        println(status.value)
        try {
            alleListenavn = varerDAO.getAlleListenavn().asLiveData()
            _status.value = "Vellykket, unike listenavn hentet ut"
            println(status.value)
        } catch (e: Exception) {
            _status.value = "Feil getAlleListenavn: ${e.message}"
            println(status.value)
        }
    }






    /**
     * Funksjonen øker antall med 1, både lokalt og sentralt
     */
    fun inkementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch {
        try { // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.inkrementerAntall(varenavn, listenavn) == 1) {
                // API oppretter handleliste/ legger til vare/ antall++
                API.retrofitService.inkrementerHandleliste(
                    brukernavn.value,
                    listenavn,
                    varenavn
                )
                _status.value = "Vellykket, inkrementerVareAntall gjennomført"
                println(status.value)
            } else {
                _status.value = "Feil inkrementerVareAntall : Lokal database svarer ikke"
                println(status.value)
            }
        } catch (e: Exception) {
            _status.value = "Feil inkementerVareAntall: ${e.message}"
            println(status.value)
        }
    }






    /**
     * Funksjonen reduserer antall med 1, både lokalt og sentralt
     */
    fun dekrementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch {
        try {   // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.dekrementerAntall(varenavn, listenavn) == 1) {
                // API sletter handleliste/ sletter vare/ antall--
                API.retrofitService.dekrementerHandleliste(
                    brukernavn.value,
                    listenavn,
                    varenavn
                )
                _status.value = "Vellykket, dekrementerVareAntall gjennomført"
                println(status.value)
            } else {
                _status.value = "Feil dekrementerVareAntall : Lokal database svarer ikke"
                println(status.value)
            }
        } catch (e: Exception) {
            _status.value = "Feil dekrementerVareAntall: ${e.message}"
            println(status.value)
        }
    }




/*

    /**
     * Fuksjon for å sette inn ny vare i lokal DB, tabell Vare (handlelister)
     * OBS: ignorerer ny ved duplikat
     */
    private fun insertVare(vare: Varer) = viewModelScope.launch {
        _status.value = "insertVare prøver insert til lokal DB"
        try {
            varerDAO.insertAll(vare)
        } catch (e: Exception) {
            _status.value = "Feil insertVare: ${e.message}"
            println("Feil insertVare: ${e.message}")
        }
    }





    /**
     * Fuksjon for å sette inn ny vare i lokal DB, tabell Vare (handlelister)
     * OBS: erstatter eksisterende ved duplikat
     */
    private fun insertVareForce(vare: Varer) = viewModelScope.launch {
        varerDAO.insertAllForce(vare)
    }



 */



    /**
     * Funksjon for å slette en vare fra lokal/ sentral DB
     * TODO: ny logikk setter antall i 0, denne er ikke i bruk
     */
    fun slettVare(varer: Varer) = viewModelScope.launch(Dispatchers.IO) {
        try { // hvis slettet lokalt: slett sentralt
            if (varerDAO.slettVare(varer) == 1) {
                API.retrofitService
                    .slettVareIListe(
                        brukernavn.value,
                        varer.listenavn,
                        varer.varenavn
                    )
                _status.value = "Vellykket, vare slettet"
                println(status)
            }
            else println("Feil slettVare: Lokal database svarer ikke")
        } catch (e:Exception) {
            _status.value = "Feil slettVare: ${e.message}"
            println(status)
        }
    }





    /**
     * Funksjon for å sette antall til 0 -> blir valgbar i lokal DB, men slettet fra sentral
     */
    fun settAntallTilNull(varer: Varer) = viewModelScope.launch {
        _status.value = "settAntallTilNull forsøker å oppdatere databaser"
        try { // hvis tilNull lokalt: slett sentralt
            if (varerDAO.antallTilNull(varer.varenavn, varer.listenavn) == 1) {
                API.retrofitService.slettVareIListe(
                    brukernavn.value,
                    varer.listenavn,
                    varer.varenavn
                )
                _status.value = "Vellykket, settAntallTilNull gjennomført"
                println(status.value)
            }
            else {
                _status.value = "Feil settAntallTilNull: Lokal database svarer ikke"
                println(status.value)
            }
        }
        catch (e:Exception) {
            _status.value = "Feil settAntallTilNull: ${e.message}"
            println(status.value)
        }
    }





    /**
     * Funksjon for å slette en handleliste fra lokal/ sentral DB
     */
    fun slettHandleliste() = viewModelScope.launch {
        _status.value = "slettHandleliste forsøker å slette liste"
        try {// Hvis slettet fra lokal DB, slett fra sentral DB
            if (varerDAO.slettHandleliste(currentListenavn) >= 1) {
                API.retrofitService.slettHandleliste(brukernavn.value, currentListenavn)
                _status.value = "Vellykket, handleliste slettet"
                println(status.value)
            }
            else {
                _status.value = "Feil slettHandleliste: Lokal database svarer ikke"
                println(status.value)
            }
        } catch (e: Exception) {
            _status.value = "Feil slettHandleliste: ${e.message}"
            println(status.value)
        }
    }








    /** HJELPEMETODER *****************************************************************************/




    /**
     * Hjelpemetode for å kompensere for manglende key fra server (butikknavn)
     */
    private fun indeksForButikk(butikk: String) : Int {
        if (butikk == "Kiwi") return 0
        if (butikk == "Meny") return 1
        if (butikk == "Coop Obs") return 2
        if (butikk == "Rema 1000") return 3
        if (butikk == "Spar") return 4
        return if (butikk == "Coop Extra") 5
        else -1
    }




    /**
     * Funksjon for å regne ut sum pr vare i handlelister
     * returnerer antall * enhetspris
     */
    fun sumPrVare(vare: Varer): Double {
        return (Math.round(vare.antall * finnPrisPrVare(currentButikk, vare.varenavn)
            .toDouble() * 100.00) / 100.0)
    }




    /**
     * Funksjon for å kontrollere om nytt listenavn er duplikat
     */
    fun kontrollerListenavn(listenavn: String) : Boolean {
        return alleListenavn.value!!.contains(listenavn)
    }


} // slutt class PrisjegerViewModel









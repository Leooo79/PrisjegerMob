
package no.usn.rygleo.prisjegermobv1.ui

import android.app.Application
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
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
 * Kommuniserer med screens (composables), sortert i filer tilsvarende navigerbare skjermer i App
 *
 * Kommuniserer med datakilder: backend API (interface RestApi) og lokal DB (pakke roomDB)
 * Data hentes på forespørsel fra server og emittes i App som livedata fra lokal DB.
 * Det er også etablert en egen funksjon som kontrollerer tjener for endringer
 * hvert 10 sekund. Dette for å håndtere visuelt avvik der en og samme bruker parallellt innlogget
 * på to eller flere enheter. Dette sørger for at det ikke kan oppstå avvik mellom lokal og sentral
 * database.
 */
class PrisjegerViewModel(application: Application) : AndroidViewModel(application) {
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

    // VARIABLER FOR LOGIN API
    private val _brukerAPI = MutableLiveData<Map<String, String>>()
    val brukerAPI: LiveData<Map<String, String>> = _brukerAPI

    //variabel som holder på brukernavn 
    private val _brukernavn = mutableStateOf("")
    var brukernavn = _brukernavn

    //variabel for å sjekke om bruker er logget inn 
    private val  _isLoggedIn = mutableStateOf(false)
    val isLoggedIn = _isLoggedIn

    //variabel for å sjekke status på registrering
    private val _registrert = mutableStateOf(false)
    val registert = _registrert

    //variabel for regstrerAPI
    private val _registrerAPI = MutableLiveData<String>()
    var registrerAPI: LiveData<String> = _registrerAPI

    //sessionId som benyttes for at serveren skal kunne loggføre sesjonen som redigerer
    //handlelister. Dette er nødvendig for logikken til livedata fra server.
    val _sessionId = mutableStateOf("")
    var sessionId = _sessionId
    var sisteOppdatertTidspunkt = mutableStateOf("")

    // Variabler for data fra lokal database
    // tar imot Flow fra Room lokal DB
    lateinit var alleVarer: LiveData<List<Varer>>
        private set

    // Variabel for uthenting av unike listenavn fra lokal DB
    lateinit var alleListenavn: LiveData<Array<String>>
        private set

    // Referanser til DAO for handlelister (Varer) og bruker (Bruker) i lokal database
    val varerDAO: VarerDAO
    val brukerDAO: BrukerDAO

    // variabler for å sette og hente lokasjonsinformasjon
    var lon : Double = 0.0
    var lat : Double = 0.0

    var currentListenavn = "MinHandleliste" // VARIABEL FOR INNEVÆRENDE HANDLELISTENAVN
    var currentButikk = "Kiwi" // VARIABEL FOR INNEVÆRENDE BUTIKK

    // referanse til egen klasse for statevariabler
    private val _uiStateNy = MutableStateFlow(
        VarerUiState(
            listenavn = currentListenavn, // endring av listenavn = rekomp
            sortert = false,
            butikknavn = currentButikk
        )
    )
    val uiStateNy: StateFlow<VarerUiState> = _uiStateNy.asStateFlow()

    // Statevariabler for at TopAppBar skal kunne vise betinget innhold.
    // Disse benyttes for å holde oversikt over hvilken visning som er i bruk
    // slik at de rette elementene kan vises.
    private val _activeNavItem = mutableStateOf("")
    var activeNavItem = _activeNavItem

    // Variabler som benyttes for å trigge dialogvinduer/ endring i HandlelisteScreen
    val valgDialog = mutableStateOf(false) // vise valgmuligheter?
    val vilSletteDialog = mutableStateOf(false) // vise bekreftelse sletting
    val butikkDialog = mutableStateOf(false) // vise detaljer om sum pr butikk
    val filtrerEtterAntall = mutableStateOf(false) // vis kun varer med antall < 0
    val handleModus = mutableStateOf(false) // Handleliste i handlemodus?

    // Variabler som benyttes for å trigge dialogvinduer/ endring i LoginScreen
    val loginDialog = mutableStateOf(false)
    val registrerDialog = mutableStateOf(false)

    // Kode i init kjøres ved instansisering
    init {
        varerDAO = AppDatabase
            .getRoomDb(application).varerDAO() // etablerer lokal DB om ikke finnes
        brukerDAO = AppDatabase
            .getRoomDb(application).brukerDAO() // etablerer lokal DB om ikke finnes
        oppdaterAlleDataFraApi() // henter alle data fra server
        kontrollerForBruker() // kontrollerer om bruker er lagret i lokal DB
    }

    /**
     * Funksjon for å sette lokasjon
     */
    fun setLokasjon(lonG : Double, latI: Double){
        lon = lonG
        lat = latI
        println("viewmodel printer koord")
        print(lon)
        print(lat)
    }

    /**
     * Metode kjøres ved navigasjon for å at programmet skal vite på hvilken skjerm bruker er.
     */
    fun setAktiv(newActiveNavItem: String) {
        _activeNavItem.value = newActiveNavItem
    }

    /**
     * Hjelepefunksjon for å oppdatere alle data fra server til lokal DB/ visning
     * Kjører kall på en rekke funskjoner som igjen kaller på tjener
     * og overfører data til memory/ lokal database
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
                oppdater()
                delay(10000) // hvor mange millisekunder det skal være mellom oppdateringer
            }
        }
    }

    /**
     * Funksjonen oppdaterer data fra tjener om dette trengs. Kaller på flere metoder
     * for oppdatering.
     */
    @RequiresApi(Build.VERSION_CODES.N)
    var førsteOppdatering = mutableStateOf(true)
    @RequiresApi(Build.VERSION_CODES.N)
    suspend fun oppdater() {
        if (!førsteOppdatering.value) {
            _status.value = "Sjekker om API data trenger oppfriskning..."
            println(status.value)
            try {
                var svar = API.retrofitService.sjekkOppdatert(
                    sisteOppdatertTidspunkt.value,
                    brukernavn.value,
                    sessionId.value,
                    currentListenavn
                )
                println(
                    "Nytt tidspunkt: " + sisteOppdatertTidspunkt.value +
                            ", bruker: " + brukernavn.value +
                            ", session:" + sessionId.value
                )
                getAlleListenavn()
                if (svar.prisUtdatert || svar.handlelisteUtdatert) {
                    sisteOppdatertTidspunkt.value = API.retrofitService.hentTidspunkt()
                    if (svar.prisUtdatert) {
                        // håndtere utgått prisdata
                        _status.value = "Prisdata er utgått. Henter nytt..."
                        println(_status.value)
                        getAPIVarer()
                        getAPIButikker()
                        getAPIPriserPrButikk()
                        oppdaterVarerFraApi()
                    }
                    if (svar.handlelisteUtdatert) {
                        // håndtere utgått handleliste
                        //varerDAO.slettHandleliste(currentListenavn)
                        _status.value = "Handleliste er nyere på tjener. Henter ny data..."
                        println(_status.value)
                        oppdaterListeFraApi()
                        getLokaleVarer(currentListenavn)
                    }
                } else {
                    _status.value = "Data er allerede oppdatert."
                    println(_status.value)
                }
            } catch (e: Exception) {
                _status.value = "Klarte ikke oppdatere data: ${e.message}"
                println(status.value)
            }
        } else { // hvis første kjøring: hent tidspunkt fra tjener, og hopp over oppdatering
            _status.value = "Starter oppdateringssystem..."
            println(_status.value)
            sisteOppdatertTidspunkt.value = API.retrofitService.hentTidspunkt()
            førsteOppdatering.value = false
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
     * funksjon for å registrere ny bruker. Etter bruker er opprettet må bruker logge inn.
     */
    fun postAPIRegistrer(epost: String, passord: String) {
        val map = mapOf("epost" to epost, "passord" to passord)
        _status.value = "postAPIRegistrer forsøker å opprette ny bruker på server"
        println(status.value)
        viewModelScope.launch {
            try {
                _registrerAPI.value = API.retrofitService.registrerBruker(map)
                registrerAPI = _registrerAPI
                 if(registrerAPI.value.equals("bruker registrert")) {
                    _registrert.value = true // er registrert
                     registrerDialog.value = true // viser
                     _status.value = "Vellykket, postAPIRegitrer opprettet ny bruker"
                } else {
                    _registrert.value = false
                     registrerDialog.value = true
                     _status.value = "Feil postAPIRegitrer, bruker eksisterer"
                }
                println(status.value)
            } catch (e: Exception) {
                _status.value = "Feil postAPIRegistrer: ${e.message}"
                println(status.value)
            }
        }
    }

    /**
     * Funksjonen henter inn Array av varenavn (String) fra tjener via interface RestApi
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
     * Funksjonen henter inn Array av butikknavn (String) fra tjener via interface RestApi
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
     * Funksjonen henter inn priser pr vare pr butikk fra tjener
     * JSON-objekt som sendes fra Tjener modelleres med class PriserPrButikk
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
     * Funksjonen sender brukernavn og passord til tjener som forsøker innlogging
     * Tjener sender bekreftelse/ feilmelding. Ved ok lagres bruker i lokal DB.
     */
    fun postAPILogin(epost: String, passord: String) {
        val map = mapOf("epost" to epost, "passord" to passord)
        viewModelScope.launch {
            _status.value = "postAPILogin prøver å logge inn bruker"
            println(status.value)
            try {
                _brukerAPI.value = API.retrofitService.login(map)
                if (brukerAPI.value?.get("melding").equals("innlogget")) {
                    currentListenavn = "MinHandleliste" // resett default listenavn
                    // insert av ny bruker til lokal DB:
                    brukerDAO.insert(Bruker(epost, lagSession(30)))
                    _brukernavn.value = brukerDAO.getBruker().brukerNavn
                    _sessionId.value = brukerDAO.getBruker().sessionId
                    _isLoggedIn.value = true // er innlogget
                    loginDialog.value = true // vis innlogging dialog
                    oppdaterListeFraApi() // oppdaterer brukers handlelister fra server
                    _status.value = "Vellykket, bruker innlogget og lagret"
                    println(status.value)
                } else {
                    _isLoggedIn.value = false
                    loginDialog.value = true
                    _status.value = "Feil ved innlogging, brukernavn eller passord"
                    println(status.value)
                }
            } catch (e: Exception) {
                _status.value = "Feil postAPILogin: ${e.message}"
                println(status.value)
            }
        }
    }

    /**
     * Funksjon for å logge ut bruker. Bruker og handlelister slettes fra lokal DB.
     */
    fun postAPILoggout(){
        // TODO: lagt til nullstilling av lokal DB
        brukerDAO.slettBruker() // sletter bruker i lokal DB
        varerDAO.slettAlleHandlelister() // sletter handlelister i lokal DB
        _isLoggedIn.value = false
    }

    /**
     * Funksjonen overfører alle varenavn hentet fra tjener til lokal database
     * Inserter class Varer med antall =0 i Entity Varer
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
     * Funksjonen overfører handlelister fra tjener og inserter Varer til lokal DB
     * OBS! Erstatter eksisterende duplikater, server eier sannheten
     */
    fun oppdaterListeFraApi() = viewModelScope.launch {
        if (!isLoggedIn.value) { // handlelister forutsetter innlogging
            _status.value = "oppdaterListeFraApi: Ingen bruker innlogget"
            println(status.value)
        } else {
            _status.value = "oppdaterListeFraApi prøver å oppdatere lokal DB (handlelister) fra API"
            println(status.value)
            try {
                API.retrofitService.getVareliste() // oppdaterer vareliste fra tjener
                // Henter unike listenavn fra tjener
                val alleHandlelister = API.retrofitService.getHandlelister(brukernavn.value)
                for (lister in alleHandlelister) { // for alle unike listenavn, hent alle rader
                    val komplettListe = API.retrofitService.getHandleliste(brukernavn.value, lister)
                    for (varenavn in hentVarerAPI.value!!) { // og for alle varer
                        if (komplettListe.containsKey(varenavn)) { // hvis vare er i sentral DB:
                            varerDAO.insertAllForce( // insert lokal DB, duplikater erstattes
                                Varer( // class Varer
                                    lister, // listenavn/ tittel
                                    varenavn, // varenavn
                                    komplettListe.getValue(varenavn) // antall
                                )
                            )
                        }
                        else { // hvis vare ikke er i sentral DB:
                            varerDAO.insertAllForce( // nullstiller lokale lister
                                Varer( // class Varer
                                    lister, // tittel/ listenavn
                                    varenavn, // varenavn
                                    0 // antall
                                )
                            )
                        }
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
     * Funksjonen henter ut enhetspris for en enkelt vare pr butikk
     */
    fun finnPrisPrVare(butikknavn: String, varenavn: String) : String {
        var pris = ""
        val indeksForButikkNavn = indeksForButikk(butikknavn)
        _status.value = "finnPrisPrVare prøver å vise enhetspris"
        println(status.value)
        try { // lopper ut enhetspris fra serverdata
            for (priser in priserPrButikk.value!!.varer) {
                if (priser.key == varenavn) {
                    priserPrButikk.value?.varer?.get(priser.key)
                        ?.get(indeksForButikkNavn)?.let {
                            pris = it // ny enhetspris
                        }
                    break // pris funnet, bryt ut
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
     * Funksjonen regner ut sum pr handleliste pr butikk
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
                            ?.get(indeksForButikkNavn)?.toDouble().let { // hent butikk (indeks)
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

    /**
     * Funksjonen kalles fra composables for å oppdatere hvilken handleliste
     * som vises. Kaller på oppdatering av state
     */
    fun setListeNavn(nyttListeNavn: String) {
        currentListenavn = nyttListeNavn
        oppdaterListenavn() // for rekomposisjon
    }

    /**
     * Tar inn nytt butikknavn og kaller på oppdatering av state
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
     * Lager kopi av current butikknavn for rekomp
     */
    private fun oppdaterButikknavn() {
        _uiStateNy.update { currentState ->
            currentState.copy(
                butikknavn = currentButikk,
            )
        }
    }

    /**
     * Hjelpefunksjon som sjekker om bruker er lagret i lokal DB
     * Slik at bruker slipper å logge inn på nytt
     * OBS! Vil kaste og fange unntak dersom bruker ikke er opprettet på telefon (lokal DB)
     */
    private fun kontrollerForBruker() {
        _status.value = "Prøver å kontrollere lokal DB for brukere"
        println(status.value)
        try {
            if (brukerDAO.getBruker().brukerNavn.isNotEmpty()) {
                _isLoggedIn.value = true
                _brukernavn.value = brukerDAO.getBruker().brukerNavn
                _sessionId.value = brukerDAO.getBruker().sessionId
                _status.value = "Vellykket, bruker funnet i lokal DB"
                println(status.value)
            }
        } catch (e:Exception) {
            _status.value = "Mulig feil kontrollerForBruker: ${e.message}"
            println(status.value)
        }
    }

    /**
     * Funksjonen henter handleliste pr listenavn fra lokal DB
     */
    fun getLokaleVarer(listenavn: String) = viewModelScope.launch {
        _status.value = "getLokaleVarer prøver å vise varer fra lokal DB"
        println(status.value)
        try {
            alleVarer = varerDAO.getAlleVarer(listenavn).asLiveData() // ny spørring lokal DB
            _status.value = "Vellykket, varer fra lokal Db hentet"
            println(status.value)
        } catch (e: Exception) {
            _status.value = "Feil: ${e.message}"
            println(status.value)
        }
    }

    /**
     * Funksjonen henter ut unike listenavn fra lokal DB
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
     * Funksjonen øker antall med 1.
     * Først oppdateres lokal DB. Når lokal DB svarer bekreftende oppdateres sentral DB.
     * Om antall > 0 opprettes aktuell handleliste i sentral DB.
     */
    fun inkementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch {
        try { // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.inkrementerAntall(varenavn, listenavn) == 1) {
                // API oppretter handleliste/ legger til vare/ antall++
                API.retrofitService.inkrementerHandleliste(
                    brukernavn.value,
                    listenavn,
                    varenavn,
                    sessionId.value
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
     * Funksjonen reduserer antall med 1.
     * Først oppdateres lokal DB. Når lokal DB svarer bekreftende oppdateres sentral DB.
     * Om antall == 0 slettes vare fra sentral DB
     */
    fun dekrementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch {
        try {   // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.dekrementerAntall(varenavn, listenavn) == 1) {
                // API sletter handleliste/ sletter vare/ antall--
                API.retrofitService.dekrementerHandleliste(
                    brukernavn.value,
                    listenavn,
                    varenavn,
                    sessionId.value
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

    /**
     * Funksjon for å sette antall til 0 -> fortsatt valgbar i lokal DB, men slettet fra sentral DB
     */
    fun settAntallTilNull(varer: Varer) = viewModelScope.launch {
        _status.value = "settAntallTilNull forsøker å oppdatere databaser"
        try { // hvis tilNull lokalt: slett sentralt
            if (varerDAO.antallTilNull(varer.varenavn, varer.listenavn) == 1) {
                API.retrofitService.slettVareIListe(
                    brukernavn.value,
                    varer.listenavn,
                    varer.varenavn,
                    sessionId.value
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
                API.retrofitService.slettHandleliste(brukernavn.value, currentListenavn, sessionId.value)
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
        if (alleListenavn.value == null) {
            return false // eneste/ første navn
        }
        return alleListenavn.value!!.contains(listenavn)
    }

} // slutt class PrisjegerViewModel









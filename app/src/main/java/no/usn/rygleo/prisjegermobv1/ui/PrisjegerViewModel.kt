
package no.usn.rygleo.prisjegermobv1.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.usn.rygleo.prisjegermobv1.API // companion-objekt server/ API
import no.usn.rygleo.prisjegermobv1.data.*
import no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem
import no.usn.rygleo.prisjegermobv1.roomDB.*


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
     * Kjører når:
     * init, ved navigasjon til handleliste, ved "oppdater handlelister" i HandlelisteScreen,
     * ved opprettelse av ny handleliste,
     */
    fun oppdaterAlleDataFraApi() {
        getAPIVarer() // oppdaterer varenavn fra server
        getAPIButikker() // oppdaterer butikknavn fra server
        getAPIPriserPrButikk() // oppdaterer priser fra server
        oppdaterVarerFraApi() // overfører varenavn til lokal DB
        oppdaterListeFraApi() // oppdaterer alle handlelister fra server til lokal DB
        getLokaleVarer() // emitter alle handlelister fra lokal DB, inkludert antall = 0
        getAlleListenavn() // henter unike handlelistenavn fra lokal DB
    }





    //funksjon for å registrere ny bruker
    fun postAPIRegistrer(epost: String, passord: String) {
        val map = mapOf("epost" to epost, "passord" to passord)
        viewModelScope.launch {
            try {
                _registrerAPI.value = API.retrofitService.registrerBruker(map)
                registrerAPI = _registrerAPI
            }
            catch (e: Exception) {
                _status.value = "Feil postAPIRegistrer: ${e.message}"
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
     * Kjøres ved oppstart og legger nye vareobjekter inn i lokal DB
     * Varer som allerede er lagret i lokal DB med identisk listenavn blir ignorert
     *
     */
    private fun getAPIVarer() {
        viewModelScope.launch {
            _status.value = "Prøver å hente varenavn fra API"
            try {
                _hentVarerAPI.value = API.retrofitService.getVareliste()
                _status.value = "Vellykket, varenavn hentet"
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }





    /**
     * Funksjonen henter inn Array av butikknavn (String) fra backend API via interface RestApi
     * Kjøres ved oppstart og legger alle butikknavn inn i Array
     *
     */
    private fun getAPIButikker() {
        viewModelScope.launch {
            _status.value = "Prøver å hente butikknavn fra API"
            try {
                _butikkerAPI.value = API.retrofitService.getButikkliste()
                _status.value = "Vellykket, butikknavn hentet"
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }





    /**
     * Funksjonen henter inn Array av priser pr vare pr butikk (++)
     * Kjøres ved oppstart
     */
    fun getAPIPriserPrButikk() {
        viewModelScope.launch {
            println("getAPIPriserPrButikk kjører")
            _status.value = "Prøver å hente butikknavn fra API"
            try {
                _priserPrButikk.value = API.retrofitService.getPrisPrButikk()
                _status.value = "Vellykket, priserPrButikk hentet"
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }





    /**
     * Funksjonen sender brukernavn og passord til server som returnerer status
     */
    fun postAPILogin(epost: String, passord: String) {
        val map = mapOf("epost" to epost, "passord" to passord)
        viewModelScope.launch {
            _status.value = "prøver å logge inn bruker"
            try {
                _brukerAPI.value = API.retrofitService.login(map)
                if(brukerAPI.value?.get("melding").equals("innlogget")){
                    _isLoggedIn.value = true
                    // TODO: endret verdi for oppdatering av brukernavn:
                //    _brukernavn.value = brukerAPI.value?.get("bruker").toString()
                    _brukernavn.value = epost
                    // TODO: lagrer bruker i lokal DB
                    brukerDAO.insert(Bruker(epost)) // insert av ny bruker til lokal DB
                    _status.value = "Vellykket, bruker innlogget og lagret" // vellykket
                }
            } catch (e: Exception) {
                _status.value = "Feil postAPILogin: ${e.message}"
            }
        }
    }
    //funksjon for å logge ut
    fun postAPILoggout(){
        _isLoggedIn.value=false
    }






    /**
     * Funksjonen overfører alle varenavn fra API og inserter varer med antall =0 til lokal DB
     */
    fun oppdaterVarerFraApi() {
        println("oppdaterVarerFraApi kjører")
        _status.value = "Prøver å oppdatere lokal DB (varenavn) fra API"
        try {
            for (varenavn in _hentVarerAPI.value!!) {
                val vareApi = Varer(currentListenavn, varenavn, 0)
                insertVare(vareApi) // insert lokal DB IGNORE
            }
            _status.value = "Vellykket, varenavn i lokal DB oppdatert"
        } catch (e: Exception) {
            _status.value = "Feil oppdaterVarerFraApi: ${e.message}"
        }
    }






    /**
     * Funksjonen overfører handlelister fra API og inserter Varer til lokal DB
     * OBS! Erstatter eksisterende varer, server eier sannheten om handlelister
     * TODO: Denne kan potensielt lytte på endringer på server, me må testes mer
     * TODO: DENNE METODEN HAR VÆRT SKYLD I MYE BUGS, BLE KJØRT FOR OFTE/ KONTINUERLIG
     */
    fun oppdaterListeFraApi() = viewModelScope.launch {
        println("oppdaterListerFraApi kjører")
        _status.value = "Prøver å oppdatere lokal DB (handlelister) fra API"
        try { // henter alle unike handlelistenavn fra server
            val alleHandlelister = API.retrofitService.getHandlelister(brukernavn.value)
            for (lister in alleHandlelister) { // henter alle handlelisterader fra server
                val komplettListe = API.retrofitService.getHandleliste(brukernavn.value, lister)
                for (varer in komplettListe) {
                    varerDAO.insertAllForce(Varer( // insert REPLACE - server eier sannheten
                        lister, // listenavn/ tittel
                        varer.key, // varenavn
                        varer.value // antall
                    ))
                }
            }
            _status.value = "Vellykket, handlelister i lokal DB oppdatert"
        } catch (e: Exception) {
            _status.value = "Feil oppdaterListeFraApi: ${e.message}"
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
        _status.value = "Prøver å vise pris pr vare pr butikk (detaljer)"
        try { // lopper ut enhetspris fra serverdata
            for (priser in _priserPrButikk.value?.varer!!) {
                if (priser.key == varenavn) {
                    _priserPrButikk.value?.varer?.get(priser.key)
                        ?.get(indeksForButikkNavn)?.let {
                            pris = it // ny enhetspris
                        }
                }
            }
            _status.value = "Vellykket, pris pr vare pr butikk vises"
        } catch (e: Exception) {
            _status.value = "Feil finnPrisPrVare: ${e.message}"
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
        println("finnSumPrButikkNavn kjører")
        _status.value = "Prøver å vise sum pr handleliste pr butikk (detaljer)"
        try { // looper alle varer i lokal DB og alle priser fra server
            for (varer in alleVarer.value!!) {
                for (priser in _priserPrButikk.value?.varer!!) { // hvis rett vare og liste,
                    if (priser.key == varer.varenavn && varer.listenavn == currentListenavn) {
                        _priserPrButikk.value?.varer?.get(priser.key)
                            ?.get(indeksForButikkNavn)?.toDouble().let { // hent rett butikk (indeks)
                                pris += it?.times(varer.antall) ?: 0.0 // aggregerer antall*pris
                            }
                    }
                }
            }
            _status.value = "Vellykket, viser sum pr handleliste pr butikk"
        } catch (e: Exception) {
            _status.value = "Feil finnSumPrButikk: ${e.message}"
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
     */
    private fun kontrollerForBruker() {
        try {
            if (!brukerDAO.getBruker().brukerNavn.isEmpty()) {
                _isLoggedIn.value = true
                _brukernavn.value = brukerDAO.getBruker().brukerNavn
            }
        } catch (e:Exception) {
            // det er ikke opprettet bruker i databasen
            println("Bruker ikke opprettet")
        }
    }




    /**
     * Funksjonen etablerer og bytter ut innhold i LiveData -> LazyColumn fra lokal DB
     * Bytter til varer med antall > 0
     */
    fun getSortertLokaleVarer() {
        println("getSortertLokaleVarer kjører")
        viewModelScope.launch {
            _status.value = "Henter valgte varer fra lokal DB"
            try {
                alleVarer = varerDAO.getAlleValgteVarer().asLiveData() // ny spørring lokal DB
                _status.value = "Vellykket, sorterte varer hentet"
                setSortert() // rekomposisjon
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }






    /**
     * Funksjonen etablerer og bytter ut innhold i LiveData -> LazyColumn fra lokal DB
     * Henter alle varer uavhengig av antall
     */
    fun getLokaleVarer() {
        println("getLokaleVarer kjører")
        viewModelScope.launch {
            _status.value = "Henter alle varer fra lokal DB"
            try {
                alleVarer = varerDAO.getAlleVarer().asLiveData() // ny spørring lokal DB
                _status.value = "Vellykket, alle varer hentet"
                setUsortert() // rekomposisjon
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }






    /**
     * Funksjonen henter ut unike listenavn fra lokal DB
     *
     */
    fun getAlleListenavn() {
        println("getAlleListenavn kjører")
        viewModelScope.launch {
            _status.value = "Henter unike listenavn fra lokal DB"
            try {
                alleListenavn = varerDAO.getAlleListenavn().asLiveData()
                _status.value = "Vellykket, unike listenavn hentet ut"
                //         setSortert() // rekomposisjon
            } catch (e: Exception) {
                _status.value = "Feil getAlleListenavn: ${e.message}"
            }
        }
    }






    /**
     * Funksjonen øker antall med 1, både lokalt og sentralt
     */
    fun inkementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch {
        try { // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.inkrementerAntall(varenavn, listenavn) == 1)
                // API oppretter handleliste/ legger til vare/ antall++
                API.retrofitService.inkrementerHandleliste(
                    brukernavn.value,
                    listenavn,
                    varenavn
                )
            else println("Feil inkrementerVareAntall : Lokal database svarer ikke")
        } catch (e: Exception) {
            println("Feil inkrementerVareAntall: ${e.message}")
        }
    }






    /**
     * Funksjonen reduserer antall med 1, både lokalt og sentralt
     */
    fun dekrementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch {
        try {   // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.dekrementerAntall(varenavn, listenavn) == 1)
                // API sletter handleliste/ sletter vare/ antall--
                API.retrofitService.dekrementerHandleliste(
                    brukernavn.value,
                    listenavn,
                    varenavn
                )
            else println("Feil dekrementerVareAntall : Lokal database svarer ikke")
        } catch (e: Exception) {
            println("Feil dekrementerVareAntall: ${e.message}")
        }
    }






    /**
     * Fuksjon for å sette inn ny vare i lokal DB, tabell Vare (handlelister)
     * OBS: ignorerer ny ved duplikat
     */
    private fun insertVare(vare: Varer) = viewModelScope.launch {
        varerDAO.insertAll(vare)
    }





    /**
     * Fuksjon for å sette inn ny vare i lokal DB, tabell Vare (handlelister)
     * OBS: erstatter ved duplikat!
     */
    private fun insertVareForce(vare: Varer) = viewModelScope.launch {
        varerDAO.insertAllForce(vare)
    }





    /**
     * Funksjon for å slette en vare fra lokal/ sentral DB
     * TODO: ny logikk setter antall i 0, denne er ikke i bruk
     */
    fun slettVare(varer: Varer) = viewModelScope.launch(Dispatchers.IO) {
        try { // hvis slettet lokalt: slett sentralt
            if (varerDAO.slettVare(varer) == 1)
                API.retrofitService
                    .slettVareIListe(
                        brukernavn.value,
                        varer.listenavn,
                        varer.varenavn
                    )
            else println("Feil slettVare: Lokal database svarer ikke")
        } catch (e:Exception) {
            println("Feil slettVare: ${e.message}")
        }
    }





    /**
     * Funksjon for å sette antall til 0 -> blir valgbar i lokal DB, men slettet fra sentral
     */
    fun settAntallTilNull(varer: Varer) = viewModelScope.launch(Dispatchers.IO) {
        try { // hvis tilNull lokalt: slett sentralt
            if (varerDAO.antallTilNull(varer.varenavn, varer.listenavn) == 1)
                API.retrofitService.slettVareIListe(
                    brukernavn.value,
                    varer.listenavn,
                    varer.varenavn
                )
            else println("Feil settAntallTilNull: Lokal database svarer ikke")
        }
        catch (e:Exception) {
            println("Feil settAntallTilNull: ${e.message}")
        }
    }





    /**
     * Funksjon for å slette en handleliste fra lokal/ sentral DB
     */
    fun slettHandleliste() = viewModelScope.launch {
        try {// Hvis slettet fra lokal DB, slett fra sentral DB
            if (varerDAO.slettHandleliste(currentListenavn) >= 1)
                API.retrofitService.slettHandleliste(brukernavn.value, currentListenavn)
            else println("Feil slettHandleliste: Lokal database svarer ikke")
        } catch (e: Exception) {
            println("Feil settAntallTilNull: ${e.message}")
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









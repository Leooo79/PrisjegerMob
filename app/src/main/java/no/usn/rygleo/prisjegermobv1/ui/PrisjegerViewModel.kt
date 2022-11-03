
package no.usn.rygleo.prisjegermobv1.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.usn.rygleo.prisjegermobv1.API
import no.usn.rygleo.prisjegermobv1.data.*
import no.usn.rygleo.prisjegermobv1.roomDB.AppDatabase
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import no.usn.rygleo.prisjegermobv1.roomDB.VarerDAO


/**
 * Klassen inneholder logikk for App Prisjeger
 * Kommuniserer med screens (visningskomponenter)
 * Kommuniserer med datakilder : backend API/ lokal DB (Room)
 * NYTT 21.10.22 : NYE DATA HENTES FRA API VED OPPSTART OG LEGGES I LOKAL DB (CONFLICT = IGNORE)
 *
 */
class PrisjegerViewModel(application: Application) : AndroidViewModel(application) {

    /**
     * Variabler for oppkobling mot backend API
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


    // VARIABLER FOR Å LESE INN BUTIKKLISTE FRA API
    private val _priserPrButikk = MutableLiveData<PriserPrButikk>()
    val priserPrButikk: LiveData<PriserPrButikk> = _priserPrButikk


    // VARIABLER FOR Å LESE INN HANDLELISTE FRA API
    private val _handlelisteAPI = MutableLiveData<Map<String, Int>>()
    val handlelisteAPI: LiveData<Map<String, Int>> = _handlelisteAPI


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
     * Variabler for oppkobling mot lokal database (Room)
     */
    // Referanse til repo
  //  private val repoVarer: VarerRepo

    // var ( ikke val ) pga ønske om Flow fra Room lokal DB
    lateinit var alleVarer: LiveData<List<Varer>>
        private set

    // Variabel for uthenting og lagring av listenavn fra lokal DB
    lateinit var alleListenavn: LiveData<Array<String>>
        private set

    // Default liste(navn) som skal vises TODO: siste lagrede??
    var currentListenavn = "Default" // VARIABEL FOR INNEVÆRENDE HANDLELISTENAVN
    var currentButikk = "Meny" // VARIABEL FOR INNEVÆRENDE BUTIKK
    var currentEpost = "tore@mail.com" // VARIABEL FOR INNEVÆRENDE BUTIKK

    // Referanse til DAO for handlelister i lokal database
    val varerDAO: VarerDAO





    /**
     * Statevariabeler i egen klasse for å ivareta endringer i state utover LiveData
     * For rekomposisjon, færre variabler på tvers av composables
     */
    private val _uiStateNy = MutableStateFlow(
        VarerUiState(
            listenavn = currentListenavn,
            sortert = false,
            butikknavn = currentButikk
        )
    )
    val uiStateNy: StateFlow<VarerUiState> = _uiStateNy.asStateFlow()





    /**
     * "Konstruktør" ved hjelp av init : kode som kjøres ved oppstart (instansiering av vM)
     * Her kan man legge inn initielle kall på API og andre oppstartsrutiner
     *
     */
    init {
        // HENTER/ SENDER DATA FRA/ TIL SERVER-API:
        getAPIVarer() // oppdaterer varenavn fra server
        getAPIButikker() // oppdaterer butikknavn fra server
        getAPIPriserPrButikk() // oppdaterer priser fra server
        postAPILogin("tore@mail.com", "passord") // post - login mot server
        getAPIHandleliste(currentEpost, currentListenavn) // TODO: når hente/ vise handlelister fra server

        // ETABLERER DATA I LOKAL DB, VISER LIVEDATA FRA LOKAL DB:
        varerDAO = AppDatabase.getRoomDb(application).varerDAO() // etablerer lokal DB om ikke finnes
     //   repoVarer = VarerRepo(varerDAO) // initierer repo
        getLokaleVarer() // henter inn alle varer fra lokal DB (Flow) og viser i LiveData
        getAlleListenavn() // henter alle unike listenavn fra lokal DB og legger i Array
    }



    
    //funksjon for å registrere ny bruker  
     fun postAPIRegistrer(epost: String, passord: String){
        val map = mapOf("epost" to epost, "passord" to passord)
        viewModelScope.launch {
            try {

                _registrerAPI.value = API.retrofitService.registrerBruker(map)
                registrerAPI = _registrerAPI
            }
            catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
                 }

            }
    }
     //funksjon for å sette et globalt brukernavn 
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
    private fun getAPIPriserPrButikk() {
        viewModelScope.launch {
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
                    _brukernavn.value = brukerAPI.value?.get("bruker").toString()
                    _status.value = "Vellykket, bruker innlogget"
                }
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }
    //funksjon for å logge ut 
    fun postAPILoggout(){
        _isLoggedIn.value=false
    }




    /**
     * Funksjonen henter brukers handleliste fra server, epost + listenavn
     * TODO: Er det denne vi skal vise til bruker ved oppstart?
     * Kjøres ved oppstart
     */
    private fun getAPIHandleliste(epost: String, listenavn: String) {
        viewModelScope.launch {
            _status.value = "Prøver å hente handleliste fra API"
            try {
                _handlelisteAPI.value = API.retrofitService.getHandleliste(epost, listenavn)
                _status.value = "Vellykket, handleliste fra API hentet"
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }




    /**
     * Funksjonen overfører varenavn fra API og inserter Varer til lokal DB
     * OBS! Kostbar ved store overføringer
     * TODO: SKAL VI HA EN DEFAULT-LISTE MED ALLE VARENAVN FOR SØK, OG KUN ANTALL > 0 I BRUKERLISTER?
     */
    fun oppdaterListeFraApi() {
        _status.value = "Prøver å oppdatere lokal DB (varenavn) fra API"
        try {
            for (varenavn in _hentVarerAPI.value!!) {
                val vareApi = Varer(currentListenavn, varenavn, 0.0, 0)
                insertVare(vareApi)
            }
            _status.value = "Vellykket, varenavn i lokal DB oppdatert"
        } catch (e: Exception) {
            _status.value = "Feil: ${e.message}"
        }
    }





    /**
     * Funksjonen overfører priser fra API og legger disse i lokal DB
     * Det er nyeste priser som hentes inn ved oppstart, og som oppdateres ved "oppdater"
     * OBS! Kostbar ved store overføringer
     */
    fun oppdaterPriserFraApi(butikknavn: String) {
        // TODO: ENHETSPRISER MÅ IKKE NØDVENDIGVIS LIGGE I LOKAL DB, BØR UANSETT ALLTID OPPDATERES
        val indeksForButikkNavn = indeksForButikk(butikknavn)
        _status.value = "Prøver å oppdatere lokal DB (enhetspris) fra API"
        try {
            for (varer in alleVarer.value!!) {
                for (priser in _priserPrButikk.value?.varer!!) {
                    if (priser.key == varer.varenavn) {
                        _priserPrButikk.value?.varer?.get(priser.key)
                            ?.get(indeksForButikkNavn)?.let {
                                oppdaterVarePris( // oppdaterer lokal DB med enhetspris pr vare
                                    varer.varenavn,
                                    varer.listenavn,
                                    it.toDouble() // ny enhetspris
                                )
                            }
                    }
                }
            }
            _status.value = "Vellykket, enhetspriser i lokal DB oppdatert"
        } catch (e: Exception) {
            _status.value = "Feil: ${e.message}"
        }
    }






    /**
     * Funksjonen henter ut enhetspris for en enkelt vare
     * Benyttes i HandlelisteScreen for visning av detaljer pr vare
     * TODO: Nesten lik funksjon som oppdaterPriserFraApi, bør integreres
     */
    fun finnPrisPrVare(butikknavn: String, varenavn: String) : String {
        var pris = ""
        val indeksForButikkNavn = indeksForButikk(butikknavn)
        _status.value = "Prøver å vise pris pr vare pr butikk (detaljer)"
        try {
            for (priser in _priserPrButikk.value?.varer!!) {
                if (priser.key == varenavn) {
                    _priserPrButikk.value?.varer?.get(priser.key)
                        ?.get(indeksForButikkNavn)?.let {
                            pris = "$it,-" // ny enhetspris

                }
            }
        }
            _status.value = "Vellykket, pris pr vare pr butikk vises"
        } catch (e: Exception) {
            _status.value = "Feil: ${e.message}"
            return "Pris mangler"
        }
        return pris
    }






    /**
     * Funksjonen henter ut sum pr handleliste pr butikk
     * Benyttes i HandlelisteScreen
     * TODO: Nesten lik funksjon som oppdaterPriserFraApi, bør integreres
     */
    fun finnSumPrButikk(butikknavn: String) : String {
        var pris = 0.0
        val indeksForButikkNavn = indeksForButikk(butikknavn)
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
            _status.value = "Feil: ${e.message}"
            return "-1"
        }
        return (Math.round(pris * 100.00) / 100.0).toString()+",-" // avrunding 2 des.
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
     * Funksjonen etablerer og bytter ut innhold i LiveData -> LazyColumn fra lokal DB
     * Bytter til varer med antall > 0
     */
    fun getSortertLokaleVarer() {
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
        viewModelScope.launch {
            _status.value = "Henter unike listenavn fra lokal DB"
            try {
                alleListenavn = varerDAO.getAlleListenavn().asLiveData()
                _status.value = "Vellykket, unike listenavn hentet ut"
                //         setSortert() // rekomposisjon
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }






    /**
     * Funksjon for å regne ut sum pr vare i handlelister
     * returnerer antall * enhetspris
     */
    fun sumPrVare(vare: Varer): String {
        return (Math.round(vare.antall * vare.enhetspris * 100.00) / 100.0).toString()
    }






    /**
     * Funksjon for å regne ut sum pr handleliste.
     * Kalles fra composables (HandlelisteScreen.HeaderVisning())
     */
    fun sumPrHandleliste(): String {
        var sum = 0.0
        alleVarer.value
            ?.forEach { varer ->
                if (varer.listenavn == currentListenavn)
                    sum += varer.antall.times(varer.enhetspris)
            }
        return (Math.round(sum * 100.00) / 100.0).toString()+",-"
    }







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






    /**
     * Fuksjon for å sette inn ny vare i lokal DB, tabell Vare (handlelister)
     */
    private fun insertVare(vare: Varer) = viewModelScope.launch(Dispatchers.IO) {
        varerDAO.insertAll(vare)
    }






    /**
     * Fuksjonen returnerer en vares antall fra lokal DB
     * TODO: brukes ikke
     */
    fun getVareAntall(vare: Varer) = viewModelScope.launch(Dispatchers.IO) {
        varerDAO.getVareAntall(vare.varenavn, vare.listenavn)
    }





    /**
     * Funksjonen øker antall med 1, både lokalt og sentralt
     */
    fun inkementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch(Dispatchers.IO) {
            // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.inkrementerAntall(varenavn, listenavn) == 1) {
                try {
                    // API oppretter handleliste/ legger til vare/ antall++
                    API.retrofitService.inkrementerHandleliste(
                        currentEpost,
                        listenavn,
                        varenavn
                    )
                } catch (e: Exception) {

                }
            } else {
                // throw Exception(E.toString())
                // Lokal DB ble ikke oppdatert
            }
    }





    /**
     * Funksjonen reduserer antall med 1, både lokalt og sentralt
     */
    fun dekrementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch(Dispatchers.IO) {
            // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.dekrementerAntall(varenavn, listenavn) == 1) {
                try {
                    // API sletter handleliste/ sletter vare/ antall++
                    API.retrofitService.dekrementerHandleliste(
                        currentEpost,
                        listenavn,
                        varenavn
                    )
                } catch (e: Exception) {

                }
            } else {
                // throw Exception(E.toString())
                // Lokal DB ble ikke oppdatert
            }
    }



/*  // TODO: denne gir noen ganger antall < 0. Tror emulator surrer, men usikker
    fun oppdaterVareAntall(endring: Int, varenavn: String, listenavn: String) =
        viewModelScope.launch(Dispatchers.IO) {
            // Lokal DB redigerer antall for aktuell vare og handleliste (+/-1)
            // if lokal DB har oppdatert 1 rad : == 1
            if (varerDAO.oppdaterAntall(endring, varenavn, listenavn) == 1) {
                try {
                    if (endring > 0) {
                        // API oppretter handleliste/ legger til vare/ antall++
                        API.retrofitService.inkrementerHandleliste(
                            currentEpost,
                            listenavn,
                            varenavn
                        )
                    }
                    else {
                        // API sletter handleliste/ fjerner vare/ antall--
                        API.retrofitService.dekrementerHandleliste(
                            currentEpost,
                            listenavn,
                            varenavn
                        )
                    }
                } catch (e: Exception) {

                }
            } else {
               // throw Exception(E.toString())
            }
    }

 */






    /**
     * Funksjon for å oppdatere Varer-objekt enhetspris i lokal DB
     * TODO: Slå sammen alle update-metoder
     */
    fun oppdaterVarePris(varenavn: String, listenavn: String, enhetspris: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            varerDAO.oppdaterPris(varenavn, listenavn, enhetspris)
        }




    /**
     * Funksjon for å oppdatere en vare uten parameter
     * TODO: IKKE I BRUK
     */
    fun oppdaterVare2(vare: Varer) = viewModelScope.launch(Dispatchers.IO) {
        varerDAO.update(vare)
    }






    /**
     * Funksjon for å slette en vare fra lokal/ sentral DB
     */
    fun slettVare(varer: Varer) = viewModelScope.launch(Dispatchers.IO) {
        // TODO: Trenger ny metode i backend. Under arbeid
        varerDAO.slettVare(varer)
        try {
            API.retrofitService.slettVareIListe(currentEpost, varer.listenavn, varer.varenavn)
        } catch (e:Exception) {

        }
    }





    /**
     * Funksjon for å slette en handleliste fra lokal/ sentral DB
     */
    fun slettHandleliste() = viewModelScope.launch(Dispatchers.IO) {
        // sletter handleliste fra lokal DB
        varerDAO.slettHandleliste(currentListenavn)
        // sletter handleliste fra sentral DB
        API.retrofitService.slettHandleliste(currentEpost, currentListenavn)
    }




    /**
     * KUN FOR TESTING
     * Funksjon for å opprette en liste av Varer
     * Kall på varelinjer fra API gjør denne jobben automatisk
     */
    private fun manuellVareliste(): List<Varer> {
        return listOf(
            Varer("RoomListe1", "AGGGGGGGGGGgurk, 1 stk", 11.11, 5),
            Varer("RoomListe1", "Aromat Krydder, 90 gram", 22.22, 4),
            Varer("RoomListe1", "Avløpsåpner Pulver Plumbo, 600 gr", 33.33, 0),
            Varer("RoomListe1", "Bakepulver Freia, 250 gram", 44.44, 0),
            Varer("RoomListe1", "Fish and Crips, Findus, 480 gram", 55.55, 0),
            Varer("RoomListe1", "Daim Dobbel Freia, 56 gr", 66.66, 0),
            Varer("RoomListe1", "Havregryn lettkokte Axa, 1,1 kg", 77.77, 0),
            Varer("RoomListe1", "Favoritt Salami, Gilde, 150 gram", 88.88, 0),
            Varer("RoomListe1", "Gilde Kjøttkaker, 800 gram", 99.99, 0),
            Varer("RoomListe1", "Grillpølser Gilde, 600 gr", 100.0, 0),
            Varer("RoomListe1", "Kokt skinke Gilde, 110 gr", 110.11, 0),
            Varer("RoomListe1", "Bretagne kylling saus, 27 gram", 239.89, 0),
            Varer("RoomListe1", "Kjøttdeig billigste type, 400 gr", 999.99, 0),
            Varer("RoomListe2", "AGGGGGGGGGGgurk, 1 stk", 11.11, 5),
            Varer("RoomListe2", "Aromat Krydder, 90 gram", 22.22, 4),
            Varer("RoomListe2", "Avløpsåpner Pulver Plumbo, 600 gr", 33.33, 0),
            Varer("RoomListe2", "Bakepulver Freia, 250 gram", 44.44, 0),
            Varer("RoomListe2", "Fish and Crips, Findus, 480 gram", 55.55, 0),
        )
    }


} // slutt class PrisjegerViewModel










package no.usn.rygleo.prisjegermobv1.ui

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.usn.rygleo.prisjegermobv1.API
import no.usn.rygleo.prisjegermobv1.data.*
import no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem
import no.usn.rygleo.prisjegermobv1.roomDB.*


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


    // Default liste(navn) som skal vises TODO: alle brukere må ha "MinHandleliste" som default
    var currentListenavn = "MinHandleliste" // VARIABEL FOR INNEVÆRENDE HANDLELISTENAVN
    var currentButikk = "Velg butikk" // VARIABEL FOR INNEVÆRENDE BUTIKK


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

        // ETABLERER DATA I LOKAL DB, VISER LIVEDATA FRA LOKAL DB:
        varerDAO = AppDatabase.getRoomDb(application).varerDAO() // etablerer lokal DB om ikke finnes
        getLokaleVarer()
        getAlleListenavn()

        // OPPDATERER LOKAL DB MED DATA FRA SERVER - ALLE VARER, ALLE HANDLELISTER
        oppdaterVarerFraApi() // Oppdaterer eventuelle nye varer fra server
        oppdaterListeFraApi() // Oppdaterer handlelisterader fra server i lokal DB TODO: kan kjøre i init
    }






    /**
     * Hjelepefunksjon for å oppdatere alle data fra server til lokal DB/ visning
     */
    fun oppdaterAlleDataFraApi() {
        getAPIVarer() // oppdaterer varenavn fra server
        getAPIButikker() // oppdaterer butikknavn fra server
        getAPIPriserPrButikk() // oppdaterer priser fra server
        oppdaterVarerFraApi() // overfører varenavn til lokal DB
        oppdaterListeFraApi() // oppdaterer alle handlelister fra server til lokal DB
        getLokaleVarer() // emitter alle handlelister i lokal DB, inkludert antall = 0
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
    private fun getAPIPriserPrButikk() {
        viewModelScope.launch {
            _status.value = "Prøver å hente butikknavn fra API"
            try {
                _priserPrButikk.value = API.retrofitService.getPrisPrButikk()
           //     oppdaterPriser()
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
                    _status.value = "Vellykket, bruker innlogget"
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
     * Funksjonen overfører alle varenavn fra API og inserter "tomme" Varer til lokal DB
     * OBS! Kostbar ved store overføringer
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
     * TODO: Denne kan potensielt lytte på endringer på server, men bruker for lang tid
     * TODO: på oppdatering. DENNE METODEN HAR VÆRT SKYLD I MYE BUGS
     */
    fun oppdaterListeFraApi() = viewModelScope.launch {
        println("oppdaterListerFraApi kjører") // TODO: denne lytter automatisk pga kall på API
        // TODO: MÅ LEGGE SVAR I EN FELLES LISTE
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
            for (varer in komplettListe) {
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





/*
    /**
     * Funksjonen overfører priser fra API og legger disse i lokal DB
     * Det er nyeste priser som hentes inn ved oppstart, og som oppdateres ved "oppdater"
     * OBS! Kostbar ved store overføringer
     */
    fun oppdaterPriserFraApi(butikknavn: String) {
        // TODO: Har endret funksjon for å lese inn priser, this is backup
        val indeksForButikkNavn = indeksForButikk(butikknavn)
        _status.value = "Prøver å oppdatere lokal DB (enhetspris) fra API"
        try {
            for (varer in alleVarer.value!!) {
                for (priser in _priserPrButikk.value?.varer!!) {
                    if (priser.key == varer.varenavn) {
                        _priserPrButikk
                            .value?.varer?.get(priser.key)
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

 */





    /**
     * Funksjonen henter ut enhetspris for en enkelt vare
     * Benyttes i HandlelisteScreen for visning av detaljer pr vare
     */
    fun finnPrisPrVare(butikknavn: String, varenavn: String) : String {
        var pris = ""
        val indeksForButikkNavn = indeksForButikk(butikknavn)
        _status.value = "Prøver å vise pris pr vare pr butikk (detaljer)"
        try {
            getAPIPriserPrButikk() // TODO: oppdaterer priser fra server
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




/*
    fun finnAntallPrVare(listenavn: String, vareSok: String) : String {
        var antall = ""
        _status.value = "Prøver å vise pris pr vare pr butikk (detaljer)"
        try {
            for (varer in _alleHandlelisterAPI.value!!) {
                if (varer.key == vareSok) {
                    antall = varer.value.toString()
                }
            }
            _status.value = "Vellykket, pris pr vare pr butikk vises"
        } catch (e: Exception) {
            _status.value = "Feil finnAntallPrVare: ${e.message}"
            return "99"
        }
        return antall
    }


 */





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
    //        oppdaterListeFraApi() // TODO: OBS!! STOR KILDE TIL FEIL
            getAPIPriserPrButikk() // TODO: oppdaterer priser fra server
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
            return "Velg butikk"
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
     * Funksjon for å regne ut sum pr vare i handlelister
     * returnerer antall * enhetspris
     */
    fun sumPrVare(vare: Varer): Double {
        return (Math.round(vare.antall * finnPrisPrVare(currentButikk, vare.varenavn).toDouble() * 100.00) / 100.0)
    }


    /**
     * Funksjon for å kontrollere om nytt listenavn er duplikat
     */
    fun kontrollerListenavn(listenavn: String) : Boolean {
        return alleListenavn.value!!.contains(listenavn)
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
     * OBS: ignorerer ny ved duplikat
     */
    private fun insertVare(vare: Varer) = viewModelScope.launch {
        varerDAO.insertAll(vare)
    }





    /**
     * Fuksjon for å sette inn ny vare i lokal DB, tabell Vare (handlelister)
     * OBS: erstatter ved duplikat!
     */
    private fun insertVareForce(vare: Varer) = viewModelScope.launch  {
        varerDAO.insertAllForce(vare)
    }






    /**
     * Funksjonen øker antall med 1, både lokalt og sentralt
     */
    fun inkementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch {
            // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.inkrementerAntall(varenavn, listenavn) == 1) {
                try {
                    // API oppretter handleliste/ legger til vare/ antall++
                    API.retrofitService.inkrementerHandleliste(
                         // TODO: ikke riktig verdi
                       // brukerAPI.value?.get("bruker").toString(),
                        brukernavn.value,
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


/*
    /**
     * Funksjonen øker antall med 1, både lokalt og sentralt
     */
    fun inkementerVareAntall2(varenavn: String, listenavn: String) =
        viewModelScope.launch {
            // hvis lokal DB har oppdatert: oppdater sentral DB
            //    oppdaterListeFraApi() // oppdaterer antall i lokal DB
                try {
                    // API oppretter handleliste/ legger til vare/ antall++
                    API.retrofitService.inkrementerHandleliste(
                        // TODO: ikke riktig verdi
                        // brukerAPI.value?.get("bruker").toString(),
                        brukernavn.value,
                        listenavn,
                        varenavn
                    )
                    varerDAO.insertAllForce(Varer(listenavn, varenavn, 999))
                    //   oppdaterEnListeFraApi()
                } catch (e: Exception) {
                    println("Klarer ikke inkrementere")
                }
        }



 */




    /**
     * Funksjonen reduserer antall med 1, både lokalt og sentralt
     */
    fun dekrementerVareAntall(varenavn: String, listenavn: String) =
        viewModelScope.launch {
            // hvis lokal DB har oppdatert: oppdater sentral DB
            if (varerDAO.dekrementerAntall(varenavn, listenavn) == 1) {
                try {
                    // API sletter handleliste/ sletter vare/ antall--
                    API.retrofitService.dekrementerHandleliste(
                        brukernavn.value.toString(),
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




/*

    /**
     * Funksjonen reduserer antall med 1, både lokalt og sentralt
     */
    fun dekrementerVareAntall2(varenavn: String, listenavn: String) =
        viewModelScope.launch {
            // hvis lokal DB har oppdatert: oppdater sentral DB
            //    oppdaterListeFraApi() // oppdaterer antall i lokal DB
                try {
                    // API sletter handleliste/ sletter vare/ antall--
                    API.retrofitService.dekrementerHandleliste(
                        brukernavn.value.toString(),
                        listenavn,
                        varenavn
                    )
                    varerDAO.insertAllForce(Varer(listenavn, varenavn, 999))
                //    oppdaterEnListeFraApi()
                } catch (e: Exception) {
                    println("klarer ikke dekrementere")
                }
        }


 */






    /**
     * Funksjon for å slette en vare fra lokal/ sentral DB
     */
    fun slettVare(varer: Varer) = viewModelScope.launch(Dispatchers.IO) {
        // TODO: Trenger ny metode i backend. Under arbeid
        varerDAO.slettVare(varer)
        try {
            API.retrofitService.slettVareIListe(brukernavn.value, varer.listenavn, varer.varenavn)
        } catch (e:Exception) {

        }
    }





    /**
     * Funksjon for å sette antall til 0 -> blir valgbar i lokal DB, men slette fra sentral
     */
    fun settAntallTilNull(varer: Varer) = viewModelScope.launch {
        // TODO: Trenger ny metode i backend. Under arbeid
        varerDAO.antallTilNull(varer.varenavn, varer.listenavn)
        try {
            API.retrofitService.slettVareIListe(brukernavn.value, varer.listenavn, varer.varenavn)
        } catch (e:Exception) {

        }
    }





    /**
     * Funksjon for å slette en handleliste fra lokal/ sentral DB
     */
    fun slettHandleliste() = viewModelScope.launch {
        // Hvis slettet fra lokal DB, slett fra sentral DB
        if (varerDAO.slettHandleliste(currentListenavn) >= 1) {
            // sletter handleliste fra sentral DB
            API.retrofitService.slettHandleliste(brukernavn.value, currentListenavn)
        }
    }



/*

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

 */




} // slutt class PrisjegerViewModel









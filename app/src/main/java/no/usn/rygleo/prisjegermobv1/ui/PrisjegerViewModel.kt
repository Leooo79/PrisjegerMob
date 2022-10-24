
package no.usn.rygleo.prisjegermobv1.ui

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import no.usn.rygleo.prisjegermobv1.API
import no.usn.rygleo.prisjegermobv1.data.*
import no.usn.rygleo.prisjegermobv1.roomDB.AppDatabase
import no.usn.rygleo.prisjegermobv1.roomDB.Bruker
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


    /**
     * Variabler for oppkobling mot lokal database (Room)
     */
    // Referanse til repo
    private val repoVarer: VarerRepo

    // Livedata liste for komposisjon av handlelister fra lokal DB
    // var ( ikke val ) pga ønske om Flow fra Room lokal DB
    // Benytter som observerbar liste fra lokal DB til handlelistevisning (Screen)
    // Alle endringer i alleVarer trigger rekomposisjon, men ny spørring
    // trigges ved å endre statevariabler i class VarerUiState
    // _uiStateNy.update() {currentState->currentState.copy()}
    lateinit var alleVarer: LiveData<List<Varer>>
        private set

    // Variabel for uthenting og lagring av listenavn fra lokal DB
    // VARIABLER FOR Å LESE INN BUTIKKLISTE FRA API
    lateinit var alleListenavn: LiveData<Array<String>>
        private set

    // Default liste(navn) som skal vises TODO: siste lagrede??
    var currentListenavn = "Tore1" // VARIABEL FOR INNEVÆRENDE HANDLELISTENAVN
    var currentButikk = "Meny" // VARIABEL FOR INNEVÆRENDE BUTIKK
    var currentEpost = "tore@mail.com" // VARIABEL FOR INNEVÆRENDE BUTIKK

    // Referanse til DAO for handlelister
    val varerDAO: VarerDAO


    /**
     * Statevariabeler i egen klasse for å ivareta endringer i state utover LiveData
     * For rekomposisjon, færre variabler i composables
     * TODO: Her kan man etablere flere statevariabler, kan virke for hele App Prisjeger. Benytter kopier for å endre state, se under
     */
    private val _uiStateNy = MutableStateFlow(
        VarerUiState(
            listenavn = currentListenavn,
            sortert = false,
        )
    )
    val uiStateNy: StateFlow<VarerUiState> = _uiStateNy.asStateFlow()


    /**
     * "Konstruktør" ved hjelp av init : kode som kjøres ved oppstart (instansiering av vM)
     * Her kan man legge inn initielle kall på API og andre oppstartsrutiner
     *
     */
    // Kode som kjøres ved oppstart. Etablere Room database om denne ikke finnes, knytter til repo,
    // og setter livedata til å spørre Room DB etter handlelister
    init {
        // HENTER DATA FRA API:
        getAPIVarer()
        getAPIButikker()
        getAPIPriserPrButikk(currentButikk)

        postAPILogin("tore@mail.com", "passord")

        getAPIHandleliste(currentEpost, currentListenavn)

        // ETABLERER LOKAL DB OM DENNE IKKE FINNES
        varerDAO = AppDatabase.getRoomDb(application).varerDAO()
        repoVarer = VarerRepo(varerDAO) // initierer repo

        // SETTER ALLEVARER TIL Å MOTTA DATA FRA LOKAL DB, OPPDATERES VED ENDRINGER
        getLokaleVarer()

        // HENTER INN UNIKE LISTENAVN FRA LOKAL DB
        getAlleListenavn()

        // GAMMEL UTHENTING FRA LOKAL DB
        //    alleVarer = repoVarer.alleVarer.asLiveData() // NYTT: FLOW FRA LOKAL DB!
        //  getLokaleVarer()


        // sortering på listenavn gjøres nå i filteret -
        //  (@composable HandlelisteScreen.Listevisning()), alle varelinjer emittes fra DB.
        // Testet svært lenge med LiveData og parameter til getAlleVarer(listenavn) for sortert liste,
        // men delete skapte problemer, sannsynligvis pga observer og nullPointer.
        // Påfølgende endring av listenavn klarte da ikke å utløse rekomp som ønsket/ forventet.
        // Utfordring løst ved å sende alle varelinjer som Flow fra DB og fange med asLiveData()
        // Ulemper: (potensielt) kostbar spørring til lokal DB på alle varelinjer i alle lister
        // Fordeler: raskere respons med alle varerlinjer i memory, Compose + Room + LazyColumn fra LiveData = Flow (?)
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
    private fun getAPIPriserPrButikk(butikknavn: String) {
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
     * Funksjonen henter inn Array av priser pr vare pr butikk (++)
     * Kjøres ved oppstart
     */
    private fun postAPILogin(epost: String, passord: String) {
        val map = mapOf("epost" to epost, "passord" to passord)
        viewModelScope.launch {
            _status.value = "Prøver å hente handleliste fra API"
            try {
                _brukerAPI.value = API.retrofitService.login(map)
                _status.value = "Vellykket, handleliste fra API hentet"
            } catch (e: Exception) {
                _status.value = "Feil: ${e.message}"
            }
        }
    }


    /**
     * Funksjonen henter inn Array av priser pr vare pr butikk (++)
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
     * Funksjonen overfører varenavn fra API og insert Varer til lokal DB
     * OBS! Kostbar ved store overføringer
     * TODO: Hente inn priser fra API og legge i enhetspris
     */
    fun oppdaterListeFraApi() {
        _status.value = "Prøver å oppdatere lokal DB fra API"
        try {
            for (varenavn in _hentVarerAPI.value!!) {
                var vareApi = Varer(currentListenavn, varenavn, 0.0, 0)
                insertVare(vareApi)
            }
            _status.value = "Vellykket, lokal DB oppdatert"
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

        // TODO: Kontrollere rekkefølge og referere til indeks direkte.
        // TODO: Det hadde vært bedre med nøkkel for butikknavn fra backend JSON
        var indeksForButikkNavn = 0
        if (butikknavn.equals("Kiwi")) indeksForButikkNavn = 0
        if (butikknavn.equals("Meny")) indeksForButikkNavn = 1
        if (butikknavn.equals("Coop Obs")) indeksForButikkNavn = 2
        if (butikknavn.equals("Rema 1000")) indeksForButikkNavn = 3
        if (butikknavn.equals("Spar")) indeksForButikkNavn = 4
        if (butikknavn.equals("Coop Extra")) indeksForButikkNavn = 5

        _status.value = "Prøver å oppdatere lokal DB (enhetspris) fra API"

        try {
            for (varer in alleVarer.value!!) {
                for (priser in _priserPrButikk.value?.varer!!) {
                    if (priser.key.equals(varer.varenavn)) {
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
     * Funksjonen etablerer og bytter ut innhold i LiveData -> LazyColumn fra lokal DB
     * Bytter til varer med antall > 0
     */
    fun getSortertLokaleVarer() {
        viewModelScope.launch {
            _status.value = "Henter valgte varer fra lokal DB"
            try {
                alleVarer = varerDAO.getAlleVarer().asLiveData() // ny spørring lokal DB
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
                alleVarer = varerDAO.getAlleVarer2().asLiveData() // ny spørring lokal DB
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
     * Funksjon for å regne ut sum pr handleliste.
     * Kalles fra composables (HandlelisteScreen.HeaderVisning())
     * Rekomp ikke nødvendig, trigges av endret antall pr varelinje (LiveData)
     */
    fun sumPrHandleliste(): Double {
        var sum = 0.0
        alleVarer.value
            ?.forEach { varer ->
                if (varer.listenavn.equals(currentListenavn))
                    sum += varer.antall?.times(varer.enhetspris!!) ?: 0.0
            }
        return sum
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
        repoVarer.insert(vare)
    }






    /**
     * Funksjon for å oppdatere Varer-objekt i lokal DB (antall+-1)
     */
    fun oppdaterVare(nyAntall: Int, varenavn: String, listenavn: String) =
        viewModelScope.launch(Dispatchers.IO) {
            repoVarer.update(nyAntall, varenavn, listenavn)
        }






    /**
     * Funksjon for å oppdatere Varer-objekt pris
     * TODO: Slå sammen alle update-metoder
     */
    fun oppdaterVarePris(varenavn: String, listenavn: String, enhetspris: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            repoVarer.oppdaterPris(varenavn, listenavn, enhetspris)
        }






    /**
     * Funksjon for å oppdatere en vare uten parameter
     * TODO: IKKE I BRUK
     */
    fun oppdaterVare2(vare: Varer) = viewModelScope.launch(Dispatchers.IO) {
        repoVarer.update2(vare)
    }






    /**
     * Funksjon for å slette en vare
     */
    fun slettVare(varer: Varer) = viewModelScope.launch(Dispatchers.IO) {
        repoVarer.slettVare(varer)
    }






    /**
     * Funksjon for å slette en handleliste
     */
    fun slettHandleliste() = viewModelScope.launch(Dispatchers.IO) {
        repoVarer.slettHandleliste(currentListenavn)
    }






    /**
     * returnerer ett Varer-objekt fra lokal DB på PK listenavn+varenavn
     */
    fun getVare(listenavn: String, varenavn: String) = viewModelScope.launch(Dispatchers.IO) {
        repoVarer.getVare(varenavn)
    }






    /**
     * KUN FOR TESTING
     * Funksjon for å opprette en liste av handlelisteItems
     * Kall på varelinjer fra API gjør denne jobben automatisk
     */
    private fun manuellVareliste(): List<Varer> {
        var liste = listOf(
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
        return liste
    }


} // slutt class PrisjegerViewModel









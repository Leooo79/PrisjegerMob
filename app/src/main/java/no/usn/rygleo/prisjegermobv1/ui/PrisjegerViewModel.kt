
package no.usn.rygleo.prisjegermobv1.ui

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.usn.rygleo.prisjegermobv1.data.*
import no.usn.rygleo.prisjegermobv1.roomDB.AppDatabase
import no.usn.rygleo.prisjegermobv1.roomDB.Bruker
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import no.usn.rygleo.prisjegermobv1.roomDB.VarerDAO


/**
 * Klassen inneholder logikk for app Prisjeger
 * Kommuniserer med screens (visningskomponenter)
 * Kommuniserer med datakilder (klasser/ TODO: API/ local storage)
 */
class PrisjegerViewModel(application: Application) : AndroidViewModel(application) {

    // DEN NYE MÅTEN MED ROOM OG LIVEDATA ::::

    /**
     * Testing på LiveData og Room FOR HANDLELISTE
     */


    // Variabler/ ref til repo, LiveData for handlelister fra Room,
    // default liste(navn) som skal vises TODO: siste lagrede??
    private val repoVarer: VarerRepo
    var alleVarer: LiveData<List<Varer>> // MÅ SETTES SOM VAR MED EGEN SETTER FOR ENDRING LISTENAVN
        private set
    var currentListenavn = "RoomListe1" // VARIABEL FOR INNEVÆRENDE HANDLELISTENAVN
    val varerDAO: VarerDAO

    // Kode som kjøres ved oppstart. Etablere Room database om denne ikke finnes, knytter til repo,
    // og setter livedata til å spørre Room DB etter handlelister
    init {
        varerDAO = AppDatabase.getRoomDb(application).varerDAO()
        repoVarer = VarerRepo(varerDAO)
        alleVarer = varerDAO.getAlleVarer(currentListenavn)
    }


    // statevariabel for rekomposisjon ved nytt listenavn
    private val _uiStateNy = MutableStateFlow(
        VarerUiState(
            listenavn = currentListenavn,
        )
    )
    val uiStateNy: StateFlow<VarerUiState> = _uiStateNy.asStateFlow()

    /**
     * Funksjonen kalles fra composables for å oppdatere hvilken handleliste
     * som vises
     */
    fun setListeNavn(nyttListeNavn: String) {
        currentListenavn = nyttListeNavn
        oppdaterListenavn(nyttListeNavn) // for rekomposisjon
        alleVarer = varerDAO.getAlleVarer(currentListenavn)
    }


    /**
     * Hjelpemetode for å oppdatere state på listenavn -> rekomposisjon
     */
    private fun oppdaterListenavn(listenavn: String) {
        _uiStateNy.update { currentState ->
            currentState.copy(
                listenavn = listenavn,
            )
        }
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insertVare(vare: Varer) = viewModelScope.launch(Dispatchers.IO) {
        repoVarer.insert(vare)
    }


    /**
     * Lager en testliste og kjører insert mot lokal DB (Room)
     */
    fun lagTestliste() {
        val dummy = manuellVareliste()
        for (varer in dummy) {
            insertVare(varer)
        }
    }

    /**
     * Funksjon for å oppdatere en vare (antall)
     */
    fun oppdaterVare(nyAntall: Int, varenavn: String, listenavn: String) = viewModelScope.launch(Dispatchers.IO) {
        repoVarer.update(nyAntall, varenavn, listenavn)
    }

    /**
     * returnerer vare pr handliliste pr varenavn
     */
    fun getVare(listenavn: String, varenavn: String) = viewModelScope.launch(Dispatchers.IO) {
        repoVarer.getVare(varenavn)
    }

    /**
     * Funksjon for å opprette en liste av handlelisteItems
     * Skal erstattes av reelle data fra API
     */
    private fun manuellVareliste(): List<Varer> {
        var liste = listOf(
            Varer("RoomListe1","AGGGGGGGGGGgurk, 1 stk", 11.11, 5),
            Varer("RoomListe1","Aromat Krydder, 90 gram", 22.22, 4),
            Varer("RoomListe1","Avløpsåpner Pulver Plumbo, 600 gr", 33.33, 0),
            Varer("RoomListe1","Bakepulver Freia, 250 gram", 44.44, 0),
            Varer("RoomListe1","Fish and Crips, Findus, 480 gram", 55.55, 0),
            Varer("RoomListe1","Daim Dobbel Freia, 56 gr", 66.66, 0),
            Varer("RoomListe1","Havregryn lettkokte Axa, 1,1 kg", 77.77, 0),
            Varer("RoomListe1","Favoritt Salami, Gilde, 150 gram", 88.88, 0),
            Varer("RoomListe1","Gilde Kjøttkaker, 800 gram", 99.99, 0),
            Varer("RoomListe1","Grillpølser Gilde, 600 gr", 100.0, 0),
            Varer("RoomListe1","Kokt skinke Gilde, 110 gr", 110.11, 0),
            Varer("RoomListe1","Bretagne kylling saus, 27 gram", 239.89, 0),
            Varer("RoomListe1","Kjøttdeig billigste type, 400 gr", 999.99, 0),
            Varer("RoomListe2","AGGGGGGGGGGgurk, 1 stk", 11.11, 5),
            Varer("RoomListe2","Aromat Krydder, 90 gram", 22.22, 4),
            Varer("RoomListe2","Avløpsåpner Pulver Plumbo, 600 gr", 33.33, 0),
            Varer("RoomListe2","Bakepulver Freia, 250 gram", 44.44, 0),
            Varer("RoomListe2","Fish and Crips, Findus, 480 gram", 55.55, 0),
        )
        return liste
    }


    // ALT NEDENFOR ER GAMMELT/ EKSPERIMENTER *********************************************************************************
    // ************************************************************************************************************************



    /**
     * Testing på LiveData og Room FOR BRUKERE
     */

    val testBruker = Bruker(2,"testNavn", "testPassord")

    // Reference to repository
    private val repository: BrukerRepo
    val allUsers: LiveData<List<Bruker>>

    init {
        val brukerDAO = AppDatabase.getRoomDb(application) // Bygger databaseobjektet....
            .brukerDAO() // ... og henter DAO-objektet fra dette
        repository = BrukerRepo(brukerDAO) // Bygger Repository-objektet basert på DAO
        allUsers = repository.allUsers // Henter en liste med alle brukere fra databasen (via repository)
    }


    val sorterteBrukere: ArrayList<Int>? = null

    fun selectAllIDs() {
        allUsers.value?.forEach {
            sorterteBrukere?.add(it.brukerId)
        }
    }


    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(bruker: Bruker) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(bruker)
    }


    fun getBruker(brukerId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.getBruker(brukerId)
    }

    fun getBrukerNavn(brukerId: Int) = viewModelScope.launch(Dispatchers.IO) {
        repository.getBruker(brukerId)
    }







    // DEN GAMLE MÅTEN MED EGEN KLASSE FOR STATE ::

    // oppretter variabel for å holde på state
    private val _uiState = MutableStateFlow(
        HandlelisteUiState(
            navn = currentListenavn,
            handleliste = manuellHandleliste(),
            handlelisteData = manuellHandlelisteData(),
            sum = totalSum(manuellHandlelisteData())
            //  sum = totalSumLiveData()
        )
    )
    val uiState: StateFlow<HandlelisteUiState> = _uiState.asStateFlow()

    /**
     * Hjelpemetoder for å opprette testdata
     */
    private fun manuellHandlelisteData(): HandlelisteData {
        return HandlelisteData("testListe1", manuellHandleliste())
    }


    /**
     * Funksjon for å opprette en liste av handlelisteItems
     * Skal erstattes av reelle data fra API
     */
    private fun manuellHandleliste(): List<HandlelisteItems> {
        var liste = listOf(
            HandlelisteItems("Agurk, 1 stk", 11.11, 5),
            HandlelisteItems("Aromat Krydder, 90 gram", 22.22, 4),
            HandlelisteItems("Avløpsåpner Pulver Plumbo, 600 gr", 33.33, 0),
            HandlelisteItems("Bakepulver Freia, 250 gram", 44.44, 0),
            HandlelisteItems("Fish and Crips, Findus, 480 gram", 55.55, 0),
            HandlelisteItems("Daim Dobbel Freia, 56 gr", 66.66, 0),
            HandlelisteItems("Havregryn lettkokte Axa, 1,1 kg", 77.77, 0),
            HandlelisteItems("Favoritt Salami, Gilde, 150 gram", 88.88, 0),
            HandlelisteItems("Gilde Kjøttkaker, 800 gram", 99.99, 0),
            HandlelisteItems("Grillpølser Gilde, 600 gr", 100.0, 0),
            HandlelisteItems("Kokt skinke Gilde, 110 gr", 110.11, 0),
            HandlelisteItems("Bretagne kylling saus, 27 gram", 239.89, 0),
            HandlelisteItems("Kjøttdeig billigste type, 400 gr", 999.99, 0),
        )
        return liste
    }




    /**
     * Hjelpemetode for å regne ut total sum for hele lista
     */
    fun totalSum(handlelisteData: HandlelisteData): Double {
        var sum = 0.0
        for (varer in handlelisteData.handleliste) {
            sum += varer.antall * varer.enhetspris
        }
        return sum
    }

    /**
     * Hjelpemetode for å regne ut total sum for hele lista
     */
    fun totalSumLiveData(): Double {
        var sum = 0.0
        for (varer in alleVarer.value!!) {
            sum += varer.antall?.times(varer.enhetspris!!) ?: 0.0
        }
        return sum
    }


    // FORSØK LIVEDATA
    val livedata: MutableLiveData<HandlelisteItems> by lazy {
        MutableLiveData<HandlelisteItems>()
    }


    /**
     * Hjelpemetode for å øke antall
     * TODO: API kall avventer oppkobling mot database/ local storage
     */
    fun inkrementer(vare: HandlelisteItems) {
        vare.antall++ // dette går fordi variabel er var og ikke val.
        // her skal det gå kall til API for å oppdatere antall
        oppdaterAntall(vare)
        oppdaterSum(vare)
    }


    /**
     * Hjelpemetode for å redusere antall
     * TODO: API kall avventer oppkobling mot database/ local storage
     */
    fun dekrementer(vare: HandlelisteItems) {
        vare.antall-- // dette går fordi variabel er var og ikke val.
        // her skal det gå kall til API for å oppdatere antall
        oppdaterAntall(vare)
        oppdaterSum(vare)
    }


    /**
     * Hjelpemetode for å oppdatere totalsum for handleliste
     */
    private fun oppdaterSum(vare: HandlelisteItems) {
        _uiState.update { currentState ->
            currentState.copy(
                sum = vare.enhetspris,
            )
        }
    }


    /**
     * Hjelpemetode for å oppdatere antall pr vare
     */
    private fun oppdaterAntall(vare: HandlelisteItems) {
        _uiState.update { currentState ->
            currentState.copy(
                antall = vare.antall,
            )
        }
    }

    /**
     * Hjelpemetode for å oppdatere totalsum for handleliste
     */
    fun oppdaterSumFraLD(vare: Varer) {
        _uiState.update { currentState ->
            currentState.copy(
                sum = vare.enhetspris,
            )
        }
    }



    fun setNavn(handlelisteData: HandlelisteData, nyttNavn: String) {
        handlelisteData.navn = nyttNavn
        _uiState.update { currentState ->
            currentState.copy(
                navn = nyttNavn,
            )
        }
    }


    private fun setHandleliste(oppdatertHandleliste: List<HandlelisteItems>) {
        _uiState.update { currentState ->
            currentState.copy(
                handleliste = oppdatertHandleliste,
            )
        }
    }

}




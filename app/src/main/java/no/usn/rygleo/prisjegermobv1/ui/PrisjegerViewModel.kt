
package no.usn.rygleo.prisjegermobv1.ui

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import no.usn.rygleo.prisjegermobv1.data.HandlelisteData
import no.usn.rygleo.prisjegermobv1.data.HandlelisteItems
import no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.usn.rygleo.prisjegermobv1.MainActivity
import no.usn.rygleo.prisjegermobv1.data.BrukerRepo
import no.usn.rygleo.prisjegermobv1.roomDB.AppDatabase
import no.usn.rygleo.prisjegermobv1.roomDB.Bruker
import no.usn.rygleo.prisjegermobv1.roomDB.BrukerDAO


/**
 * Klassen inneholder logikk for app Prisjeger
 * Kommuniserer med screens (visningskomponenter)
 * Kommuniserer med datakilder (klasser/ TODO: API/ local storage)
 */
class PrisjegerViewModel(application: Application) : AndroidViewModel(application) {
    // oppretter variabel for å holde på state
    private val _uiState = MutableStateFlow(
        HandlelisteUiState(
            handleliste = manuellHandleliste(),
            handlelisteData = manuellHandlelisteData(),
            sum = totalSum(manuellHandlelisteData())
        )
    )
    val uiState: StateFlow<HandlelisteUiState> = _uiState.asStateFlow()

    /**
     * Testing på LiveData og Room
     */
    // oppretter en testbruker for insert i database
    val testBruker = Bruker(66,"testNavn", "testPassord")

    // Reference to repository
    private val repository: BrukerRepo
    // Using LiveData and caching what getAll returns has several benefits:
// - We can put an observer on the data and only update the UI when the data actually changes.
// - Repository is completely separated from the UI through the ViewModel.
    val alleBrukere: LiveData<List<Bruker>>
    
    init {
        val brukerDAO = AppDatabase.getRoomDb(application) // Bygger databaseobjektet....
            .brukerDAO() // ... og henter DAO-objektet fra dette
        repository = BrukerRepo(brukerDAO) // Bygger Repository-objektet basert på DAO
        alleBrukere = repository.allUsers // Henter en liste med alle brukere fra databasen (via repository)
    }
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(bruker: Bruker) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(bruker)
    }





    
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



    private fun setNavn(nyttNavn: String) {
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




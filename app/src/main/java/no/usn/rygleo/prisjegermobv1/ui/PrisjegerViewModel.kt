
package no.usn.rygleo.prisjegermobv1.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import no.usn.rygleo.prisjegermobv1.data.HandlelisteData
import no.usn.rygleo.prisjegermobv1.data.HandlelisteItems
import no.usn.rygleo.prisjegermobv1.data.HandlelisteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


private const val TESTNAVN = "Dette_er_test"

/**
 * Klassen inneholder logikk for app Prisjeger
 * Kommuniserer med screens (visningskomponenter)
 * Kommuniserer med datakilder (klasser/ TODO: API/ local storage)
 */
class PrisjegerViewModel : ViewModel() {
    // oppretter variabel for å holde på state
    private val _uiState = MutableStateFlow(
        HandlelisteUiState(
            navn = TESTNAVN,
            handleliste = manuellHandleliste(),
            handlelisteData = manuellHandlelisteData(),
            sum = nyTotal(manuellHandlelisteData())
        )
    )
    val uiState: StateFlow<HandlelisteUiState> = _uiState.asStateFlow()


    /**
     * Hjelpemetoder for å opprette testdata
     */
    private fun manuellHandlelisteData(): HandlelisteData {
        return HandlelisteData("testListe1", manuellHandleliste())
    }

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

    fun nyTotal(handlelisteData: HandlelisteData): Double {
        var nyTotal: Double = 0.0
        for (handlelisteItems in handlelisteData.handleliste) {
            nyTotal += handlelisteItems.sumPrVare
        }
        return nyTotal
    }

    // FORSØK LIVEDATA
    val livedata: MutableLiveData<HandlelisteItems> by lazy {
        MutableLiveData<HandlelisteItems>()
    }


    private fun manuellPrisliste(): List<Pair<String, Double>> {
        var liste = listOf(
            Pair("Agurk, 1 stk", 11.11),
            Pair("Aromat Krydder, 90 gram", 22.22),
            Pair("Avløpsåpner Pulver Plumbo, 600 gr", 33.33),
            Pair("vare 4", 44.44)
        )
        return liste
    }


    /**
     * Hjelpemetode for å redusere antall
     * TODO: API kall avventer oppkobling mot database/ local storage
     */
    fun dekrementer(vare: HandlelisteItems): Int {
        oppdaterAntall(vare)
        oppdaterSum(vare)
        return vare.antall--
    }

    private fun oppdaterSum(vare: HandlelisteItems) {
        _uiState.update { currentState ->
            currentState.copy(
                sum = vare.enhetspris,
            )
        }
    }


    /**
     * Hjelpemetode for å rekomponere ved hjelp av "dummyvariabel"
     * For testing av UI
     * TODO: endres etter oppkobling mot database/ API kall (post/req)
     */
    fun oppdaterAntall(vare: HandlelisteItems) {
        _uiState.update { currentState ->
            currentState.copy(
                antall = vare.antall,
            )
        }
    }

    /**
     * Hjelpemetode for å øke antall
     * TODO: API kall avventer oppkobling mot database/ local storage
     */
    fun inkrementer(vare: HandlelisteItems): Int {
        oppdaterAntall(vare)
        return vare.antall++
    }

    /**
     * Hjelpemetode for å regne ut sum pr vare (pr rad i liste)
     */
    fun sumPrVare(vare: HandlelisteItems): Double {
        return Math.round(vare.enhetspris * vare.antall * 100.00) / 100.0
    }



    /**
     * Hjelpemetode for å regne ut total sum for hele lista
     */
    fun totalSum(handlelisteData: HandlelisteData): Double {
        var sum: Double = 0.0
        for (varer in handlelisteData.handleliste) {
            sum += varer.antall * varer.enhetspris
        }
        return sum
    }



    fun setNavn(nyttNavn: String) {
        _uiState.update { currentState ->
            currentState.copy(
                navn = nyttNavn,
            )
        }
    }


    fun setHandleliste(oppdatertHandleliste: List<HandlelisteItems>) {
        _uiState.update { currentState ->
            currentState.copy(
                handleliste = oppdatertHandleliste,
            )
        }
    }
}




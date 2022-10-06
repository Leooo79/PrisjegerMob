package no.usn.rygleo.prisjegermobv1.data

import androidx.lifecycle.LiveData

data class HandlelisteUiState(
    val navn: String = "",
    val antall: Int = -1,
    val handleliste: List<HandlelisteItems> = emptyList(),
    val handlelisteData: HandlelisteData? = null,
    val sum: Double? = handlelisteData?.let { nyTotal(it) }
)


fun nyTotal(handlelisteData: HandlelisteData): Double {
    var nyTotal: Double = 0.0
    for (handlelisteItems in handlelisteData.handleliste) {
        nyTotal += handlelisteItems.sumPrVare
    }
    return nyTotal
}


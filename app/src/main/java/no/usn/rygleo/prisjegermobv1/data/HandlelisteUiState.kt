package no.usn.rygleo.prisjegermobv1.data


data class HandlelisteUiState(
    val navn: String = "",
    val antall: Int = 0,
    val handleliste: List<HandlelisteItems> = emptyList(),
    val handlelisteData: HandlelisteData? = null,
    val sum: Double? = handlelisteData?.let { nyTotal(it) }
)


fun nyTotal(handlelisteData: HandlelisteData): Double {
    var nyTotal = 0.0
    for (handlelisteItems in handlelisteData.handleliste) {
        nyTotal += handlelisteItems.sumPrVare
    }
    return nyTotal
}


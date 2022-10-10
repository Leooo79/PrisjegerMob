package no.usn.rygleo.prisjegermobv1.data


data class HandlelisteData(
    // val bruker : Bruker,
    val navn: String,
    val handleliste : List<HandlelisteItems>,
    val sum: Double = nyTotal(handleliste), // burde aggregert grandTotal av alle rader i handleliste
)


fun nyTotal(handleliste: List<HandlelisteItems>): Double {
    var nyTotal: Double = 0.0
    for (handlelisteItems in handleliste) {
        nyTotal += handlelisteItems.sumPrVare
    }
    return nyTotal
}


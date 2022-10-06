package no.usn.rygleo.prisjegermobv1.data

import androidx.compose.runtime.mutableStateListOf
import no.usn.rygleo.prisjegermobv1.navigasjon.BottomNavItem

val grandTotal: List<Double> = emptyList()

data class HandlelisteData(
    // val bruker : Bruker,
    val navn: String,
    val handleliste : List<HandlelisteItems>,
    val sum: Double = nyTotal(handleliste), // burde aggregert grandTotal av alle rader i handleliste
)

fun oppdaterSum(handleliste : List<HandlelisteItems>): Double {
    var nySum = 0.0
    handleliste.forEach {e -> nySum += 1}
    return nySum
}

fun nyTotal(handleliste: List<HandlelisteItems>): Double {
    var nyTotal: Double = 0.0
    for (handlelisteItems in handleliste) {
        nyTotal += handlelisteItems.sumPrVare
    }
    return nyTotal
}


package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import no.usn.rygleo.prisjegermobv1.data.HandlelisteItems

@Entity
data class HandlelisteMod(
    @PrimaryKey                         val handlelisteId: Int,
    @ColumnInfo(name = "navn")          val brukerNavn: String?,
    @ColumnInfo(name = "varer")         val varer: ArrayList<HandlelisteItems>?,
    @ColumnInfo(name = "sum")           val sum: Double? = varer?.let { sumHandleliste(it) }
)

fun sumHandleliste(handleliste: List<HandlelisteItems>): Double {
    var nyTotal: Double = 0.0
    for (handlelisteItems in handleliste) {
        nyTotal += handlelisteItems.sumPrVare
    }
    return nyTotal
}
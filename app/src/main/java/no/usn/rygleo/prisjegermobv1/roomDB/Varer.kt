package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import no.usn.rygleo.prisjegermobv1.data.HandlelisteItems

@Entity
data class Varer(
    @PrimaryKey                         val varenavn: String,
    @ColumnInfo(name = "enhetspris")    val enhetspris: Double?,
    @ColumnInfo(name = "antall")        val antall: Int?,
)


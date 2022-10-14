package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import no.usn.rygleo.prisjegermobv1.data.HandlelisteItems

@Entity(primaryKeys = ["listenavn","varenavn"]) // trenger komposittnøkkel
data class Varer(
    @ColumnInfo(name = "listenavn")     val listenavn: String,
    @ColumnInfo(name = "varenavn")      val varenavn: String,
    @ColumnInfo(name = "enhetspris")    val enhetspris: Double?,
    @ColumnInfo(name = "antall")        val antall: Int?,
)


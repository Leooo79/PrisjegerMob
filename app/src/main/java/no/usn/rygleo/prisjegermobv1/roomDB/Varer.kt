package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Kombinert modellklasse og entitet i lokal database.
 * Definerer rader i handlelister
 */
@Entity(primaryKeys = ["listenavn","varenavn"]) // trenger komposittnøkkel
data class Varer(
    @ColumnInfo(name = "listenavn")     val listenavn: String,
    @ColumnInfo(name = "varenavn")      val varenavn: String,
    @ColumnInfo(name = "antall")        val antall: Int,
)



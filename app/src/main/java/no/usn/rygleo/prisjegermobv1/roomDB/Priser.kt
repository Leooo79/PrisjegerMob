package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(primaryKeys = ["butikknavn","varenavn"]) // trenger komposittnøkkel
data class Priser(
    // @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @ColumnInfo(name = "butikknavn")     val butikknavn: String,
    @ColumnInfo(name = "varenavn")      val varenavn: String,
    @ColumnInfo(name = "enhetspris")    val enhetspris: Double,
)

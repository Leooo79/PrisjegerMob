package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bruker(
    @PrimaryKey                         val brukerId: Int,
    @ColumnInfo(name = "brukerNavn")    val brukerNavn: String?,
    @ColumnInfo(name = "passord")       val passord: String?
)

package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Bruker(
 //   @PrimaryKey(autoGenerate = true) val brukerId: Long = 0,
    @PrimaryKey
    @ColumnInfo(name = "brukerNavn")    val brukerNavn: String,
    @ColumnInfo(name = "sessionId")     val sessionId: String,
    //   @ColumnInfo(name = "passord")       val passord: String?
)

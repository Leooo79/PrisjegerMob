package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Kombinert modellklasse og entitet i lokal database.
 * Bruker lagres i Db ved innlogging, og slettes ved utlogging.
 * Bruker trenger således kun å logge inn en gang.
 */
@Entity
data class Bruker(
    @PrimaryKey
    @ColumnInfo(name = "brukerNavn")    val brukerNavn: String,
    @ColumnInfo(name = "sessionId")     val sessionId: String,
)

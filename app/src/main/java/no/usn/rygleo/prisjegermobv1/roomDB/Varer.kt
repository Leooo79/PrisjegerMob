package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import no.usn.rygleo.prisjegermobv1.data.HandlelisteItems

// bra lenke om overgang til id som PK:
// https://stackoverflow.com/questions/44364240/android-room-get-the-id-of-new-inserted-row-with-auto-generate

@Entity(primaryKeys = ["listenavn","varenavn"]) // trenger komposittnøkkel
data class Varer(
  //  @PrimaryKey(autoGenerate = true) val id: Long? = 0,
    @ColumnInfo(name = "listenavn")     val listenavn: String,
    @ColumnInfo(name = "varenavn")      val varenavn: String,
    @ColumnInfo(name = "enhetspris")    val enhetspris: Double?,
    @ColumnInfo(name = "antall")        val antall: Int?,
)


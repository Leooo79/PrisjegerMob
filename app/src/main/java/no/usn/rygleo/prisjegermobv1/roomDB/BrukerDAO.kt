package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface BrukerDAO {
    @Query("SELECT * FROM Bruker")
    fun getAll(): List<Bruker>


    @Query("SELECT * FROM Bruker WHERE brukerId IN (:alleBrukerId)")
    fun listePrId(alleBrukerId: IntArray): List<Bruker>


    @Query("SELECT * FROM Bruker WHERE brukerNavn LIKE :first AND " +
            "passord LIKE :last LIMIT 1")
    fun findByBrukerNavnOgPassord(first: String, last: String): Bruker


    @Insert
    fun insertAll(vararg brukere: Bruker)


    @Delete
    fun delete(bruker: Bruker)
}

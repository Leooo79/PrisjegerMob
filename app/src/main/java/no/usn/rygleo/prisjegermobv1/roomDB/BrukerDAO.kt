package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface BrukerDAO {
    @Query("SELECT * FROM Bruker")
    fun getAlleBrukere(): LiveData<List<Bruker>>


    @Query("SELECT * FROM Bruker")
    fun getBruker(): Bruker


    /*

      @Query("SELECT * FROM Bruker WHERE brukerId IN (:alleBrukerId)")
      fun listePrId(alleBrukerId: IntArray): List<Bruker>


      @Query("SELECT * FROM Bruker WHERE brukerId IN (:brukerId)")
      fun getBruker(brukerId: Int): Bruker


      @Query("SELECT brukerNavn FROM Bruker WHERE brukerId IN (:brukerId)")
      fun getBrukerNavn(brukerId: Int): String


      @Query("SELECT * FROM Bruker WHERE brukerNavn LIKE :first AND " +
              "passord LIKE :last LIMIT 1")
      fun findByBrukerNavnOgPassord(first: String, last: String): Bruker


       */

 //   @Insert(onConflict = OnConflictStrategy.REPLACE)
 //   fun insertAll(vararg brukere: Bruker)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bruker: Bruker)


    @Delete
    fun delete(bruker: Bruker)
}

package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface VarerDAO {

    @Query("SELECT * FROM Varer WHERE listenavn IN (:listenavn)")
    fun getAlleVarer(listenavn: String): LiveData<List<Varer>>


    @Query("SELECT varenavn FROM Varer WHERE varenavn IN (:varenavn)")
    fun getVare(varenavn: String): String


    @Query("SELECT * FROM Varer WHERE varenavn IN (:alleVarer)")
    fun listePrId(alleVarer: IntArray): LiveData<List<Varer>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg varer: Varer)

    @Query("UPDATE varer SET antall=:nyAntall WHERE varenavn = :varenavn " +
            "AND listenavn = :listenavn")
    fun update(nyAntall: Int, varenavn: String, listenavn: String)

    @Delete
    fun delete(varer: Varer)
}
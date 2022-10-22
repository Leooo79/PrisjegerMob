package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VarerDAO {

    @Query("SELECT * FROM Varer WHERE antall > 0 ORDER BY varenavn ASC")
    fun getAlleVarer(): Flow<List<Varer>>


    // sortering på listenavn gjøres i filteret (composable)
    @Query("SELECT * FROM Varer ORDER BY varenavn ASC")
    fun getAlleVarer2(): Flow<List<Varer>>


    @Query("SELECT DISTINCT listenavn FROM Varer ORDER BY listenavn ASC")
    fun getAlleListenavn(): Flow<Array<String>>


    @Query("SELECT varenavn FROM Varer WHERE varenavn IN (:varenavn)")
    fun getVare(varenavn: String): String


    @Query("SELECT * FROM Varer WHERE varenavn IN (:alleVarer)")
    fun listePrId(alleVarer: IntArray): List<Varer>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg varer: Varer)


    @Query("UPDATE varer SET antall=:nyAntall WHERE varenavn = :varenavn " +
            "AND listenavn = :listenavn")
    fun update(nyAntall: Int, varenavn: String, listenavn: String)

    @Update // trenger oppdaterte parameter, bruk likegjerne fun update
    fun update2(varer: Varer)

    @Delete
    fun delete(varer: Varer)


    @Query("DELETE FROM varer WHERE varenavn = :varenavn " +
            "AND listenavn = :listenavn")
    fun delete2(varenavn: String, listenavn: String)
}
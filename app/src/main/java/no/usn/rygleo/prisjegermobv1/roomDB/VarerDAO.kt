package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VarerDAO {

    @Query("SELECT * FROM Varer")
    fun getAlleVarer(): LiveData<List<Varer>>


    @Query("SELECT * FROM Varer WHERE varenavn IN (:alleVarer)")
    fun listePrId(alleVarer: IntArray): LiveData<List<Varer>>


    @Insert
    fun insertAll(vararg varer: Varer)


    @Delete
    fun delete(varer: Varer)
}
package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface HandlelisteDAO {

    @Query("SELECT * FROM HandlelisteMod")
    fun getAlleHandlelister(): LiveData<List<HandlelisteMod>>


    @Query("SELECT * FROM HandlelisteMod WHERE handlelisteId IN (:alleHandlelisteId)")
    fun listePrId(alleHandlelisteId: IntArray): LiveData<List<HandlelisteMod>>


    @Insert
    fun insertAll(vararg handlelisteMod: HandlelisteMod)


    @Delete
    fun delete(handlelisteMod: HandlelisteMod)
}
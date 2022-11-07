package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PriserDAO {



    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPriser(vararg pris: Priser)



    @Query("SELECT * FROM Priser WHERE butikknavn=:butikknavn AND varenavn=:varenavn")
    fun getPris(butikknavn: String, varenavn: String): Flow<List<Priser>>

}
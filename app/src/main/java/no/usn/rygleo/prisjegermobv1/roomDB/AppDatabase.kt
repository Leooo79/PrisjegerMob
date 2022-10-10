package no.usn.rygleo.prisjegermobv1.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel

@Database(entities = arrayOf(Bruker::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun brukerDAO(): BrukerDAO

    companion object{
        @Volatile
        private var instans : AppDatabase? = null

        fun getRoomDb(context: Context): AppDatabase {
            if(instans == null) {
                synchronized(this) {
                    instans = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        "brukerdata.db"
                    ).build()
                }
            }
            return instans!!
            // https://kotlinlang.org/docs/null-safety.html#the-operator
        }
    }

}

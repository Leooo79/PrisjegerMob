package no.usn.rygleo.prisjegermobv1.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import no.usn.rygleo.prisjegermobv1.ui.PrisjegerViewModel

// satt exportSchema = false - se build.gradle for info og kode for export
@Database(entities = arrayOf(Bruker::class, Varer::class), version = 18, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {   // HUSK Å ENDRE VERSJON VED NYTT SCHEMA

    abstract fun brukerDAO(): BrukerDAO
    abstract fun varerDAO(): VarerDAO

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
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instans!!
        }
    }

}

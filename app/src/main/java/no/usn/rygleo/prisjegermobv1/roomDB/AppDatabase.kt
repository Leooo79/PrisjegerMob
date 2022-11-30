package no.usn.rygleo.prisjegermobv1.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Abstrakt klasse som oppretter eget companion-objekt. Oppretter lokal SQL database med
 * wrapper Room. Kun en instans opprettes. Programmet kontrollerer for allerede opprettet instans.
 * Oppretter også entiteter.
 */
@Database(entities = [Bruker::class, Varer::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {   // HUSK Å ENDRE VERSJON VED NYTT SCHEMA

    abstract fun brukerDAO(): BrukerDAO
    abstract fun varerDAO(): VarerDAO

    companion object{
        @Volatile
        private var instans : AppDatabase? = null

        /**
         * Kontroller om instansiert, ellers ny instans
         */
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

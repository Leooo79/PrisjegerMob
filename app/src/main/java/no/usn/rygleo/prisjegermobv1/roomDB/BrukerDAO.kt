package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.lifecycle.LiveData
import androidx.room.*


/**
 * Grensesnitt mot lokal database, entitet Bruker
 * Det tillates kun en enkelt bruker i lokal DB
 * Ivaretas ved at bruker slettes ved utlogging.
 */
@Dao
interface BrukerDAO {

    /**
     * returnerer alle Brukere fra DB
     */
    @Query("SELECT * FROM Bruker")
    fun getAlleBrukere(): LiveData<List<Bruker>>

    /**
     * returnerer en bruker fra DB
     */
    @Query("SELECT * FROM Bruker")
    fun getBruker(): Bruker

    /**
     * Sletter alt innhold i entitet Bruker
     */
    @Query("DELETE FROM Bruker")
    fun slettBruker()

    /**
     * Insert av Bruker til lokal DB
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(bruker: Bruker)

    /**
     * Sletter en enkelt bruker fra lokal DB
     */
    @Delete
    fun delete(bruker: Bruker)
}

package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * Grensesnitt mot lokal database, entitet Varer.
 */
@Dao
interface VarerDAO {

    /**
     * Returnerer Flow med alle varelinjer lagret lokalt (alle lister)
     */
    @Query("SELECT * FROM Varer WHERE listenavn=:listenavn ORDER BY varenavn ASC")
    fun getAlleVarer(listenavn: String): Flow<List<Varer>>

    /**
     * Returnerer Flow med alle unike listenavn lagret lokalt
     */
    @Query("SELECT DISTINCT listenavn FROM Varer ORDER BY listenavn ASC")
    fun getAlleListenavn(): Flow<Array<String>>

    /**
     * Insert av lister med Varer. Allerede eksisterende PK ignoreres
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg varer: Varer)

    /**
     * Insert av lister med Varer. Allerede eksisterende PK erstattes
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllForce(vararg varer: Varer)

    /**
     * Setter antall til 0 for aktuell vare
     */
    @Query("UPDATE varer SET antall=0 WHERE varenavn=:varenavn " +
              "AND listenavn=:listenavn")
    fun antallTilNull(varenavn: String, listenavn: String) : Int

    /**
     * Øker antall av vare med 1. Følger logikk etablert på tjener.
     */
    @Query("UPDATE varer SET antall=antall+1 WHERE varenavn=:varenavn " +
            "AND listenavn=:listenavn")
    fun inkrementerAntall(varenavn: String, listenavn: String) : Int

    /**
     * Reduserer antall av vare med 1.Følger logikk etablert på tjener.
     */
    @Query("UPDATE varer SET antall=antall-1 WHERE varenavn=:varenavn " +
            "AND listenavn=:listenavn AND antall >= 1")
    fun dekrementerAntall(varenavn: String, listenavn: String) : Int

    /**
     * Sletting av en enkelt vare, trenger ikke parameter
     */
    @Delete
    fun slettVare(varer: Varer): Int

    /**
     * Sletting av en enkelt handleliste, alle varer
     */
    @Query("DELETE FROM varer WHERE listenavn = :listenavn")
    fun slettHandleliste(listenavn: String): Int

    /**
     * Slett alle handlelister, alle varer
     */
    @Query("DELETE FROM varer")
    fun slettAlleHandlelister()

}





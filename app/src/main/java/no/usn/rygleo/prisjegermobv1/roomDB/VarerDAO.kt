package no.usn.rygleo.prisjegermobv1.roomDB

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import no.usn.rygleo.prisjegermobv1.RestApi

@Dao
interface VarerDAO {

    /**
     * Returnerer Flow med alle varlinjer lagret lokalt (alle lister)
     * Returnerer kun varer med antall > 0
     */
    @Query("SELECT * FROM Varer WHERE antall > 0 ORDER BY varenavn ASC")
    fun getAlleValgteVarer(): Flow<List<Varer>>


    /**
     * Returnerer Flow med alle varelinjer lagret lokalt (alle lister)
     */
    @Query("SELECT * FROM Varer ORDER BY varenavn ASC")
    fun getAlleVarer(): Flow<List<Varer>>


    /**
     * Returnerer Flow med alle unike listenavn lagret lokalt
     */
    @Query("SELECT DISTINCT listenavn FROM Varer ORDER BY listenavn ASC")
    fun getAlleListenavn(): Flow<Array<String>>



    /**
     * Returnerer Flow med alle unike listenavn lagret lokalt
     */
    @Query("SELECT antall FROM Varer WHERE varenavn=:varenavn " +
            "AND listenavn=:listenavn")
    fun getVareAntall(varenavn: String, listenavn: String): Int





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
     * Oppdaterer en vare uten parameter
     */
    @Update
    fun update(varer: Varer)


    /**
     * Oppdaterer varer med data fra server
     */
    @Query("UPDATE varer SET antall=:antall WHERE varenavn=:varenavn " +
            "AND listenavn=:listenavn")
    fun updateVare(antall: Int, listenavn: String, varenavn: String)


  /* FUNGERER MULIGENS IKKE, TILFELLER AV ANTALL < 0. EMULATORTRØBBEL?!
    @Query("UPDATE varer SET antall=:nyAntall+antall WHERE varenavn=:varenavn " +
            "AND listenavn=:listenavn")
    fun oppdaterAntall(nyAntall: Int, varenavn: String, listenavn: String) : Int

   */


  @Query("UPDATE varer SET antall=0 WHERE varenavn=:varenavn " +
          "AND listenavn=:listenavn")
  fun antallTilNull(varenavn: String, listenavn: String) : Int



    /**
     * Øker antall av vare med 1. Innført egen for inkrement og dekrement
     * grunnet mulig bug med treg oppdatering av Flow->Livedata
     */
    @Query("UPDATE varer SET antall=antall+1 WHERE varenavn=:varenavn " +
            "AND listenavn=:listenavn")
    fun inkrementerAntall(varenavn: String, listenavn: String) : Int



    /**
     * Reduserer antall av vare med 1. Innført egen for inkrement og dekrement
     * grunnet mulig bug med treg oppdatering av Flow->Livedata
     */
    @Query("UPDATE varer SET antall=antall-1 WHERE varenavn=:varenavn " +
            "AND listenavn=:listenavn AND antall >= 1")
    fun dekrementerAntall(varenavn: String, listenavn: String) : Int



    /**
     * Update av enhetspris pr vare pr liste
     */
 //   @Query("UPDATE varer SET enhetspris=:enhetspris WHERE varenavn = :varenavn " +
 //           "AND listenavn = :listenavn")
 //   fun oppdaterPris(varenavn: String, listenavn: String, enhetspris: Double)




    /**
     * Sletting av en enkelt vare, trenger ikke parameter
     */
    @Delete
    fun slettVare(varer: Varer)




    /**
     * Sletting av en hel handleliste, alle varer
     */
    @Query("DELETE FROM varer WHERE listenavn = :listenavn")
    fun slettHandleliste(listenavn: String): Int
}

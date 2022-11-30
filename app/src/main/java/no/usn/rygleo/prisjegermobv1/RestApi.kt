package no.usn.rygleo.prisjegermobv1

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import no.usn.rygleo.prisjegermobv1.data.OppdatertStatus
import no.usn.rygleo.prisjegermobv1.data.PriserPrButikk
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

// URL til tjener
private const val BASE_URL ="http://prisjeger-app.duckdns.org:6969/api/"

// bygger Moshi-objekt
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// bygger Retrofit-objekt
private val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

/**
 * Grensesnitt mot rest-api på tjener.
 * Instansierer et companion-objekt som brukes som referanse i vM
 */
interface RestApi {

    /**
     * Funksjon for å hente en enkelt handleliste fra backend API
     */
    @GET("handlelister/{epost}/{tittel}")
    suspend fun getHandleliste(
        @Path("epost") epost: String,
        @Path("tittel") tittel: String
    ): Map<String, Int>

    /**
     * Funksjon for å hente array med alle handlelistenavn fra backend API
     */
    @GET("handlelister/{epost}")
    suspend fun getHandlelister(
        @Path("epost") epost: String,
    ): Array<String>

    /**
     * Funksjon for å hente nyeste priser fra backend API
     */
    @GET("siste/")
    suspend fun getPrisPrButikk(): PriserPrButikk

    /**
     * Funksjon for å hente/ oppdatere vareliste fra backend API
     */
    @GET("vareliste") //"vareliste"
    suspend fun getVareliste(): Array<String>

    /**
     * Funksjon for å hente liste med butikknavn fra backend API
     */
    @GET("butikkliste")
    suspend fun getButikkliste(): Array<String>

    /**
     * Funksjon for å sjekke om backendAPI har endret data siden siste forespørsel
     */
    @GET("sjekkoppdatert/{tidspunkt}/{epost}/{session}/{handleliste}")
    suspend fun sjekkOppdatert(
        @Path("tidspunkt") tidspunkt: String,
        @Path("epost") epost: String,
        @Path("session") sessionId: String,
        @Path("handleliste") handleliste: String
    ): OppdatertStatus

    /**
     * Funksjon for å nåværende tidspunkt fra tjener
     */
    @GET("tid")
    suspend fun hentTidspunkt(): String

    /**
     * Funksjonen for å autentisere bruker, og logge inn
     */
    @POST("login")
    suspend fun login(
        @Body map: Map<String, String>
    ): Map<String, String>

    /**
     * Funksjon for å registrere ny bruker
     */
    @POST("regist")
    suspend fun registrerBruker(
        @Body map: Map<String, String>
    ): String

    /**
     * Funksjon for å logge ut bruker
     */
    @GET("logUt")
    suspend fun loggUt()

    /**
     * Backend ØKER antall med en pr vare pr handleliste
     * Dersom handleliste ikke finnes opprettes listen
     */
    @POST("handlelister/{epost}/{tittel}/addvare/{vare}/{session}")
    suspend fun inkrementerHandleliste(
        @Path("epost") epost: String,
        @Path("tittel") tittel: String,
        @Path("vare") vare: String,
        @Path("session") sessionId: String
    )

    /**
     * Backend REDUSERER antall med en pr vare pr handleliste
     * Ved antall == 0 slettes vare fra handleliste
     */
    @POST("handlelister/{epost}/{tittel}/pop/{vare}/{session}")
    suspend fun dekrementerHandleliste(
        @Path("epost") epost: String,
        @Path("tittel") tittel: String,
        @Path("vare") vare: String,
        @Path("session") sessionId: String
    )

    /**
     * Backend sletter vare fra handleliste
     */
    @POST("handlelister/{epost}/{tittel}/delete/{vare}/{session}")
    suspend fun slettVareIListe(
        @Path("epost") epost: String,
        @Path("tittel") tittel: String,
        @Path("vare") vare: String,
        @Path("session") sessionId: String
    )

    /**
     * Backend sletter handleliste
     */
    @POST("handlelister/{epost}/{tittel}/remove/{session}")
    suspend fun slettHandleliste(
        @Path("epost") epost: String,
        @Path("tittel") tittel: String,
        @Path("session") sessionId: String
    )
}

/**
 * Companion-objekt for enkel referering. Kalles fra viewModel.
 */
object API {
    val retrofitService: RestApi by
    lazy {
        retrofit.create(RestApi::class.java) }
}

package no.usn.rygleo.prisjegermobv1

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import no.usn.rygleo.prisjegermobv1.data.PriserPrButikk
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


private const val BASE_URL ="http://prisjeger-app.duckdns.org:6969/api/"

private val
        moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val
        retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(
        MoshiConverterFactory.create(moshi))
    .build()

interface RestApi {
    // TODO: Vurdere rekkefølge og omfang på oppdatering av lokal/ sentral DB
    // TODO: Bør all aktivitet speiles?
    // TODO: Data fra server -> lokal DB -> vises i APP. if update -> lokal DB -> vises i APP + update server



    // Funksjon for å hente handlelister fra backend API
    @GET("handlelister/{epost}/{listenavn}")
    suspend fun getHandleliste(
        @Path("epost") epost: String,
        @Path("tittel") tittel: String
    ): Map<String, Int>


    // Funksjon for å hente nyeste priser fra backend API
    @GET("siste/")
    suspend fun getPrisPrButikk(): PriserPrButikk


    // Funksjon for å hente all historikk, kun for test
    @GET("historikk")
    suspend fun getAll(): Varer


    // Funksjon for å hente/ oppdatere vareliste fra backend API
    @GET("vareliste") //"vareliste"
    suspend fun getVareliste(): Array<String>


    // Funksjon for å hente liste med butikknavn fra backend API
    @GET("butikkliste")
    suspend fun getButikkliste(): Array<String>


    // Funksjonen sender inn epost og passord til server
    // og logger inn bruker (?)
    @POST("login")
    suspend fun login(
        @Body map: Map<String, String>
    ): Map<String, String>


    // Funksjonen ØKER antall med en pr vare pr handleliste
    // Dersom handleliste ikke finnes opprettes listen
    // Dersom varen ikke finnes legges den til i liste (antall = 1)
    @POST("handlelister/{epost}/{tittel}/add/{vare}")
    suspend fun inkrementerHandleliste(
        @Path("epost") epost: String,
        @Path("tittel") tittel: String,
        @Path("vare") vare: String,
    )


    // Funksjonen REDUSERER antall med en pr vare pr handleliste
    // Ved antall == 0 slettes vare fra handleliste
    @POST("handlelister/{epost}/{tittel}/pop/{vare}")
    suspend fun dekrementerHandleliste(
        @Path("epost") epost: String,
        @Path("tittel") tittel: String,
        @Path("vare") vare: String,
    )
}

object API {
    val retrofitService: RestApi by
    lazy {
        retrofit.create(RestApi::class.java) }
}
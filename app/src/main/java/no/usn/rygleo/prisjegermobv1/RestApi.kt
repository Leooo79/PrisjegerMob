package no.usn.rygleo.prisjegermobv1

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import no.usn.rygleo.prisjegermobv1.data.PriserPrButikk
import no.usn.rygleo.prisjegermobv1.data.TestAPI
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

private const val BASE_URL ="http://prisjeger-app.duckdns.org:6969/api/"
// private const val BASE_URL = "https://www.boredapi.com/api/"
//private const val BASE_URL = "http://prisjeger-app.duckdns.org:6969/api/handlelister/tore@mail.com/"


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
    // TODO funksjon for å teste API. Se URL over. Hentes inn av vM og vises i BottomNavConteneScreens.visAPI()
    // TODO Har automatisk opprettet dataklasse TestAPI som model for JSONdata fra test-API, vi må gjøre tilsvarende
    // TODO for egne JSONdata, men kan bli vanskelig uten Key-Value struktur.

    // Funksjon for å hente data fra testAPI
    @GET("activity") // se class TestAPI for values
    suspend fun getTestAPI(): TestAPI

    // Funksjon for å hente handlelister fra backend API
    @GET("handlelister/{epost}/{listenavn}")
    suspend fun getHandleliste(
        @Path("epost") epost: String,
        @Path("listenavn") listenavn: String): Map<String, Int>

    // Funksjonen sender inn epost og passord til server
    // og logger inn bruker (?)
    @POST("login")
    suspend fun login(@Body map: Map<String, String>): Map<String, String>

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

    //burde det være bruker klasse her? bruker.epost isteden for direkete epost string?
    @POST("handlelister/{tore@mail.com]/{TestMOBhandleliste}/add")
    suspend fun addHandleliste(@Body handleliste: Map<String, Int>)

}

object API {
    val retrofitService: RestApi by
    lazy {
        retrofit.create(RestApi::class.java) }
}
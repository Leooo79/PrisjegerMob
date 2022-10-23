package no.usn.rygleo.prisjegermobv1

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import no.usn.rygleo.prisjegermobv1.data.HandlelisteItems
import no.usn.rygleo.prisjegermobv1.data.PriserPrButikk
import no.usn.rygleo.prisjegermobv1.data.TestAPI
import no.usn.rygleo.prisjegermobv1.data.VarenavnAPI
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
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
    @GET("activity") // se class TestAPI for values
    suspend fun getTestAPI(): TestAPI


    @GET("Tore1") // se class TestAPI for values
    suspend fun getTore(): TestAPI

    @GET("siste/")
    suspend fun getPrisPrButikk(): PriserPrButikk


    @GET("historikk")
    suspend fun getAll(): Varer
    @GET("vareliste") //"vareliste"
    suspend fun getVareliste(): Array<String>
    @GET("butikkliste")
    suspend fun getButikkliste(): Array<String>
    @GET("handlelister/{epost}")
    suspend fun getHandlelister(): List<List<HandlelisteItems>>
    //burde det være bruker klasse her? bruker.epost isteden for direkete epost string?
    @POST("handlelister/{epost]/{tittel}/add")
    suspend fun nyHandlelisteAdd(@Body handleliste: Varer)

    @GET("butikkliste")
    fun searchVolumes(
        @Query("key") apiKey: String?
    ): Call<VarenavnAPI?>?


}

object API {
    val retrofitService: RestApi by
    lazy {
        retrofit.create(RestApi::class.java) }
}
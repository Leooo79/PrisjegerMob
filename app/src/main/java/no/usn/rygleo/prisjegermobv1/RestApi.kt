package no.usn.rygleo.prisjegermobv1

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import no.usn.rygleo.prisjegermobv1.data.Handleliste
import no.usn.rygleo.prisjegermobv1.data.HandlelisteItems
import no.usn.rygleo.prisjegermobv1.roomDB.Varer
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL ="http://prisjeger-app.duckdns.org:6969/api/"

    private val
        moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
    private val
        retrofit = Retrofit.Builder()
        .addConverterFactory(
        MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

interface RestApi {
    @GET("historikk")
    suspend fun getAll(): Varer
    @GET("vareliste")
    suspend fun getVareliste(): List<HandlelisteItems>
    @GET("butikkliste")
    suspend fun getButikkliste(): String
    @GET("handlelister/{epost}")
    suspend fun getHandlelister(): List<List<HandlelisteItems>>
    //burde det være bruker klasse her? bruker.epost isteden for direkete epost string?
    @POST("handlelister/{epost]/{tittel}/add")
    suspend fun nyHandlelisteAdd(@Body handleliste: Handleliste)


}

object API {
    val retrofitService: RestApi by
    lazy {
        retrofit.create(RestApi::class.java) }
}
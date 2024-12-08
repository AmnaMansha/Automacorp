package com.automacorp.service
import com.example.automacorp.model.RoomCommandDto
import com.example.automacorp.model.RoomDto
import com.example.automacorp.service.RoomsApiService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import okhttp3.logging.HttpLoggingInterceptor
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
const val API_USERNAME = "user"
const val API_PASSWORD = "password"
object ApiServices {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    val roomsApiService: RoomsApiService by lazy {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val client = try {


            val trustManager = object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {

                }

                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {

                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            }

            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, arrayOf(trustManager), null)

            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .sslSocketFactory(sslContext.socketFactory, trustManager)
                .hostnameVerifier { _, _ -> true }
                .addInterceptor(BasicAuthInterceptor(API_USERNAME, API_PASSWORD))
                .build()
        } catch (e: Exception) {
            throw RuntimeException("Error setting up SSL", e)
        }

        val baseUrl = "https://automacorp.devmind.cleverapps.io/api/"

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(RoomsApiService::class.java)
    }
}

class BasicAuthInterceptor(val username: String, val password: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header("Authorization", Credentials.basic(username, password))
            .build()
        return chain.proceed(request)
    }
}

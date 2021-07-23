package aslan.aslanov.videocapture.network

import aslan.aslanov.videocapture.BuildConfig
import aslan.aslanov.videocapture.local.manager.SharedPreferenceManager
import aslan.aslanov.videocapture.network.RetrofitFactory.createService
import aslan.aslanov.videocapture.network.service.AccountService
import aslan.aslanov.videocapture.network.service.UserService
import aslan.aslanov.videocapture.network.service.VideoService
import aslan.aslanov.videocapture.util.NetworkConstant.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    //Old Version
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private var retrofit: Retrofit? = null

    private val okHttpClientBuilder = OkHttpClient.Builder()

    fun requestInterceptor(tokenString: String): Interceptor {
        return Interceptor { chain ->

            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $tokenString")
                .build()
            return@Interceptor chain.proceed(newRequest)
        }
    }


    private fun okHttpClient(): OkHttpClient {
        when {
            BuildConfig.DEBUG -> {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.HEADERS
                logging.level = HttpLoggingInterceptor.Level.BODY
                okHttpClientBuilder.addInterceptor(logging)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)

            }
        }
        return okHttpClientBuilder.build()
    }

    private fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .client(okHttpClient())
                .baseUrl(BASE_URL)
                .build()
        }
        return retrofit as Retrofit
    }

    // val accountService: AccountService by lazy { getClient().create(AccountService::class.java) }

    val accountServiceTwo = createService<AccountService>(BuildConfig.DEBUG, BASE_URL)
    val videoService = createService<VideoService>(BuildConfig.DEBUG, BASE_URL)
    val userService = createService<UserService>(BuildConfig.DEBUG, BASE_URL)

}
package aslan.aslanov.videocapture.network

import aslan.aslanov.videocapture.BuildConfig
import aslan.aslanov.videocapture.network.RetrofitFactory.createService
import aslan.aslanov.videocapture.network.service.AccountService
import aslan.aslanov.videocapture.network.service.UserService
import aslan.aslanov.videocapture.network.service.VideoService
import aslan.aslanov.videocapture.util.NetworkConstant.BASE_URL
import okhttp3.Interceptor

object UserService {
    fun requestInterceptor(tokenString: String): Interceptor {
        return Interceptor { chain ->

            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $tokenString")
                .build()
            return@Interceptor chain.proceed(newRequest)
        }
    }



    val accountServiceTwo = createService<AccountService>(BuildConfig.DEBUG, BASE_URL)
    val videoService = createService<VideoService>(BuildConfig.DEBUG, BASE_URL)
    val userService = createService<UserService>(BuildConfig.DEBUG, BASE_URL)

}
package uz.gita.myapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.myapplication.data.api.AuthApi
import uz.gita.myapplication.data.source.locale.SharedPref
import uz.gita.myapplication.data.source.remote.request.PhoneRequest
import javax.inject.Singleton

private const val BASE_URL = "https://3f06-84-54-92-236.eu.ngrok.io"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @[Provides Singleton]
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)


    @[Provides Singleton]
    fun providesClient(
        loggingInterceptor: HttpLoggingInterceptor,
        pref: SharedPref,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor {
                val request = it.request()
                    .newBuilder()
                    .addHeader("token", pref.accessToken)
                    .build()
                it.proceed(request)
            }
            .authenticator(TokenAuthenticator(SharedPref.getInstance()))
            .build()


    @[Provides Singleton]
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    class TokenAuthenticator(
        private val pref: SharedPref
    ) : Authenticator {


        val loggingInterceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)

        private val retrofit = Retrofit.Builder()
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
            )
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val api = retrofit.create(AuthApi::class.java)


        override fun authenticate(route: Route?, response: Response): Request? {

            val result =
                api.refreshForEachRequest(pref.refreshToken, PhoneRequest(pref.phone)).execute()

            if (result.code() == 401 || result.body() == null) {
                pref.refreshToken = ""
                pref.accessToken = ""
                return null
            }

            val tokenData = result.body()!!.data

            pref.accessToken = tokenData!!.accessToken
            pref.refreshToken = tokenData.refreshToken

            return response.request
                .newBuilder()
                .addHeader("token", tokenData.accessToken)
                .build()

        }
    }
}
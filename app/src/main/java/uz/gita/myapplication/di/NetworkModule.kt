package uz.gita.myapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.gita.myapplication.data.source.locale.SharedPref
import javax.inject.Singleton

private const val BASE_URL = "https://1019-87-237-237-81.eu.ngrok.io"

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @[Provides Singleton]
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    @[Provides Singleton]
    fun providesClient(
        loggingInterceptor: HttpLoggingInterceptor,
        pref: SharedPref
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
            .build()


    @[Provides Singleton]
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}
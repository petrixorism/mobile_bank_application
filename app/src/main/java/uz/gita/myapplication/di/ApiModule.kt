package uz.gita.myapplication.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.gita.myapplication.data.api.AuthApi
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @[Provides Singleton]
    fun provideAuthAPI(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

}
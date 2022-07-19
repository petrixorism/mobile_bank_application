package uz.gita.myapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.myapplication.data.repository.AuthRepositoryImpl
import uz.gita.myapplication.domain.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Singleton Binds]
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

}
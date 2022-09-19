package uz.gita.myapplication.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.myapplication.data.repository.AuthRepositoryImpl
import uz.gita.myapplication.data.repository.CardRepositoryImpl
import uz.gita.myapplication.data.repository.ProfileRepositoryImpl
import uz.gita.myapplication.data.repository.TransferRepositoryImpl
import uz.gita.myapplication.domain.repository.AuthRepository
import uz.gita.myapplication.domain.repository.CardRepository
import uz.gita.myapplication.domain.repository.ProfileRepository
import uz.gita.myapplication.domain.repository.TransferRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Singleton Binds]
    fun provideAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @[Singleton Binds]
    fun provideCardRepository(impl: CardRepositoryImpl): CardRepository

    @[Singleton Binds]
    fun provideTransferRepository(impl: TransferRepositoryImpl): TransferRepository

    @[Singleton Binds]
    fun provideProfileRepository(impl: ProfileRepositoryImpl): ProfileRepository
}
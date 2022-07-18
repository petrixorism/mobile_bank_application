package uz.gita.myapplication.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import uz.gita.myapplication.data.source.locale.SharedPref
import uz.gita.myapplication.util.LocaleHelper
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @[Provides Singleton]
    fun provideSharedPreference(): SharedPref =
        SharedPref.getInstance()


    @[Provides Singleton]
    fun provideLocaleHelper(@ApplicationContext context: Context): LocaleHelper =
        LocaleHelper(context)

}
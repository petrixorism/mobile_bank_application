package uz.gita.myapplication.app

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import uz.gita.myapplication.data.source.locale.SharedPref

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        SharedPref.init(this)

    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var instance: Context
            private set
    }

}
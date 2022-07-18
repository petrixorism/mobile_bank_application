package uz.gita.myapplication.data.source.locale

import android.content.Context

class SharedPref(context: Context) {

    companion object {

        private var instance: SharedPref? = null

        fun init(context: Context) {
            instance = SharedPref(context)
        }

        fun getInstance(): SharedPref = instance!!

    }

    private val pref = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE)


    var language: String
        set(value) = pref.edit().putString("LANGUAGE", value).apply()
        get() = pref.getString("LANGUAGE", "en")!!


    var isFirstTime: Boolean
        set(value) = pref.edit().putBoolean("IS_FIRST_TIME", value).apply()
        get() = pref.getBoolean("IS_FIRST_TIME", true)


}
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

    var pin: String
        set(value) = pref.edit().putString("PIN", value).apply()
        get() = pref.getString("PIN", "")!!

    var accessToken: String
        set(value) = pref.edit().putString("ACCESS_TOKEN", value).apply()
        get() = pref.getString("ACCESS_TOKEN", "")!!

    var refreshToken: String
        set(value) = pref.edit().putString("REFRESH_TOKEN", value).apply()
        get() = pref.getString("REFRESH_TOKEN", "")!!


    var phone: String
        set(value) = pref.edit().putString("PHONE", value).apply()
        get() = pref.getString("PHONE", "")!!

    var baseUrl: String
        set(value) = pref.edit().putString("BASE_URL", value).apply()
        get() = pref.getString("BASE_URL", "https://c114-95-214-210-176.eu.ngrok.io")!!


    var isFirstTime: Boolean
        set(value) = pref.edit().putBoolean("IS_FIRST_TIME", value).apply()
        get() = pref.getBoolean("IS_FIRST_TIME", true)

    var isSkippedPin: Boolean
        set(value) = pref.edit().putBoolean("IS_SKIPPED_PIN", value).apply()
        get() = pref.getBoolean("IS_SKIPPED_PIN", true)

    fun clearPref() {
        pref.edit().putString("LANGUAGE", "en").apply()
        pref.edit().putString("PIN", "").apply()
        pref.edit().putString("ACCESS_TOKEN", "").apply()
        pref.edit().putString("REFRESH_TOKEN", "").apply()
        pref.edit().putBoolean("IS_FIRST_TIME", true).apply()
        pref.edit().putBoolean("IS_SKIPPED_PIN", true).apply()
        pref.edit().putString("PHONE", "").apply()
    }

}
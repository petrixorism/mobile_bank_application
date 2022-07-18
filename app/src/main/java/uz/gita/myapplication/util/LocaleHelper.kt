package uz.gita.myapplication.util

import android.content.Context
import android.content.ContextWrapper
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import uz.gita.myapplication.data.source.locale.SharedPref
import java.util.*


class LocaleHelper(private val context: Context) : ContextWrapper(context) {


    fun selectLang(language: String): Boolean {

        val locale = Locale(language)
        Locale.setDefault(locale)
        val resources: Resources = context.resources
        val configuration: Configuration = resources.configuration
        configuration.locale = locale
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return true
    }


}
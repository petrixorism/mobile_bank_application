package uz.gita.myapplication.util

class Check {


    fun checkURL(url: String): Boolean {
        return url.startsWith("https://") && url.length > 12
    }
}
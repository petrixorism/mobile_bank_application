package uz.gita.myapplication.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import uz.gita.myapplication.R
import uz.gita.myapplication.app.App
import uz.gita.myapplication.data.source.remote.response.CardResponse

typealias CardList = List<CardResponse>

@SuppressLint("UseCompatLoadingForDrawables")
fun Fragment.getColorDrawable(color: Int): Drawable? {

    when (color) {
        1 -> {
            return requireContext().getDrawable(R.drawable.ic_card1)
        }
        2 -> {
            return requireContext().getDrawable(R.drawable.ic_card2)
        }
        3 -> {
            return requireContext().getDrawable(R.drawable.ic_card3)
        }
        4 -> {
            return requireContext().getDrawable(R.drawable.ic_card4)
        }
        5 -> {
            return requireContext().getDrawable(R.drawable.ic_card5)
        }
        6 -> {
            return requireContext().getDrawable(R.drawable.ic_card6)
        }
        7 -> {
            return requireContext().getDrawable(R.drawable.ic_card7)
        }
        else -> {
            return requireContext().getDrawable(R.drawable.ic_card8)
        }
    }


}

fun Fragment.showToast(message: String) {
    makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun Fragment.showSnackBar(message: String) {
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()
}


fun View.changeVisibility(isVisible: Boolean) {
    visibility = if (isVisible) VISIBLE
    else GONE
}


fun isConnected(): Boolean = App.instance.isAvailableNetwork()

@SuppressLint("MissingPermission")
private fun Context.isAvailableNetwork(): Boolean {
    var result = false
    val connectivityManager =
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }

            }
        }
    }
    return result
}
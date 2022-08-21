package uz.gita.myapplication.data.source.remote.request

import com.google.gson.annotations.SerializedName

data class CardRequest(

    @SerializedName("cardName")
    val cardName: String,

    @SerializedName("pan")
    val pan: String,

    @SerializedName("exp")
    val exp: String
)

package uz.gita.myapplication.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class CardResponse(

    @SerializedName("owner")
    val owner: String,

    @SerializedName("cardName")
    val cardName: String,

    @SerializedName("balance")
    val balance: Double,

    @SerializedName("color")
    val color: Int,

    @SerializedName("id")
    val id: Int,

    @SerializedName("pan")
    val pan: String,

    @SerializedName("exp")
    val exp: String,

    @SerializedName("ignoreBalance")
    val ignoreBalance: Boolean,

    @SerializedName("status")
    val status: Int
)

package uz.gita.myapplication.data.source.remote.response

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    val message: String?
)

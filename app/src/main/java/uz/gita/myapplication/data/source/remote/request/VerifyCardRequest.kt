package uz.gita.myapplication.data.source.remote.request

data class VerifyCardRequest(
    val pan: String,
    val code: String
)
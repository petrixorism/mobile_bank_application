package uz.gita.myapplication.data.source.remote.request

data class VerifyRequest(
    val phone: String,
    val code: String
)

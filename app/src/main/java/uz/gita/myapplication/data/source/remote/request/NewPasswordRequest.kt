package uz.gita.myapplication.data.source.remote.request

data class NewPasswordRequest(
    val code: String,
    val phone: String,
    val newPassword: String
)


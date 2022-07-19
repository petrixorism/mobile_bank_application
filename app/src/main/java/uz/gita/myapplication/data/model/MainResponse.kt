package uz.gita.myapplication.data.model

data class MainResponse<T>(
    val data: T? = null,
    val message: String? = null
)

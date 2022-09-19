package uz.gita.myapplication.data.source.remote.response

data class TransferResponse(
    val amount: Double,
    val receiver: Int,
    val sender: Int,
    val fee: Double,
    val id: Int,
    val time: Long,
    val status: Int
)


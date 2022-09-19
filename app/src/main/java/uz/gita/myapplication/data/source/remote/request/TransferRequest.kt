package uz.gita.myapplication.data.source.remote.request

data class TransferRequest(
    val amount: Int,
    val receiverPan: String,
    val sender: Int
)


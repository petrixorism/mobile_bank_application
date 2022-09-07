package uz.gita.myapplication.data.source.remote.response

data class HistoryResponse(
    val pageNumber: Int,
    val data: List<HistoryItem>,
    val pageSize: Int,
    val totalCount: Int
)

data class HistoryItem(
    val receiver: Int?,
    val sender: Int?,
    val amount: Double,
    val time: Long,
    val fee: Double,
    val status: Int,
    var owner: String
)


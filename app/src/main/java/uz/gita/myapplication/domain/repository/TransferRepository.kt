package uz.gita.myapplication.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.TransferRequest
import uz.gita.myapplication.data.source.remote.response.HistoryItem
import uz.gita.myapplication.data.source.remote.response.TransferResponse

interface TransferRepository {

    fun transferMoney(transferRequest: TransferRequest): Flow<MainResult<TransferResponse>>
    suspend fun getHistory(scope: CoroutineScope): Flow<PagingData<HistoryItem>>
}
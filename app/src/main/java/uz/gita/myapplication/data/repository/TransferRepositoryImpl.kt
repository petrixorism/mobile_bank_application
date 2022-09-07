package uz.gita.myapplication.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.myapplication.data.api.CardApi
import uz.gita.myapplication.data.api.TransferApi
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.HistoryDataSource
import uz.gita.myapplication.data.source.remote.request.TransferRequest
import uz.gita.myapplication.data.source.remote.response.HistoryItem
import uz.gita.myapplication.data.source.remote.response.MessageResponse
import uz.gita.myapplication.data.source.remote.response.TransferResponse
import uz.gita.myapplication.domain.repository.TransferRepository
import javax.inject.Inject

class TransferRepositoryImpl
@Inject constructor(
    private val cardApi: CardApi,
    private val transferApi: TransferApi
) : TransferRepository {

    override fun transferMoney(transferRequest: TransferRequest): Flow<MainResult<TransferResponse>> =
        flow {
            emit(MainResult.Loading(true))
            try {
                val response = transferApi.transferMoney(transferRequest)
                if (response.isSuccessful) {
                    emit(MainResult.Success(response.body()!!.data!!))
                } else {
                    val errorResponse = Gson().fromJson(
                        response.errorBody()?.charStream(),
                        MessageResponse::class.java
                    )
                    emit(MainResult.Message(errorResponse.message!!))
                }
                emit(MainResult.Loading(false))

            } catch (e: Throwable) {
                emit(MainResult.Message(e.message.toString()))
                emit(MainResult.Loading(false))
            }

        }

    override suspend fun getHistory(scope: CoroutineScope): Flow<PagingData<HistoryItem>> =
        Pager(
            PagingConfig(10)
        ) {
            HistoryDataSource(cardApi, transferApi)
        }.flow.cachedIn(scope)

}
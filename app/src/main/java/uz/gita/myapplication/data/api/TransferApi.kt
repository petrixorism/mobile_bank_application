package uz.gita.myapplication.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import uz.gita.myapplication.data.model.MainResponse
import uz.gita.myapplication.data.source.remote.request.TransferRequest
import uz.gita.myapplication.data.source.remote.response.HistoryResponse
import uz.gita.myapplication.data.source.remote.response.TransferResponse

interface TransferApi {

    @POST("/api/v1/money-transfer/send-money")
    suspend fun transferMoney(@Body transferRequest: TransferRequest): Response<MainResponse<TransferResponse>>

    @GET("/api/v1/money-transfer/history")
    suspend fun history(
        @Query("page_number") pageNumber: Int,
        @Query("page_size") pageSize: Int
    ): Response<MainResponse<HistoryResponse>>


}
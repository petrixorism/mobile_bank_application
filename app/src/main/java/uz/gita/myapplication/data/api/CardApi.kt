package uz.gita.myapplication.data.api

import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import uz.gita.myapplication.data.model.MainResponse
import uz.gita.myapplication.data.source.remote.request.*
import uz.gita.myapplication.data.source.remote.response.CardResponse
import uz.gita.myapplication.data.source.remote.response.OwnerResponse
import uz.gita.myapplication.util.CardList


interface CardApi {

    @GET("/api/v1/card/total-sum")
    suspend fun totalSum(): Response<MainResponse<String>>

    @POST("/api/v1/card/add-card")
    suspend fun addCard(@Body cardRequest: CardRequest): Response<MainResponse<String>>

    @POST("/api/v1/card/verify")
    suspend fun verify(@Body verifyCardRequest: VerifyCardRequest): Response<MainResponse<CardResponse>>

    @POST("/api/v1/card/edit-card")
    suspend fun editCard(@Body editCardRequest: EditCardRequest): Response<MainResponse<String>>  // Karta nomi o'zgartirildi

    @POST("/api/v1/card/delete-card")
    suspend fun deleteCard(@Body deleteCardRequest: DeleteCardRequest): Response<MainResponse<String>>  // Karta o'chirildi

    @GET("/api/v1/card/all")
    suspend fun allCard(): Response<MainResponse<CardList>>

    @PUT("/api/v1/card/color")
    suspend fun color(@Body colorRequest: ColorRequest): Response<MainResponse<ColorRequest>>

    @PUT("/api/v1/card/ignore-balance")
    suspend fun ignoreBalance(@Body ignoreBalanceRequest: IgnoreBalanceRequest): Response<MainResponse<IgnoreBalanceRequest>>

    @GET("/api/v1/card/owner-by-id")
    suspend fun ownerById(@Query("cardid") cardId: String): Response<MainResponse<OwnerResponse>>

    @GET("/api/v1/card/owner-by-pan")
    suspend fun ownerByPan(@Query("pan") pan: String): Response<MainResponse<OwnerResponse>>

    @GET("/api/v1/card/owner-by-pan")
    fun ownerByPanCallback(@Query("pan") pan: String): Callback<MainResponse<OwnerResponse>>


}
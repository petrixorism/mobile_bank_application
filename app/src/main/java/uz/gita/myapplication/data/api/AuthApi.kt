package uz.gita.myapplication.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uz.gita.myapplication.data.model.MainResponse
import uz.gita.myapplication.data.source.remote.request.RegisterRequest
import uz.gita.myapplication.data.source.remote.request.VerifyRequest
import uz.gita.myapplication.data.source.remote.response.TokenResponse


interface AuthApi {

    @POST("/api/v1/auth/register")
    suspend fun register(@Body data: RegisterRequest): Response<MainResponse<Nothing>>

    @POST("/api/v1/auth/verify")
    suspend fun verify(@Body verifyRequest: VerifyRequest): Response<MainResponse<TokenResponse>>
}
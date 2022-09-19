package uz.gita.myapplication.data.api

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import uz.gita.myapplication.data.model.MainResponse
import uz.gita.myapplication.data.source.remote.request.*
import uz.gita.myapplication.data.source.remote.response.TokenResponse


interface AuthApi {

    @POST("/api/v1/auth/register")
    suspend fun register(@Body data: RegisterRequest): Response<MainResponse<Nothing>>

    @POST("/api/v1/auth/login")
    suspend fun login(@Body data: LoginRequest): Response<MainResponse<Nothing>>

    @POST("/api/v1/auth/verify")
    suspend fun verify(@Body verifyRequest: VerifyRequest): Response<MainResponse<TokenResponse>>

    @POST("/api/v1/auth/resend")
    suspend fun resend(@Body resendRequest: ResendRequest): Response<MainResponse<Nothing>>

    @POST("api/v1/auth/refresh")
    suspend fun refresh(
        @Header("refresh_token") refreshToken: String,
        @Body refreshRequest: PhoneRequest
    ): Response<MainResponse<TokenResponse>>

    // For middleware
    @POST("api/v1/auth/refresh")
    fun refreshForEachRequest(
        @Header("refresh_token") refreshToken: String,
        @Body refreshRequest: PhoneRequest
    ): Call<MainResponse<TokenResponse>>

    @POST("api/v1/auth/reset")
    suspend fun reset(@Body phoneRequest: PhoneRequest): Response<MainResponse<Nothing>>

    @POST("api/v1/auth/newpassword")
    suspend fun newPassword(@Body newPasswordRequest: NewPasswordRequest): Response<MainResponse<Nothing>>

    @POST("api/v1/auth/logout")
    suspend fun logout(@Header("token") token: String): Response<MainResponse<Nothing>>

}
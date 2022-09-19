package uz.gita.myapplication.data.api

import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*
import uz.gita.myapplication.data.model.MainResponse
import uz.gita.myapplication.data.source.remote.request.ProfileInfoRequest
import uz.gita.myapplication.data.source.remote.response.AvatarResponse
import uz.gita.myapplication.data.source.remote.response.MessageResponse
import uz.gita.myapplication.data.source.remote.response.ProfileInfoResponse

interface ProfileApi {

    @GET("/api/v1/profile/info")
    suspend fun getProfileInfo(): Response<MainResponse<ProfileInfoResponse>>

    @PUT("/api/v1/profile")
    suspend fun updateProfile(@Body data: ProfileInfoRequest): Response<MainResponse<ProfileInfoResponse>>

    @Multipart
    @POST("/api/v1/profile/avatar")
    suspend fun uploadAvatar(@Part image: MultipartBody.Part): Response<MessageResponse>

    @GET("/api/v1/profile/photo-url")
    suspend fun getAvatarUrl(): Response<AvatarResponse>


}
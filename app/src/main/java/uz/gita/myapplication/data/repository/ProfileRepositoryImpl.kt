package uz.gita.myapplication.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import uz.gita.myapplication.data.api.ProfileApi
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.ProfileInfoRequest
import uz.gita.myapplication.data.source.remote.response.MessageResponse
import uz.gita.myapplication.data.source.remote.response.ProfileInfoResponse
import uz.gita.myapplication.domain.repository.ProfileRepository
import java.io.File
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileApi: ProfileApi
) : ProfileRepository {

    override suspend fun getUserInfo(): Flow<MainResult<ProfileInfoResponse>> = flow {
        emit(MainResult.Loading(true))
        try {
            val response = profileApi.getProfileInfo()
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

    override suspend fun updateProfile(data: ProfileInfoRequest): Flow<MainResult<ProfileInfoResponse>> =
        flow {
            emit(MainResult.Loading(true))
            try {
                val response = profileApi.updateProfile(data)
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


    override suspend fun uploadAvatar(file: File): Flow<MainResult<String>> = flow {
        emit(MainResult.Loading(true))
        try {

            val body = MultipartBody.Part.createFormData(
                "avatar",
                file.name,
                file.asRequestBody("image/*".toMediaTypeOrNull())
            )
            val response = profileApi.uploadAvatar(body)

            if (response.isSuccessful) {
                emit(MainResult.Success(response.body()!!.message!!))
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

    override suspend fun getAvatar(): Flow<MainResult<String>> = flow {
        emit(MainResult.Loading(true))
        try {
            val response = profileApi.getAvatarUrl()
            if (response.isSuccessful) {
                emit(MainResult.Success(response.body()!!.url!!))
            } else {

                emit(MainResult.Message(response.body()!!.message!!))
            }
            emit(MainResult.Loading(false))

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }
    }
}
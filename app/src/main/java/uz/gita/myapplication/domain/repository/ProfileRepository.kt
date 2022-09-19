package uz.gita.myapplication.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.ProfileInfoRequest
import uz.gita.myapplication.data.source.remote.response.ProfileInfoResponse
import java.io.File

interface ProfileRepository {

    suspend fun getUserInfo(): Flow<MainResult<ProfileInfoResponse>>

    suspend fun updateProfile(data: ProfileInfoRequest): Flow<MainResult<ProfileInfoResponse>>

    suspend fun uploadAvatar(file: File): Flow<MainResult<String>>

    suspend fun getAvatar(): Flow<MainResult<String>>

}
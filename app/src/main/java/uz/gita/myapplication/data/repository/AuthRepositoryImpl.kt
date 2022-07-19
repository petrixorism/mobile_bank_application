package uz.gita.myapplication.data.repository

import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.myapplication.data.api.AuthApi
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.RegisterRequest
import uz.gita.myapplication.data.source.remote.response.MessageResponse
import uz.gita.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override fun register(registerRequest: RegisterRequest): Flow<MainResult<Unit>> = flow {

        try {
            emit(MainResult.Loading(true))
            val result = api.register(registerRequest)
            if (result.isSuccessful) {
                emit(MainResult.Success(Unit))
            } else {
                val errorResponse = Gson().fromJson(
                    result.errorBody()?.charStream(),
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
}
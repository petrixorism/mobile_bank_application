package uz.gita.myapplication.data.repository

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.gita.myapplication.data.api.AuthApi
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.locale.SharedPref
import uz.gita.myapplication.data.source.remote.request.*
import uz.gita.myapplication.data.source.remote.response.MessageResponse
import uz.gita.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val pref: SharedPref
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

    override fun login(loginRequest: LoginRequest): Flow<MainResult<Unit>> = flow {

        try {
            emit(MainResult.Loading(true))
            val result = api.login(loginRequest)
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

    override fun verify(verifyRequest: VerifyRequest): Flow<MainResult<Unit>> = flow {

        emit(MainResult.Loading(true))

        try {
            val result = api.verify(verifyRequest)
            if (result.isSuccessful) {
                result.body()?.let {
                    pref.phone = verifyRequest.phone
                    pref.accessToken = it.data!!.accessToken
                    pref.refreshToken = it.data.refreshToken
                    emit(MainResult.Success(Unit))
                }
                emit(MainResult.Loading(false))

            } else {
                result.body()?.let {
                    emit(MainResult.Message(it.message!!))
                }
                emit(MainResult.Loading(false))
            }
        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }

    }

    override fun refresh(phone: String): Flow<MainResult<Unit>> = flow {

        emit(MainResult.Loading(true))

        try {
            val result = api.refresh(pref.refreshToken, PhoneRequest(phone))
            if (result.isSuccessful) {
                result.body()?.let {
                    pref.accessToken = it.data!!.accessToken
                    pref.refreshToken = it.data.refreshToken
                    emit(MainResult.Success(Unit))
                }
                emit(MainResult.Loading(false))

            } else {
                Log.d("TAGDF", "refresh error")

                val errorResponse = Gson().fromJson(
                    result.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                Log.d("TAGDF", errorResponse.message!!)

                emit(MainResult.Message(errorResponse.message))
                emit(MainResult.Loading(false))
            }
        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }

    }

    override fun resend(resendRequest: ResendRequest): Flow<MainResult<Unit>> = flow {

        emit(MainResult.Loading(true))

        try {
            val result = api.resend(resendRequest)
            if (result.isSuccessful) {
                result.body()?.let {
                    emit(MainResult.Success(Unit))
                }
                emit(MainResult.Loading(false))

            } else {
                val errorResponse = Gson().fromJson(
                    result.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
                emit(MainResult.Loading(false))
            }
        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }

    }

    override fun reset(phoneRequest: PhoneRequest): Flow<MainResult<Unit>> = flow {
        try {
            emit(MainResult.Loading(true))
            val result = api.reset(phoneRequest)

            if (result.isSuccessful) {
                Log.d("TAGDF", "reset: repo success")
                emit(MainResult.Success(Unit))
                emit(MainResult.Loading(false))

            } else {
                val errorResponse = Gson().fromJson(
                    result.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
                emit(MainResult.Loading(false))

            }

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }

    }

    override fun newPassword(newPasswordRequest: NewPasswordRequest): Flow<MainResult<Unit>> =
        flow {
            try {
                emit(MainResult.Loading(true))
                val result = api.newPassword(newPasswordRequest)

                if (result.isSuccessful) {
                    emit(MainResult.Success(Unit))
                    emit(MainResult.Loading(false))

                } else {
                    val errorResponse = Gson().fromJson(
                        result.errorBody()?.charStream(),
                        MessageResponse::class.java
                    )
                    emit(MainResult.Message(errorResponse.message!!))
                    emit(MainResult.Loading(false))

                }

            } catch (e: Throwable) {
                emit(MainResult.Message(e.message.toString()))
                emit(MainResult.Loading(false))
            }

        }

    override fun logout(): Flow<MainResult<Unit>> = flow {
        try {
            emit(MainResult.Loading(true))
            val result = api.logout(pref.accessToken)

            if (result.isSuccessful) {
                emit(MainResult.Success(Unit))
                emit(MainResult.Loading(false))
            } else {
                val errorResponse = Gson().fromJson(
                    result.errorBody()?.charStream(),
                    MessageResponse::class.java
                )
                emit(MainResult.Message(errorResponse.message!!))
                emit(MainResult.Loading(false))

            }

        } catch (e: Throwable) {
            emit(MainResult.Message(e.message.toString()))
            emit(MainResult.Loading(false))
        }

    }
}
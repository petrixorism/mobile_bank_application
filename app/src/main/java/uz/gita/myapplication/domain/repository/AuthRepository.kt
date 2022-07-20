package uz.gita.myapplication.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.*


interface AuthRepository {

    fun register(registerRequest: RegisterRequest): Flow<MainResult<Unit>>
    fun login(loginRequest: LoginRequest): Flow<MainResult<Unit>>
    fun verify(verifyRequest: VerifyRequest): Flow<MainResult<Unit>>
    fun refresh(phone: String): Flow<MainResult<Unit>>
    fun resend(resendRequest: ResendRequest): Flow<MainResult<Unit>>
    fun reset(phoneRequest: PhoneRequest): Flow<MainResult<Unit>>
    fun newPassword(newPasswordRequest: NewPasswordRequest): Flow<MainResult<Unit>>
    fun logout(): Flow<MainResult<Unit>>

}
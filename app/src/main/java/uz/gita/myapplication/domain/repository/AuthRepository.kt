package uz.gita.myapplication.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.RegisterRequest
import uz.gita.myapplication.data.source.remote.request.VerifyRequest


interface AuthRepository {

    fun register(registerRequest: RegisterRequest): Flow<MainResult<Unit>>
    fun verify(verifyRequest: VerifyRequest): Flow<MainResult<Unit>>

}
package uz.gita.myapplication.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.RegisterRequest


interface AuthRepository {

    fun register(registerRequest: RegisterRequest): Flow<MainResult<Unit>>

}
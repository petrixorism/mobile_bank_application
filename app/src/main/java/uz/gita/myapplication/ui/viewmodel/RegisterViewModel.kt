package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.RegisterRequest
import uz.gita.myapplication.data.source.remote.request.VerifyRequest
import uz.gita.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _successChannel = Channel<String>()
    private val _verifiedChannel = Channel<Unit>()
    private val _errorChannel = Channel<String>()
    private val _errorVerifyChannel = Channel<String>()
    private val _loadingStateFlow = MutableStateFlow(false)
    private val _loadingVerifyStateFlow = MutableStateFlow(false)

    val successFlow: Flow<String> = _successChannel.receiveAsFlow()
    val verifiedFlow: Flow<Unit> = _verifiedChannel.receiveAsFlow()
    val errorFlow: Flow<String> = _errorChannel.receiveAsFlow()
    val errorVerifyFlow: Flow<String> = _errorVerifyChannel.receiveAsFlow()
    val loadingFlow: Flow<Boolean> = _loadingStateFlow.asStateFlow()
    val loadingVerifyFlow: Flow<Boolean> = _loadingVerifyStateFlow.asStateFlow()

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            repository.register(registerRequest).collect() { result ->
                when (result) {
                    is MainResult.Success -> {
                        _successChannel.send(result.data.toString())
                    }
                    is MainResult.Message -> {
                        _errorChannel.send(result.message)
                    }
                    is MainResult.Loading -> {
                        _loadingStateFlow.emit(result.isLoading)
                    }
                }
            }

        }
    }

    fun verify(verifyRequest: VerifyRequest) {
        viewModelScope.launch {
            if (verifyRequest.code.length < 6) {
                _errorChannel.send("Enter the full SMS code")
            }
            repository.verify(verifyRequest).collect { result ->
                when (result) {
                    is MainResult.Success -> {
                        _verifiedChannel.send(Unit)
                    }
                    is MainResult.Message -> {
                        _errorVerifyChannel.send(result.message)
                    }
                    is MainResult.Loading -> {
                        _loadingVerifyStateFlow.emit(result.isLoading)
                    }
                }
            }

        }

    }

}
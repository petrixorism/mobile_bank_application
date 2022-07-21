package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.LoginRequest
import uz.gita.myapplication.data.source.remote.request.ResendRequest
import uz.gita.myapplication.data.source.remote.request.VerifyRequest
import uz.gita.myapplication.domain.repository.AuthRepository
import uz.gita.myapplication.util.isConnected
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {


    private val _successLoginChannel = Channel<String>()
    private val _verifiedChannel = Channel<Unit>()
    private val _errorChannel = Channel<String>()
    private val _errorVerifyChannel = Channel<String>()
    private val _loadingStateFlow = MutableStateFlow(false)
    private val _loadingVerifyStateFlow = MutableStateFlow(false)

    val successLoginFlow: Flow<String> = _successLoginChannel.receiveAsFlow()
    val verifiedFlow: Flow<Unit> = _verifiedChannel.receiveAsFlow()
    val errorFlow: Flow<String> = _errorChannel.receiveAsFlow()
    val errorVerifyFlow: Flow<String> = _errorVerifyChannel.receiveAsFlow()
    val loadingFlow: Flow<Boolean> = _loadingStateFlow.asStateFlow()
    val loadingVerifyFlow: Flow<Boolean> = _loadingVerifyStateFlow.asStateFlow()


    private val _isConnectedChannel = Channel<Boolean>()
    val isConnectedFlow: Flow<Boolean> = _isConnectedChannel.receiveAsFlow()

    fun checkInternet() {
        viewModelScope.launch {
            delay(500L)
            _isConnectedChannel.send(isConnected())
        }
    }

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            if (isConnected()) {
                if (loginRequest.phone.length < 13) {
                    _errorChannel.send("Phone number's length must be 9")
                } else if (loginRequest.password.length < 6) {
                    _errorChannel.send("Password's length must be longer than 6")
                } else {
                    repository.login(loginRequest).collect() { result ->
                        when (result) {
                            is MainResult.Success -> {
                                _successLoginChannel.send(result.data.toString())
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

            } else {
                _isConnectedChannel.send(isConnected())
            }
        }
    }

    fun verify(verifyRequest: VerifyRequest) {
        viewModelScope.launch {
            if (!isConnected()) {
                _isConnectedChannel.send(isConnected())
            } else if (verifyRequest.code.length < 6) {
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

    fun resend(resendRequest: ResendRequest) {
        viewModelScope.launch {
            if (resendRequest.phone.length < 13) {
                _errorChannel.send("Phone number's length must be 9")
            } else if (resendRequest.password.length < 6) {
                _errorChannel.send("Password's length must be longer than 6")
            } else {
                repository.resend(resendRequest).collect() { result ->
                    when (result) {
                        is MainResult.Success -> {
                            _successLoginChannel.send(result.data.toString())
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
    }


}
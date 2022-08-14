package uz.gita.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.NewPasswordRequest
import uz.gita.myapplication.data.source.remote.request.PhoneRequest
import uz.gita.myapplication.domain.repository.AuthRepository
import uz.gita.myapplication.util.isConnected
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _resetChannel = Channel<Unit>()
    private val _newPasswordSuccessChannel = Channel<Unit>()
    private val _loadingChannel = Channel<Boolean>()
    private val _errorChannel = Channel<String>()
    private val _isConnectedChannel = Channel<Boolean>()

    val resetFlow: Flow<Unit> = _resetChannel.receiveAsFlow()
    val newPasswordSuccessFlow: Flow<Unit> = _newPasswordSuccessChannel.receiveAsFlow()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()
    val errorFlow: Flow<String> = _errorChannel.receiveAsFlow()
    val isConnectedFlow: Flow<Boolean> = _isConnectedChannel.receiveAsFlow()


    fun checkInternet() {
        viewModelScope.launch {
            delay(1000L)
            _isConnectedChannel.send(isConnected())
        }
    }

    fun setNewPassword(newPasswordRequest: NewPasswordRequest) {
        viewModelScope.launch {
            if (newPasswordRequest.phone.length < 13) {
                _errorChannel.send("Invalid phone number")
            } else if (newPasswordRequest.code.length < 6) {
                _errorChannel.send("Invalid verification code number")
            } else if (!isConnected()) {
                _isConnectedChannel.send(false)
            } else {

                repository.newPassword(newPasswordRequest).collect { result ->
                    when (result) {
                        is MainResult.Success -> {
                            _newPasswordSuccessChannel.send(Unit)
                        }
                        is MainResult.Message -> {
                            _errorChannel.send(result.message)
                        }
                        is MainResult.Loading -> {
                            _loadingChannel.send(result.isLoading)
                        }
                    }
                }

            }
        }
    }

    fun reset(phone: String) {
        viewModelScope.launch {
            if (phone.length < 13) {
                _errorChannel.send("Invalid phone number")
            } else if (!isConnected()) {
                _isConnectedChannel.send(false)
            } else {
                repository.reset(PhoneRequest(phone)).collect { result ->
                    when (result) {
                        is MainResult.Success -> {
                            Log.d("TAG", "reset: success")
                            _resetChannel.send(Unit)
                        }
                        is MainResult.Message -> {
                            _errorChannel.send(result.message)
                        }
                        is MainResult.Loading -> {
                            _loadingChannel.send(result.isLoading)
                        }
                    }
                }
            }
        }
    }

}
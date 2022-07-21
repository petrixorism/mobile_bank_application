package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
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
            delay(500L)
            _isConnectedChannel.send(isConnected())
        }
    }

    fun reset(phone:String) {
        viewModelScope.launch {
            repository.reset(PhoneRequest(phone))
        }
    }

}
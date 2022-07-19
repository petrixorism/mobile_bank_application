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
import uz.gita.myapplication.data.source.remote.request.RegisterRequest
import uz.gita.myapplication.domain.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _successChannel = Channel<String>()
    private val _errorChannel = Channel<String>()
    private val _loadingStateFlow = MutableStateFlow(false)

    val successFlow: Flow<String> = _successChannel.receiveAsFlow()
    val errorFlow: Flow<String> = _errorChannel.receiveAsFlow()
    val loadingFlow: Flow<Boolean> = _loadingStateFlow.asStateFlow()

    fun register(registerRequest: RegisterRequest) {
        viewModelScope.launch {
            repository.register(registerRequest).collect() { result ->
                when (result) {
                    is uz.gita.myapplication.data.model.MainResult.Success -> {
                        _successChannel.send(result.data)
                    }
                    is uz.gita.myapplication.data.model.MainResult.Message -> {
                        _errorChannel.send(result.message)
                    }
                    is uz.gita.myapplication.data.model.MainResult.Loading -> {
                        _loadingStateFlow.emit(result.isLoading)
                    }
                }
            }

        }
    }

}
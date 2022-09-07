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
import uz.gita.myapplication.domain.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class TransferViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {


    private val _loadingChannel = Channel<Boolean>()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()

    private val _messageChannel = Channel<String>()
    val messageFlow: Flow<String> = _messageChannel.receiveAsFlow()

    private val _ownerByPanStateFlow = MutableStateFlow("")
    val ownerByPan: Flow<String> = _ownerByPanStateFlow.asStateFlow()

    fun ownerByPan(pan: String) {
        viewModelScope.launch {

            repository.ownerByPan(pan).collect {
                when (it) {
                    is MainResult.Success -> {
                        _ownerByPanStateFlow.emit(it.data.fio)
                    }
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                }
            }

        }
    }


}
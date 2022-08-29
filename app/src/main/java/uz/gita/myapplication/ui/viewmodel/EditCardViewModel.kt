package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.ColorRequest
import uz.gita.myapplication.data.source.remote.request.EditCardRequest
import uz.gita.myapplication.domain.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class EditCardViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {

    private val _loadingChannel = Channel<Boolean>()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()

    private val _messageChannel = Channel<String>()
    val messageFlow: Flow<String> = _messageChannel.receiveAsFlow()

    private val _cardEditedChannel = Channel<String>()
    val cardEditedFlow: Flow<String> = _cardEditedChannel.receiveAsFlow()

    private val _colorChangedChannel = Channel<Int>()
    val colorChangedFlow: Flow<Int> = _colorChangedChannel.receiveAsFlow()

    fun changeColor(colorRequest: ColorRequest) {
        viewModelScope.launch {

            repository.color(colorRequest).collect {
                when (it) {
                    is MainResult.Success -> {
                        _colorChangedChannel.send(it.data.color)
                    }
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                }
            }
        }
    }

    fun editCard(editCardRequest: EditCardRequest) {
        viewModelScope.launch {

            if (editCardRequest.newName.isEmpty()) {
                _messageChannel.send("Card name is empty")
            } else if (editCardRequest.cardNumber.length < 16) {
                _messageChannel.send("Card number is incomplete")
            } else {

                repository.editCard(editCardRequest).collect {
                    when (it) {
                        is MainResult.Success -> {
                            _cardEditedChannel.send(it.data)
                        }
                        is MainResult.Message -> {
                            _messageChannel.send(it.message)
                        }
                        is MainResult.Loading -> {
                            _loadingChannel.send(it.isLoading)
                        }
                    }
                }

            }

        }
    }


}
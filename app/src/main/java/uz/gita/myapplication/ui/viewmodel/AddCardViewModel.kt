package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.CardRequest
import uz.gita.myapplication.data.source.remote.request.VerifyCardRequest
import uz.gita.myapplication.domain.repository.CardRepository
import javax.inject.Inject

@HiltViewModel
class AddCardViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {

    private val _loadingChannel = Channel<Boolean>()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()

    private val _messageChannel = Channel<String>()
    val messageFlow: Flow<String> = _messageChannel.receiveAsFlow()

    private val _cardAddedChannel = Channel<Unit>()
    val cardAddedFlow: Flow<Unit> = _cardAddedChannel.receiveAsFlow()

    private val _verifiedChannel = Channel<String>()
    val verifiedFlow: Flow<String> = _verifiedChannel.receiveAsFlow()

    fun addCard(cardRequest: CardRequest) {
        viewModelScope.launch {

            if (cardRequest.cardName.isEmpty() || cardRequest.cardName.isBlank()) {
                _messageChannel.send("Card name is empty")
            } else if (cardRequest.pan.length < 16) {
                _messageChannel.send("Card number is incomplete")
            } else if (cardRequest.exp.length < 5) {
                _messageChannel.send("Card's expiration date is incomplete")
            } else {


                repository.addCard(cardRequest).collect {
                    when (it) {
                        is MainResult.Success -> {
                            _cardAddedChannel.send(Unit)
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

    fun verify(verifyCardRequest: VerifyCardRequest) {
        viewModelScope.launch {

            repository.verify(verifyCardRequest).collect {
                when (it) {
                    is MainResult.Success -> {
                        _verifiedChannel.send("Card added")
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
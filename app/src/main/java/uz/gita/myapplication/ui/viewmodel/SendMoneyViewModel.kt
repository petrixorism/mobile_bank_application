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
import uz.gita.myapplication.data.source.remote.request.TransferRequest
import uz.gita.myapplication.data.source.remote.response.TransferResponse
import uz.gita.myapplication.domain.repository.CardRepository
import uz.gita.myapplication.domain.repository.TransferRepository
import uz.gita.myapplication.util.CardList
import javax.inject.Inject

@HiltViewModel
class SendMoneyViewModel @Inject constructor(
    private val repository: TransferRepository,
    private val cardRepository: CardRepository
) : ViewModel() {


    private val _loadingChannel = Channel<Boolean>()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()

    private val _messageChannel = Channel<String>()
    val messageFlow: Flow<String> = _messageChannel.receiveAsFlow()

    private val _sendMoneyChannel = Channel<TransferResponse>()
    val sendMoneySuccessFlow: Flow<TransferResponse> = _sendMoneyChannel.receiveAsFlow()

    private val _ownerByIdChannel = Channel<String>()
    val ownerByIdFlow: Flow<String> = _ownerByIdChannel.receiveAsFlow()

    private val _allCardsStateFlow = MutableStateFlow<CardList>(emptyList())
    val allCardsFlow: Flow<CardList> = _allCardsStateFlow.asStateFlow()


    fun allCard() {
        viewModelScope.launch {
            cardRepository.allCards().collect {
                when (it) {
                    is MainResult.Success -> {
                        _allCardsStateFlow.emit(it.data)
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


    fun sendMoney(transferRequest: TransferRequest) {
        viewModelScope.launch {
            repository.transferMoney(transferRequest).collect {
                when (it) {
                    is MainResult.Success -> {
                        _sendMoneyChannel.send(it.data)
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

    fun ownerById(id: String) {
        viewModelScope.launch {
            cardRepository.ownerById(id).collect {
                when (it) {
                    is MainResult.Success -> {
                        _ownerByIdChannel.send(it.data.fio)
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
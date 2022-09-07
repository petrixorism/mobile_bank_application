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
import uz.gita.myapplication.data.source.remote.request.DeleteCardRequest
import uz.gita.myapplication.data.source.remote.request.IgnoreBalanceRequest
import uz.gita.myapplication.domain.repository.CardRepository
import uz.gita.myapplication.util.CardList
import javax.inject.Inject

@HiltViewModel
class CardsViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {

    private val _loadingChannel = Channel<Boolean>()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()

    private val _messageChannel = Channel<String>()
    val messageFlow: Flow<String> = _messageChannel.receiveAsFlow()

    private val _deletedChannel = Channel<String>()
    val deletedCardFlow: Flow<String> = _deletedChannel.receiveAsFlow()

    private val _ignoreBalanceChannel = Channel<Unit>()
    val ignoreBalanceFlow: Flow<Unit> = _ignoreBalanceChannel.receiveAsFlow()

    private val _allCardsStateFlow = MutableStateFlow<CardList>(emptyList())
    val allCardsFlow: Flow<CardList> = _allCardsStateFlow.asStateFlow()


    fun allCard() {
        viewModelScope.launch {
            repository.allCards().collect {
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

    fun deleteCard(pan: String) {
        viewModelScope.launch {
            repository.deleteCard(DeleteCardRequest(pan)).collect {
                when (it) {
                    is MainResult.Success -> {
                        _deletedChannel.send(it.data)
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

    fun ignoreBalanceCard(ignoreBalanceRequest: IgnoreBalanceRequest) {
        viewModelScope.launch {
            repository.ignoreBalance(ignoreBalanceRequest).collect {
                when (it) {
                    is MainResult.Success -> {
                        _ignoreBalanceChannel.send(Unit)
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
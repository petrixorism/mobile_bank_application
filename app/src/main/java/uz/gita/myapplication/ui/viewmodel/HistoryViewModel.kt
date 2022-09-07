package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.source.remote.response.HistoryItem
import uz.gita.myapplication.domain.repository.TransferRepository
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: TransferRepository
) : ViewModel() {


    private val _loadingChannel = Channel<Boolean>()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()

    private val _messageChannel = Channel<String>()
    val messageFlow: Flow<String> = _messageChannel.receiveAsFlow()

    private val _historyListChannel = Channel<PagingData<HistoryItem>>()
    val historyFlow: Flow<PagingData<HistoryItem>> = _historyListChannel.receiveAsFlow()

    init {

        viewModelScope.launch {
            delay(1000L)
            _loadingChannel.send(true)
            repository.getHistory(this).collect {
                _historyListChannel.send(it)
                _loadingChannel.send(false)
            }


        }
    }

}



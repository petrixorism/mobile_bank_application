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
import uz.gita.myapplication.data.source.remote.response.ProfileInfoResponse
import uz.gita.myapplication.domain.repository.CardRepository
import uz.gita.myapplication.domain.repository.ProfileRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: CardRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _totalSumStateFlow = MutableStateFlow("0.0")
    val totalSumFlow: Flow<String> = _totalSumStateFlow.asStateFlow()

    private val _loadingChannel = Channel<Boolean>()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()

    private val _messageChannel = Channel<String>()
    val messageFlow: Flow<String> = _messageChannel.receiveAsFlow()

    private val _avatarUrlStateFlow = MutableStateFlow("")
    val avatarUrlFlow: Flow<String> = _avatarUrlStateFlow.asStateFlow()

    private val _infoChannel = Channel<ProfileInfoResponse>()
    val infoFlow: Flow<ProfileInfoResponse> = _infoChannel.receiveAsFlow()


    fun getAvatar() {
        viewModelScope.launch {
            profileRepository.getAvatar().collect {
                when (it) {
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                    is MainResult.Success -> {
                        _avatarUrlStateFlow.emit(it.data)
                    }
                }
            }
        }
    }

    init {

        viewModelScope.launch {
            repository.totalSum().collect {
                when (it) {
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                    is MainResult.Success -> {
                        _totalSumStateFlow.emit(it.data)
                    }
                }
            }
        }

    }


    fun getProfileInfo() {
        viewModelScope.launch {
            profileRepository.getUserInfo().collect {
                when (it) {
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                    is MainResult.Success -> {
                        _infoChannel.send(it.data)
                    }
                }
            }
        }
    }


}
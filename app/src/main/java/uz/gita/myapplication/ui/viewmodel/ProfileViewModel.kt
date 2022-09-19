package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.remote.request.ProfileInfoRequest
import uz.gita.myapplication.data.source.remote.response.ProfileInfoResponse
import uz.gita.myapplication.domain.repository.AuthRepository
import uz.gita.myapplication.domain.repository.ProfileRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loadingChannel = Channel<Boolean>()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()

    private val _messageChannel = Channel<String>()
    val messageFlow: Flow<String> = _messageChannel.receiveAsFlow()

    private val _avatarUrl = Channel<String>()
    val avatarUrlFlow: Flow<String> = _avatarUrl.receiveAsFlow()

    private val _infoChannel = Channel<ProfileInfoResponse>()
    val infoFlow: Flow<ProfileInfoResponse> = _infoChannel.receiveAsFlow()

    private val _updateChannel = Channel<ProfileInfoResponse>()
    val updateFlow: Flow<ProfileInfoResponse> = _updateChannel.receiveAsFlow()

    private val _successAvatarImageChannel = Channel<String>()
    val successAvatarFlow: Flow<String> = _successAvatarImageChannel.receiveAsFlow()

    private val _logoutChannel = Channel<Unit>()
    val logoutFlow: Flow<Unit> = _logoutChannel.receiveAsFlow()

    fun getAvatar() {
        viewModelScope.launch {
            repository.getAvatar().collect {
                when (it) {
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                    is MainResult.Success -> {
                        _avatarUrl.send(it.data)
                    }
                }
            }
        }
    }

    fun getProfileInfo() {
        viewModelScope.launch {
            repository.getUserInfo().collect {
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

    fun updateProfile(profileInfoRequest: ProfileInfoRequest) {
        viewModelScope.launch {
            repository.updateProfile(profileInfoRequest).collect {
                when (it) {
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                    is MainResult.Success -> {
                        _updateChannel.send(it.data)
                    }
                }
            }
        }
    }

    fun uploadAvatar(file: File) {
        viewModelScope.launch {
            repository.uploadAvatar(file).collect {
                when (it) {
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                    is MainResult.Success -> {
                        _successAvatarImageChannel.send(it.data)
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout().collect {
                when (it) {
                    is MainResult.Message -> {
                        _messageChannel.send(it.message)
                    }
                    is MainResult.Loading -> {
                        _loadingChannel.send(it.isLoading)
                    }
                    is MainResult.Success -> {
                        _logoutChannel.send(Unit)
                    }
                }
            }
        }
    }

}
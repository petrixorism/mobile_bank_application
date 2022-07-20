package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.model.MainResult
import uz.gita.myapplication.data.source.locale.SharedPref
import uz.gita.myapplication.domain.repository.AuthRepository
import uz.gita.myapplication.util.isConnected
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val pref: SharedPref,
    private val repository: AuthRepository
) : ViewModel() {

    private val _selectLanguageChannel = Channel<Unit>()
    private val _loginChannel = Channel<Unit>()
    private val _loadingChannel = Channel<Boolean>()
    private val _mainScreenChannel = Channel<Unit>()
    private val _checkPinChannel = Channel<Unit>()
    private val _languageChannel = Channel<String>()
    private val _noConnectionChannel = Channel<Unit>()

    val noConnectionFlow: Flow<Unit> = _noConnectionChannel.receiveAsFlow()
    val mainScreenFlow: Flow<Unit> = _mainScreenChannel.receiveAsFlow()
    val checkPinFlow: Flow<Unit> = _checkPinChannel.receiveAsFlow()
    val selectLanguageFlow: Flow<Unit> = _selectLanguageChannel.receiveAsFlow()
    val loginFlow: Flow<Unit> = _loginChannel.receiveAsFlow()
    val loadingFlow: Flow<Boolean> = _loadingChannel.receiveAsFlow()
    val languageFlow: Flow<String> = _languageChannel.receiveAsFlow()


    fun check() {
        viewModelScope.launch {
            delay(2000L)
            if (!isConnected()) {
                _noConnectionChannel.send(Unit)
            } else {
                if (pref.isFirstTime) {
                    _selectLanguageChannel.send(Unit)
                } else {
                    repository.refresh(pref.phone).collect() { result ->
                        when (result) {
                            is MainResult.Success -> {
                                if (pref.isSkippedPin) {
                                    _mainScreenChannel.send(Unit)
                                } else {
                                    _checkPinChannel.send(Unit)
                                }
                            }
                            is MainResult.Message -> {
                                _loginChannel.send(Unit)
                            }
                            is MainResult.Loading -> {
                                _loadingChannel.send(result.isLoading)
                            }
                        }
                    }


                }
            }
        }
    }

    fun languageCheck() {
        viewModelScope.launch {
            _languageChannel.send(pref.language)
        }
    }


}
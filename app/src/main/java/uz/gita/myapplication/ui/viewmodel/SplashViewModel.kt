package uz.gita.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.source.locale.SharedPref
import uz.gita.myapplication.util.isConnected
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val pref: SharedPref
) : ViewModel() {

    private val _isFirstTimeStateFlow = Channel<Unit>()
    private val _splashChannel = Channel<Unit>()
    private val _languageStateFlow = Channel<String>()
    private val _noConnectionChannel = Channel<Unit>()

    val noConnectionFlow: Flow<Unit> = _noConnectionChannel.receiveAsFlow()
    val splashFlow = _splashChannel.receiveAsFlow()
    val languageFlow = _languageStateFlow.receiveAsFlow()
    val isFirstTimeFlow: Flow<Unit> = _isFirstTimeStateFlow.receiveAsFlow()

    fun check() {
        viewModelScope.launch {
            delay(2000L)
            if (!isConnected()) {
                _noConnectionChannel.send(Unit)
            } else {
                if (pref.isFirstTime) {
                    _isFirstTimeStateFlow.send(Unit)
                } else {
                    _splashChannel.send(Unit)
                }
            }
        }
    }



}
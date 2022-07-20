package uz.gita.myapplication.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.source.locale.SharedPref
import uz.gita.myapplication.util.isConnected
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val pref: SharedPref
) : ViewModel() {

    private val _startChannel = Channel<Unit>()
    val startFlow = _startChannel.receiveAsFlow()


    private val _connectionStateFlow = MutableStateFlow(isConnected())
    val connectionFlow = _connectionStateFlow.asStateFlow()


    fun start() {
        viewModelScope.launch {

            if (isConnected()) {
                pref.isFirstTime = false
                _startChannel.send(Unit)
            } else {
                _connectionStateFlow.emit(false)
            }
        }


    }

}
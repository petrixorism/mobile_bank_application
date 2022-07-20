package uz.gita.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.source.locale.SharedPref
import javax.inject.Inject

@HiltViewModel
class SetPinViewModel @Inject constructor(
    private val pref: SharedPref
) : ViewModel() {

    private val _errorChannel = Channel<Unit>()
    private val _successChannel = Channel<Unit>()

    val errorFlow: Flow<Unit> = _errorChannel.receiveAsFlow()
    val successFlow: Flow<Unit> = _successChannel.receiveAsFlow()

    fun skipPin() {
        viewModelScope.launch{
            pref.isSkippedPin = true
            _successChannel.send(Unit)
        }

    }

    fun setPin(pin: String) {
        viewModelScope.launch {
            if (pin.length == 4) {
                pref.pin = pin
                pref.isSkippedPin = false
                _successChannel.send(Unit)
            } else {
                _errorChannel.send(Unit)
            }
        }
    }

}
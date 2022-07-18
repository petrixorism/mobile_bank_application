package uz.gita.myapplication.ui.viewmodel

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import uz.gita.myapplication.data.source.locale.SharedPref
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class SelectLanguageViewModel @Inject constructor(private val pref: SharedPref) :
    ViewModel() {


    private val _languageChannel = Channel<String>()
    val languageFlow = _languageChannel.receiveAsFlow()

    private val _startChannel = Channel<Boolean>()
    val startFlow: Flow<Boolean> = _startChannel.receiveAsFlow()

    private val _languageCheckStateFlow = MutableStateFlow<Int>(0)
    val languageCheckFlow = _languageCheckStateFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            when (pref.language) {
                "uz" -> _languageCheckStateFlow.value = 0
                "en" -> _languageCheckStateFlow.value = 2
                "ru" -> _languageCheckStateFlow.value = 4
            }
        }
    }

    fun changeLanguage(id: Int) {
        viewModelScope.launch {
            when (id) {
                0 -> {
                    pref.language = "uz"
                    _languageCheckStateFlow.emit(0)
                    _languageChannel.send("uz")
                }
                2 -> {
                    pref.language = "en"
                    _languageCheckStateFlow.emit(2)
                    _languageChannel.send("en")
                }
                4 -> {
                    pref.language = "ru"
                    _languageCheckStateFlow.emit(4)
                    _languageChannel.send("ru")
                }
            }
        }

    }

    fun start() {
        viewModelScope.launch {
            if (pref.isFirstTime) {
                _startChannel.send(true)
                pref.isFirstTime = false
            } else {
                _startChannel.send(false)
            }
        }

    }

}
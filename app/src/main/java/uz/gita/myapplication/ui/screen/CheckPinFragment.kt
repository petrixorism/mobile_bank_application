package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentCheckPinBinding
import uz.gita.myapplication.ui.viewmodel.CheckPinViewModel
import uz.gita.myapplication.util.animation.Animator
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class CheckPinFragment : Fragment(R.layout.fragment_check_pin) {

    private val viewModel: CheckPinViewModel by viewModels()

    private val binding by viewBinding(FragmentCheckPinBinding::bind)
    private var pinCode = ""
    private val animator by lazy { Animator() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            viewModel.errorFlow.collect {
                showToast(getString(R.string.pin_length_error))
                getPins().forEach {
                    animator.shake(it)
                }
            }
        }
        lifecycleScope.launch {
            viewModel.successFlow.collect() {
                findNavController().navigate(CheckPinFragmentDirections.actionCheckPinFragmentToMainFragment())
            }
        }


        binding.backBtn2.setOnClickListener {
            requireActivity().onBackPressed()
        }

        getDigits().forEach { button ->
            button.setOnClickListener {
                val pin = getPins().filter { pin ->
                    pin.text.toString().isBlank()
                }
                if (pin.isNotEmpty() && pinCode.length < 4) {
                    pin.first().text = "*"
                    pinCode += button.text
                }
            }
        }
        binding.backspaceBtn.setOnClickListener {
            val pins = getPins().filter { pin ->
                pin.text.toString().isNotBlank()
            }
            if (pins.isNotEmpty() && pinCode.isNotEmpty()) {
                pins.last().text = ""
                pinCode = pinCode.removeRange(
                    startIndex = pinCode.length - 1,
                    endIndex = pinCode.length
                )
            }
        }
        binding.saveBtn.clicks()
            .onEach {
                viewModel.check(pinCode.trim())
            }
            .debounce(2000L)
            .launchIn(lifecycleScope)


    }


    //----- Functions -----//

    private fun getDigits(): List<Button> {
        val buttons = ArrayList<Button>()
        for (i in 0..10) {
            val childButton = (binding.pinContainer[i] as Button)
            if (childButton.text.toString().isNotBlank()) buttons.add(childButton)
        }
        return buttons
    }

    private fun getPins(): List<TextView> = ArrayList<TextView>().apply {
        add(binding.pin1)
        add(binding.pin2)
        add(binding.pin3)
        add(binding.pin4)
    }


}
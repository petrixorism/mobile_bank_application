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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.launch
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentSetPinBinding
import uz.gita.myapplication.ui.viewmodel.SetPinViewModel
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class SetPinFragment : Fragment(R.layout.fragment_set_pin) {

    private val binding by viewBinding(FragmentSetPinBinding::bind)
    private val viewModel: SetPinViewModel by viewModels()
    private var pinCode = ""

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            viewModel.errorFlow.collect {
                showToast(getString(R.string.pin_length_error))
            }
        }
        lifecycleScope.launch {
            viewModel.successFlow.collect() {
                findNavController().navigate(SetPinFragmentDirections.actionPinFragmentToMainFragment())
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
        binding.skipTv.setOnClickListener {
            viewModel.skipPin()
        }
        binding.saveBtn.setOnClickListener {
            viewModel.setPin(pinCode.trim())
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
package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentTransferBinding
import uz.gita.myapplication.ui.viewmodel.TransferViewModel
import uz.gita.myapplication.util.CreditCardFormatWatcher
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class TransferFragment : Fragment(R.layout.fragment_transfer) {

    private val binding by viewBinding(FragmentTransferBinding::bind)
    private val viewModel: TransferViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.confirmBtn.isEnabled = false
        binding.confirmBtn.isClickable = false

        lifecycleScope.launchWhenCreated {
            viewModel.loadingFlow.collect {
                if (it) {
                    binding.cardOwner.text = getString(R.string.checking)
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.messageFlow.collect {
                showToast(it)
                binding.confirmBtn.isEnabled = false
                binding.confirmBtn.isClickable = false
            }
        }

        lifecycleScope.launch {
            viewModel.ownerByPan.collect{
                binding.cardOwner.text = it
                binding.confirmBtn.isEnabled = true
                binding.confirmBtn.isClickable = true
            }
        }

        binding.recieverPan.addTextChangedListener(CreditCardFormatWatcher())

        binding.recieverPan.addTextChangedListener { it ->
            val pan = it?.filter {
                it.isDigit()
            }
            if (pan != null) {
                if (pan.length == 16) {
                    viewModel.ownerByPan(pan.toString())
                } else {
                    binding.confirmBtn.isEnabled = false
                    binding.confirmBtn.isClickable = false
                }
            }
        }

        binding.confirmBtn.setOnClickListener {
            if (binding.totalSum.text.toString().isNotEmpty()) {
                findNavController().navigate(
                    TransferFragmentDirections.actionPreTransferFragmentToTransferFragment2(
                        binding.totalSum.text.toString().filter { it.isDigit() }.toInt(),
                        binding.recieverPan.text.toString().filter { it.isDigit() },
                        binding.cardOwner.text.toString()
                    )
                )
            } else {
                showToast("Enter the amount of money")
            }

        }

        binding.button5.setOnClickListener {
            binding.totalSum.setText("5000")
        }
        binding.button10.setOnClickListener {
            binding.totalSum.setText("10000")
        }
        binding.button50.setOnClickListener {
            binding.totalSum.setText("50000")
        }
        binding.button100.setOnClickListener {
            binding.totalSum.setText("100000")
        }

        binding.cancelTransfer.setOnClickListener {
            binding.recieverPan.setText("")
            binding.totalSum.setText("")
        }


    }

}
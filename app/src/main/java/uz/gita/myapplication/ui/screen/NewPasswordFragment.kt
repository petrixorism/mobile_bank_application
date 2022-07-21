package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import android.view.View.VISIBLE
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentNewPasswordBinding
import uz.gita.myapplication.ui.viewmodel.NewPasswordViewModel
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class NewPasswordFragment : Fragment(R.layout.fragment_new_password) {

    private val binding by viewBinding(FragmentNewPasswordBinding::bind)
    private val viewModel: NewPasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            viewModel.isConnectedFlow.collect() {
                if (!it) findNavController().navigate(NewPasswordFragmentDirections.actionGlobalNoInternetFragment())
            }
        }
        lifecycleScope.launch {
            viewModel.newPasswordSuccessFlow.collect() {
                requireActivity().onBackPressed()
            }
        }
        lifecycleScope.launch {
            viewModel.errorFlow.collect() {
                showToast(it)
            }
        }
        lifecycleScope.launch {
            viewModel.loadingFlow.collect() {
                binding.progressCircular.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewModel.resetFlow.collect() {
                binding.passwordTet.visibility = VISIBLE
                binding.smsCodeEt.visibility = VISIBLE
                binding.resetBtn.text = "Set new password"
            }
        }

        binding.resetBtn.clicks()
            .onEach {
                viewModel.reset()
            }
            .debounce(2000L)
            .launchIn(lifecycleScope)
    }

    override fun onStart() {
        super.onStart()
        viewModel.checkInternet()
    }
}
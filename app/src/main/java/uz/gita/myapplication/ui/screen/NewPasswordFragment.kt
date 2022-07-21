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
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.request.NewPasswordRequest
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
                showToast(getString(R.string.password_updated))
                requireActivity().onBackPressed()
            }
        }

        lifecycleScope.launch {
            viewModel.errorFlow.collect() {
                showToast(it)
                binding.passwordTil.isVisible = false
                binding.smsCodeTil.isVisible = false
                binding.resetBtn.isVisible = true
                binding.setPasswordBtn.isVisible = false
            }
        }

        lifecycleScope.launch {
            viewModel.loadingFlow.collect() {
                binding.progressCircular.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewModel.resetFlow.collect() {
                binding.passwordTil.visibility = VISIBLE
                binding.smsCodeTil.visibility = VISIBLE
                binding.resetBtn.isVisible = false
                binding.setPasswordBtn.isVisible = true
            }
        }

        binding.resetBtn.clicks()
            .onEach {
                viewModel.reset("+998${binding.phoneNumberEt.text.toString()}")
            }
            .debounce(3000L)
            .launchIn(lifecycleScope)

        binding.setPasswordBtn.clicks()
            .onEach {
                viewModel.setNewPassword(
                    NewPasswordRequest(
                        binding.smsCodeEt.text.toString(),
                        "+998${binding.phoneNumberEt.text.toString()}",
                        binding.passwordEt.text.toString()
                    )
                )
            }
            .debounce(3000L)
            .launchIn(lifecycleScope)


    }

    override fun onStart() {
        super.onStart()
        viewModel.checkInternet()
    }
}
package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentSplashBinding
import uz.gita.myapplication.ui.viewmodel.SplashViewModel
import uz.gita.myapplication.util.LocaleHelper
import uz.gita.myapplication.util.animation.Animator
import uz.gita.myapplication.util.isConnected

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val viewModel: SplashViewModel by viewModels()
    private val localeHelper by lazy { LocaleHelper(requireContext()) }
    private val animator by lazy { Animator() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

isConnected()
        lifecycleScope.launch {
            viewModel.loginFlow.collect {
                viewModel.languageCheck()
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToLoginFragment())
            }
        }

        lifecycleScope.launch {
            viewModel.loadingFlow.collect {
                binding.loadingPb.isVisible = it
            }
        }

        lifecycleScope.launch {
            viewModel.selectLanguageFlow.collect {
                viewModel.languageCheck()
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToFragmentSelectLang())
            }
        }

        lifecycleScope.launch {
            viewModel.checkPinFlow.collect {
                viewModel.languageCheck()
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToCheckPinFragment())
            }
        }

        lifecycleScope.launch {
            viewModel.noConnectionFlow.collect() {
                viewModel.languageCheck()
                findNavController().navigate(R.id.action_global_noInternetFragment)
            }
        }
        lifecycleScope.launch {
            viewModel.languageFlow.collect() {
                localeHelper.selectLang(it)
                requireActivity().recreate()
            }
        }

        lifecycleScope.launch {
            viewModel.mainScreenFlow.collect() {
                viewModel.languageCheck()
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
            }
        }


        animator.fadeOut(binding.logo)
    }

    override fun onStart() {
        super.onStart()
        viewModel.check()
    }

}
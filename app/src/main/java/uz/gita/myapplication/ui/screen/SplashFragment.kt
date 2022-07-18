package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
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

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private val binding by viewBinding(FragmentSplashBinding::bind)
    private val viewModel: SplashViewModel by viewModels()
    private val localeHelper by lazy { LocaleHelper(requireContext()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        lifecycleScope.launch {
            viewModel.isFirstTimeFlow.collect {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToFragmentSelectLang())
            }
        }

        lifecycleScope.launch {
            viewModel.noConnectionFlow.collect() {

                findNavController().navigate(
                    SplashFragmentDirections.actionSplashFragmentToNoInternetFragment2()
                )

            }
        }
        lifecycleScope.launch {
            viewModel.languageFlow.collect() {
                localeHelper.selectLang(it)
                requireActivity().recreate()
            }
        }

        lifecycleScope.launch {
            viewModel.splashFlow.collect() {
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
            }
        }

    }

    override fun onStart() {
        super.onStart()
        Log.d("TAGDF", "onStart")
        viewModel.check()
    }

    override fun onPause() {
        super.onPause()
        Log.d("TAGDF", "onPause")

    }

    override fun onResume() {
        super.onResume()
        Log.d("TAGDF", "onResume")

    }

    override fun onStop() {
        super.onStop()
        Log.d("TAGDF", "onStop")

    }

    override fun onDetach() {
        super.onDetach()
        Log.d("TAGDF", "onDetach")

    }
}
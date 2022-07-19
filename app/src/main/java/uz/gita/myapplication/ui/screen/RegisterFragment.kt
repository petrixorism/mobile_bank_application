package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.request.RegisterRequest
import uz.gita.myapplication.databinding.FragmentRegisterBinding
import uz.gita.myapplication.ui.viewmodel.RegisterViewModel
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: RegisterViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            viewModel.loadingFlow.collect {
                showToast(it.toString())
                Log.d("RETROFIT", "$it")
            }
        }

        lifecycleScope.launch {
            viewModel.errorFlow.collect {
                showToast(it)
                Log.d("RETROFIT", it)

            }
        }

        lifecycleScope.launch {
            viewModel.successFlow.collect {
                showToast(it)
                Log.d("RETROFIT", it)

            }
        }

        binding.registerBtn
            .clicks()
            .onEach {
                viewModel.register(
//                    RegisterRequest(
//                        binding.nameEt.text.toString(),
//                        binding.lastNameEt.text.toString(),
//                        "+998${binding.phoneNumberEt.text.toString()}",
//                        binding.passwordTet.text.toString(),
//                    )
                    RegisterRequest(
                        "Ravshan",
                        "Baxranov",
                        "+998991234545",
                        "123456789",
                    )
                )
            }
            .debounce(2000L)
            .launchIn(lifecycleScope)


    }
}
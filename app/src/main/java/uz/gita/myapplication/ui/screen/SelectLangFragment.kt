package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentSelectLanguageBinding
import uz.gita.myapplication.ui.viewmodel.SelectLanguageViewModel
import uz.gita.myapplication.util.LocaleHelper

@AndroidEntryPoint
class SelectLangFragment : Fragment(R.layout.fragment_select_language) {

    private val binding by viewBinding(FragmentSelectLanguageBinding::bind)
    private val viewModel: SelectLanguageViewModel by viewModels()
    private val localeHelper by lazy { LocaleHelper(requireContext()) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            viewModel.languageFlow.collect {
                localeHelper.selectLang(it)
                requireActivity().recreate()
            }
        }
        lifecycleScope.launch {
            viewModel.startFlow.collect {
                if (it) findNavController().navigate(SelectLangFragmentDirections.actionFragmentSelectLangToOnboardParentFragment())
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.languageCheckFlow.collectLatest {
                (binding.radioGroup.getChildAt(it) as RadioButton).isChecked = true
            }
        }

        binding.radioGroup.children.forEachIndexed { index, view ->
            view.setOnClickListener {
                viewModel.changeLanguage(index)
            }
        }

        binding.backBtn.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.selectBtn.clicks()
            .onEach {
                viewModel.start()
            }.debounce(2000L).launchIn(lifecycleScope)

    }

}
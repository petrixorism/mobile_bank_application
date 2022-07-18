package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.SelectLanguageFragmentBinding

class SelectLangFragment : Fragment(R.layout.select_language_fragment) {

    private val binding by viewBinding(SelectLanguageFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            findNavController().navigate(SelectLangFragmentDirections.actionFragmentSelectLangToOnboardParentFragment())
        }
    }

}
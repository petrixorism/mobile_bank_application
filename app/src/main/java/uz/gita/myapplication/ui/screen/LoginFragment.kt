package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentSelectLanguageBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentSelectLanguageBinding::bind)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }
}
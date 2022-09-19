package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.locale.SharedPref
import uz.gita.myapplication.databinding.FragmentBaseUrlBinding
import uz.gita.myapplication.util.showSnackBar

class BaseUrlFragment : Fragment(R.layout.fragment_base_url) {

    private val binding by viewBinding(FragmentBaseUrlBinding::bind)

    @RequiresApi(33)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.button.setOnClickListener {
            val url = binding.editTextTextPersonName.text.toString()
            if (checkURL(url)) {
                SharedPref.getInstance().baseUrl = url
                showSnackBar("BASE URL has been changed")
            } else {
                showSnackBar("It is not BASE URL")
            }
        }
        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


}

fun checkURL(url: String): Boolean {
    return url.startsWith("https://") && url.length > 12
}


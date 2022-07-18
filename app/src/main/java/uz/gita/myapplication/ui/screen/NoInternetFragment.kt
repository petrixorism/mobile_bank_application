package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentNoConnectionBinding
import uz.gita.myapplication.util.isConnected
import uz.gita.myapplication.util.showToast

class NoInternetFragment : Fragment(R.layout.fragment_no_connection) {

    private val binding by viewBinding(FragmentNoConnectionBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.retryBtn.setOnClickListener {
            if (isConnected()) requireActivity().onBackPressed()
            else showToast(getString(R.string.no_internet_connection))
        }

    }
}
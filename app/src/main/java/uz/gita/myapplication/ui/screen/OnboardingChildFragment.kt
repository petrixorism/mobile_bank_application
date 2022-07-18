package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.OnboardingChildFragmentBinding

class OnboardingChildFragment : Fragment(R.layout.onboarding_child_fragment) {

    private val binding by viewBinding(OnboardingChildFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        arguments?.takeIf {
            it.containsKey("object")
        }?.apply {
            when (getInt("object")) {
                0 -> {
                    binding.contentIv.setBackgroundResource(R.drawable.ic_onboarding1)
                }
                1 -> {
                    binding.contentIv.setBackgroundResource(R.drawable.ic_onboarding2)

                }
                2 -> {
                    binding.contentIv.setBackgroundResource(R.drawable.ic_onboarding3)

                }
            }


        }
    }

}
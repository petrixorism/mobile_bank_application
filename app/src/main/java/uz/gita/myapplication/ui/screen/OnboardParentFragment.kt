package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentOnboardingParentBinding
import uz.gita.myapplication.ui.adapter.OnboardingPagerAdapter
import uz.gita.myapplication.util.changeVisibility

@AndroidEntryPoint
class OnboardParentFragment : Fragment(R.layout.fragment_onboarding_parent) {

    private val binding by viewBinding(FragmentOnboardingParentBinding::bind)
    private lateinit var pagerAdapter: OnboardingPagerAdapter
    private lateinit var viewPager: ViewPager2

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = OnboardingPagerAdapter(this)
        viewPager = binding.viewPager
        viewPager.adapter = pagerAdapter
        binding.dotsIndicator.setViewPager2(viewPager)


        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.skipTv.changeVisibility(position != 2)
                binding.nextTv.changeVisibility(position != 2)
                binding.startBtn.changeVisibility(position == 2)
            }

        })

        binding.startBtn.setOnClickListener {
            findNavController().navigate(OnboardParentFragmentDirections.actionOnboardParentFragmentToMainFragment())
        }

        binding.nextTv.setOnClickListener {
            viewPager.currentItem++
        }
        binding.skipTv.setOnClickListener {
            viewPager.currentItem = 2
        }

    }

}
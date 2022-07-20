package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentOnboardingParentBinding
import uz.gita.myapplication.ui.adapter.OnboardingPagerAdapter
import uz.gita.myapplication.ui.viewmodel.OnboardViewModel
import uz.gita.myapplication.util.changeVisibility

@AndroidEntryPoint
class OnboardParentFragment : Fragment(R.layout.fragment_onboarding_parent) {

    private val binding by viewBinding(FragmentOnboardingParentBinding::bind)
    private lateinit var pagerAdapter: OnboardingPagerAdapter
    private lateinit var viewPager: ViewPager2
    private val viewModel: OnboardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        pagerAdapter = OnboardingPagerAdapter(this)
        viewPager = binding.viewPager
        viewPager.adapter = pagerAdapter
        binding.dotsIndicator.setViewPager2(viewPager)

        lifecycleScope.launch {
            viewModel.startFlow.collect() {
                findNavController().navigate(OnboardParentFragmentDirections.actionOnboardParentFragmentToLoginFragment())
            }
        }

        lifecycleScope.launch {
            viewModel.connectionFlow.collect() {
                if (!it) {
                    findNavController().navigate(OnboardParentFragmentDirections.actionGlobalNoInternetFragment())
                }
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.skipTv.changeVisibility(position != 2)
                binding.nextTv.changeVisibility(position != 2)
                binding.startBtn.changeVisibility(position == 2)
            }

        })

        binding.startBtn.setOnClickListener {
            viewModel.start()
        }

        binding.nextTv.setOnClickListener {
            viewPager.currentItem++
        }
        binding.skipTv.setOnClickListener {
            viewPager.currentItem = 2
        }

    }

}
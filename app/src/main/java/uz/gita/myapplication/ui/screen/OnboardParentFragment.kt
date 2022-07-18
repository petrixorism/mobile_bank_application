package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.widget.ViewPager2
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.tabs.TabLayoutMediator
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.OnboardingParentFragmentBinding
import uz.gita.myapplication.ui.adapter.OnboardingPagerAdapter
import uz.gita.myapplication.util.showToast

class OnboardParentFragment : Fragment(R.layout.onboarding_parent_fragment) {

    private val binding by viewBinding(OnboardingParentFragmentBinding::bind)
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



            }

        })


    }

}
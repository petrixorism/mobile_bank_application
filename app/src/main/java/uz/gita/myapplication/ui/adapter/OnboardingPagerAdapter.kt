package uz.gita.myapplication.ui.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.gita.myapplication.ui.screen.OnboardingChildFragment


class OnboardingPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {

        val fragment = OnboardingChildFragment()
        fragment.arguments = Bundle().apply {
            putInt("object", position)
        }
        return fragment
    }


}
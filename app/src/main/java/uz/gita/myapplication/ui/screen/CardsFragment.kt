package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentCardsBinding
import uz.gita.myapplication.ui.adapter.CardAdapter
import uz.gita.myapplication.ui.viewmodel.CardsViewModel
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class CardsFragment : Fragment(R.layout.fragment_cards) {

    private val binding by viewBinding(FragmentCardsBinding::bind)
    private val viewModel: CardsViewModel by viewModels()
    private val adapter by lazy { CardAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenCreated {
            viewModel.loadingFlow.collect {
                binding.loadingPb.isVisible = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.messageFlow.collect {
                showToast(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.allCardsFlow.collect {
                adapter.submitList(it)
            }
        }

        binding.cardsRv.adapter = adapter

        binding.addCardCl.setOnClickListener {
            findNavController().navigate(CardsFragmentDirections.actionCardsFragmentToAddCardFragment())
        }

    }

}
package uz.gita.myapplication.ui.screen

import android.app.AlertDialog
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
import uz.gita.myapplication.data.source.remote.request.IgnoreBalanceRequest
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

        lifecycleScope.launchWhenCreated {
            viewModel.deletedCardFlow.collect {
                viewModel.allCard()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.ignoreBalanceFlow.collect {
                viewModel.allCard()
            }
        }

        binding.cardsRv.adapter = adapter

        binding.addCardCl.setOnClickListener {
            findNavController().navigate(CardsFragmentDirections.actionCardsFragmentToAddCardFragment())
        }

        adapter.setEditCardListener {
            findNavController().navigate(
                CardsFragmentDirections.actionCardsFragmentToEditCardFragment(
                    it.pan, it.exp, it.owner, it.color, it.cardName, it.id
                )
            )
        }
        adapter.setIgnoreBalanceListener {
            viewModel.ignoreBalanceCard(
                IgnoreBalanceRequest(
                    userCardId = it.userCardId,
                    ignoreBalance = it.ignoreBalance
                )
            )
        }
        adapter.setDeleteCardListener {
            openAlertDialog(it.pan)
//            viewModel.deleteCard(it.pan)
        }
        viewModel.allCard()

    }

    fun openAlertDialog(pan: String) {
        val builder = AlertDialog.Builder(requireContext())
        //set title for alert dialog
        builder.setTitle(getString(R.string.delete))
        builder.setCancelable(true)
        //set message for alert dialog
        builder.setMessage(getString(R.string.delete_quote))
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton(getString(R.string.yes)) { _, _ ->
            viewModel.deleteCard(pan)
        }
        builder.setNegativeButton(getString(R.string.cancel), null)
        // Create the AlertDialog
        // Set other dialog properties
        val alertDialog: AlertDialog = builder.create()

        //performing negative action


        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}
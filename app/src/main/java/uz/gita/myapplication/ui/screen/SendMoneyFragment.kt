package uz.gita.myapplication.ui.screen

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.request.TransferRequest
import uz.gita.myapplication.databinding.FragmentSendMoneyBinding
import uz.gita.myapplication.ui.adapter.CardTransferAdapter
import uz.gita.myapplication.ui.viewmodel.SendMoneyViewModel
import uz.gita.myapplication.util.showToast
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SendMoneyFragment : Fragment(R.layout.fragment_send_money) {

    private val binding by viewBinding(FragmentSendMoneyBinding::bind)

    private val args by navArgs<SendMoneyFragmentArgs>()
    private val viewModel: SendMoneyViewModel by viewModels()
    private var senderId = 0
    val adapter = CardTransferAdapter()

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.recSum.text = args.sum.toString()
        binding.recPan.text = "${args.receiverPan}\n${args.receiverName}"
        binding.recFee.text = "${args.sum.toDouble() * 0.01} (1%)"

        lifecycleScope.launchWhenCreated {
            viewModel.loadingFlow.collect {
                binding.loadingPb.isVisible = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.allCardsFlow.collect { list ->
                adapter.submitList(list)
                if (list.isNotEmpty()) {
                    list[0].let {
                        binding.cardName.text = it.cardName
                        binding.cardPan.text = it.pan
                        binding.cardSum.text
                        senderId = it.id
                    }
                } else {
                    binding.cardName.text = getString(R.string.no_card_found)
                    binding.cardPan.text = ".... .... .... ...."
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.ownerByIdFlow.collect {

                binding.homeBtn.isClickable = true
                binding.backTv.isClickable = true

                binding.receiverTv.text = it

                animateOut()
                delay(700L)
                animateIn()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.sendMoneySuccessFlow.collect {

                val sdf = SimpleDateFormat("d MMM yyyy HH:mm:ss")
                val netDate = Date(it.time)
                val time = sdf.format(netDate)


                viewModel.ownerById(it.receiver.toString())
                binding.doneAmountTv.text = it.amount.toString() + " so'm"
                binding.timeTv.text = time

//                binding.lifecycleScope.launch {
//                    animateOut()
//                    delay(700L)
//                    animateIn()
//                }

//                binding.doneAnimation.isVisible = true
//                delay(2000)
//                binding.doneAnimation.isVisible = false
//                showSnackBar(getString(R.string.transfer_success))
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.messageFlow.collect {
                showToast(it)
            }
        }
        binding.backButton.setOnClickListener {


            requireActivity().onBackPressed()


        }
        binding.selectAnotherCard.setOnClickListener {
            openCardsBottomSheetDialog()
        }
        binding.sendMoneyBtn.setOnClickListener {
            viewModel.sendMoney(
                TransferRequest(
                    amount = args.sum,
                    receiverPan = args.receiverPan,
                    sender = senderId
                )
            )

        }
        binding.homeBtn.setOnClickListener {
            findNavController().navigate(SendMoneyFragmentDirections.actionGlobalHomeFragment())
        }

        binding.backTv.setOnClickListener {
            requireActivity().onBackPressed()
        }
        viewModel.allCard()
    }

    @SuppressLint("CutPasteId", "InflateParams", "SetTextI18n")
    private fun openCardsBottomSheetDialog() {
        val dialog = BottomSheetDialog(this.requireContext())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val view = layoutInflater.inflate(R.layout.bottomsheet_cards, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)

        val recyclerView = dialog.findViewById<RecyclerView>(R.id.cards_rv)!!

        recyclerView.adapter = adapter

        adapter.setClickCardListener {
            binding.cardName.text = it.cardName
            binding.cardPan.text = it.pan
            senderId = it.id
            dialog.dismiss()
        }
        dialog.show()


    }

    private fun animateIn() {

        binding.successContainer.animate().apply {
            duration = 600L
            alpha(1f)
            start()

        }
    }

    private fun animateOut() {
        binding.screenContainer.children.forEach {
            lifecycleScope.launch {
                it.animate().apply {
                    duration = 600L
                    translationXBy(900f)
                    alpha(0f)
                    start()
                }
                delay(100L)
            }
        }
    }


}
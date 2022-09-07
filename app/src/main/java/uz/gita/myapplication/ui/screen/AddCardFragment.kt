package uz.gita.myapplication.ui.screen

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.request.CardRequest
import uz.gita.myapplication.data.source.remote.request.VerifyCardRequest
import uz.gita.myapplication.databinding.FragmentAddCardBinding
import uz.gita.myapplication.ui.viewmodel.AddCardViewModel
import uz.gita.myapplication.util.CreditCardFormatWatcher
import uz.gita.myapplication.util.animation.Animator
import uz.gita.myapplication.util.showSnackBar
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class AddCardFragment : Fragment(R.layout.fragment_add_card) {

    private val binding by viewBinding(FragmentAddCardBinding::bind)
    private val viewModel: AddCardViewModel by viewModels()
    private val animator by lazy { Animator() }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.panEt.addTextChangedListener(CreditCardFormatWatcher())


        lifecycleScope.launchWhenCreated {
            viewModel.cardAddedFlow.collect {
                openVerifyBottomSheetDialog(binding.panEt.text.toString().filter {
                    it.isDigit()
                })
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.loadingFlow.collect {
                binding.loadingP.isVisible = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.messageFlow.collect {
                showToast(it)
            }
        }

        binding.addCardBtn.clicks()
            .onEach {
                val pan = binding.panEt.text.toString().filter { it.isDigit() }
                val cardName = binding.cardNameEt.text.toString()
                val exp = binding.expEt.text.toString()
                viewModel.addCard(CardRequest(cardName, pan, exp))
            }.debounce(2000L).launchIn(lifecycleScope)

        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun openVerifyBottomSheetDialog(pan: String) {
        val dialog = BottomSheetDialog(this.requireContext())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val view = layoutInflater.inflate(R.layout.bottomsheet_verify, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)

        val smsCode = dialog.findViewById<EditText>(R.id.smscode)!!
        val numberTv = dialog.findViewById<TextView>(R.id.phone_number_bottom_sheet)!!
        val resendTv = dialog.findViewById<TextView>(R.id.resend_btn)!!

        resendTv.isVisible = false
        numberTv.isVisible = false



        lifecycleScope.launch {
            viewModel.verifiedFlow.collect() {
                hideKeyboard(requireActivity())
                dialog.dismiss()
                showSnackBar("Card has been added")
                delay(500L)
                requireActivity().onBackPressed()
            }
        }

        lifecycleScope.launch {
            viewModel.messageFlow.collect() {
                animator.shake(smsCode)
                showToast(it)
            }
        }



        smsCode.textChanges()
            .onEach {
                if (it.length == 6) {
                    viewModel.verify(VerifyCardRequest(pan, it.toString()))
                }
            }
            .flowWithLifecycle(lifecycle)
            .launchIn(lifecycleScope)
        dialog.show()


    }


    //--Functions--//
    private fun hideKeyboard(activity: Activity) {
        val imm: InputMethodManager =
            activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}






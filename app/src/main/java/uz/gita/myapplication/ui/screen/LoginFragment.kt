package uz.gita.myapplication.ui.screen

import android.annotation.SuppressLint
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
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChanges
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.request.LoginRequest
import uz.gita.myapplication.data.source.remote.request.VerifyRequest
import uz.gita.myapplication.databinding.FragmentLoginBinding
import uz.gita.myapplication.ui.viewmodel.LoginViewModel
import uz.gita.myapplication.util.animation.Animator
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()
    private val animator by lazy { Animator() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launch {
            viewModel.loadingFlow.collect {
                binding.progressCircular.isVisible = it
            }
        }


        lifecycleScope.launch {
            viewModel.errorFlow.collect {
                showToast(it)
            }
        }

        lifecycleScope.launch {
            viewModel.successLoginFlow.collect {
                openVerifyBottomSheetDialog("+998${binding.phoneNumberEt.text.toString()}")
            }
        }


        binding.goToRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }

        binding.loginBtn.clicks()
            .onEach {
                viewModel.login(
                    LoginRequest(
                        "+998${binding.phoneNumberEt.text}",
                        binding.passwordTet.text.toString()
                    )
                )
            }
            .debounce(2000L)
            .launchIn(lifecycleScope)

        binding.forgotPasswordBtn.setOnClickListener {
            showToast("Don't forget )")
        }
    }

    //-- BottomSheet Dialog--//

    @SuppressLint("CutPasteId", "InflateParams", "SetTextI18n")
    private fun openVerifyBottomSheetDialog(phoneNumber: String) {
        val dialog = BottomSheetDialog(this.requireContext())
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        val view = layoutInflater.inflate(R.layout.bottomsheet_verify, null)
        dialog.setCancelable(true)
        dialog.setContentView(view)

        val smsCode = dialog.findViewById<EditText>(R.id.smscode)!!
        val verificationTv = dialog.findViewById<TextView>(R.id.verifytext)!!
        val numberTv = dialog.findViewById<TextView>(R.id.phone_number_bottom_sheet)!!

        lifecycleScope.launch {
            viewModel.verifiedFlow.collect() {
                hideKeyboard(requireActivity())
                dialog.dismiss()
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSetPinFragment())
            }
        }

        lifecycleScope.launch {
            viewModel.errorVerifyFlow.collect() {
                animator.shake(smsCode)
                verificationTv.text = "Invalid code"
            }
        }

        lifecycleScope.launch {
            viewModel.loadingVerifyFlow.collect() {
                if (it) {
                    verificationTv.text = "Checking..."
                } else {
                    verificationTv.text = phoneNumber
                }
            }
        }

        numberTv.text = phoneNumber
        smsCode.textChanges()
            .onEach {
                if (it.length == 6) {
                    viewModel.verify(VerifyRequest(phoneNumber, it.toString()))
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
package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks
import uz.gita.myapplication.R
import uz.gita.myapplication.data.source.remote.request.ColorRequest
import uz.gita.myapplication.data.source.remote.request.EditCardRequest
import uz.gita.myapplication.databinding.FragmentEditCardBinding
import uz.gita.myapplication.ui.viewmodel.EditCardViewModel
import uz.gita.myapplication.util.getColorDrawable
import uz.gita.myapplication.util.showSnackBar
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class EditCardFragment : Fragment(R.layout.fragment_edit_card) {

    private val args by navArgs<EditCardFragmentArgs>()
    private val binding by viewBinding(FragmentEditCardBinding::bind)
    private val viewModel: EditCardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lifecycleScope.launchWhenCreated {
            viewModel.messageFlow.collect {
                showToast(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.loadingFlow.collect {
                binding.loadingPb.isVisible = it
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.cardEditedFlow.collect {
                showSnackBar(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.colorChangedFlow.collect {
                binding.cardColor.background = getColorDrawable(it)
            }
        }

        binding.cardNameEt.doOnTextChanged { text, _, _, _ ->
            binding.cardName.text = text
        }

        binding.cardName.text = args.cardname
        binding.cardPan.text = args.pan
        binding.cardOwnerName.text = args.owner
        binding.cardColor.background = getColorDrawable(args.color)
        binding.cardExp.text = args.exp

        getColorButtons().forEachIndexed { index, imageButton ->
            imageButton.clicks().onEach {
                viewModel.changeColor(
                    ColorRequest(
                        color = index + 1,
                        userCardId = args.id
                    )
                )
            }
                .debounce(2000L)
                .launchIn(lifecycleScope)
        }

        binding.editCardBtn.clicks()
            .onEach {
                viewModel.editCard(
                    EditCardRequest(
                        args.pan, binding.cardNameEt.text.toString()
                    )
                )
            }.debounce(2000L)
            .launchIn(lifecycleScope)


        binding.backButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    private fun getColorButtons(): List<ImageButton> {
        return listOf(
            binding.card1,
            binding.card2,
            binding.card3,
            binding.card4,
            binding.card5,
            binding.card6,
            binding.card7,
            binding.card8
        )
    }

}
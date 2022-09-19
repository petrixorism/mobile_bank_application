package uz.gita.myapplication.ui.screen

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.filter
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentHistoryBinding
import uz.gita.myapplication.ui.adapter.HistoryAdapter
import uz.gita.myapplication.ui.viewmodel.HistoryViewModel
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val binding by viewBinding(FragmentHistoryBinding::bind)
    private val adapter by lazy { HistoryAdapter() }
    private val viewModel: HistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.historyRv.adapter = adapter

        lifecycleScope.launchWhenCreated {
            viewModel.messageFlow.collect {
                showToast(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.historyFlow.collect {
                adapter.submitData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.loadingFlow.collect {
                if (!it) {
                    binding.shimmerViewContainer.visibility = GONE
                }
            }
        }
        adapter.setItemClick {

        }

    }

}
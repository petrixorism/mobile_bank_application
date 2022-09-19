package uz.gita.myapplication.ui.screen

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint
import uz.gita.myapplication.R
import uz.gita.myapplication.databinding.FragmentMainBinding
import uz.gita.myapplication.ui.viewmodel.HomeViewModel
import uz.gita.myapplication.util.showToast

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: HomeViewModel by viewModels()


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLineChartData()

        lifecycleScope.launchWhenCreated {
            viewModel.loadingFlow.collect {
                binding.loadingPb.isVisible = it
            }
        }
        lifecycleScope.launchWhenCreated {
            viewModel.infoFlow.collect {
                binding.usernameTv.text =
                    "${it.firstName} ${it.lastName}"
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.avatarUrlFlow.collect {
                if (it.isNotBlank()) {
                    val split = it.replaceFirst(":8080", "")
                    val url = "https:" + split.substring(5)

                    Glide
                        .with(requireContext())
                        .load(url)
                        .error(R.drawable.img)
                        .centerCrop()
                        .placeholder(R.drawable.img)
                        .into(binding.profilePicSiv)
                }

            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.messageFlow.collect {
                showToast(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.totalSumFlow.collect {
                binding.totalSumTv.text = it
            }
        }
        binding.historyTv.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionGlobalHistoryFragment())
        }
        viewModel.getProfileInfo()

        viewModel.getAvatar()

        var counter = 0

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (counter == 0) {
                        showToast(getString(R.string.back_pressed))
                        counter++
                    } else {
                        requireActivity().finish()
                    }
                }

            })

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setLineChartData() {

        val lineValues = ArrayList<Entry>()
        lineValues.add(Entry(20f, 0.0F))
        lineValues.add(Entry(30f, 3.0F))
        lineValues.add(Entry(40f, 2.0F))
        lineValues.add(Entry(50f, 4.0F))
        lineValues.add(Entry(60f, 3.0F))
        lineValues.add(Entry(70f, 6.0F))
        lineValues.add(Entry(80f, 3.0F))
        lineValues.add(Entry(90f, 4.0F))
        lineValues.add(Entry(100f, 5.0F))

        val lineDataset = LineDataSet(lineValues, "").apply {
            setDrawFilled(true)
            setCircleColor(requireContext().getColor(R.color.yellow))

            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.yellow_gradient)
            circleRadius = 4f
            color = requireContext().getColor(R.color.yellow)
            lineWidth = 3f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextColor = Color.WHITE
        }

        val lineData = LineData(lineDataset)
        //We connect our data to the UI Screen
        binding.lastThirtyDaysChart.apply {
            setBorderColor(requireContext().getColor(R.color.green))
            setBackgroundColor(Color.TRANSPARENT)
            animateXY(2000, 1000, Easing.EaseInCubic)

            axisRight.setDrawGridLines(false)
            xAxis.setDrawGridLines(false)
            legend.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            data = lineData
            axisLeft.textColor = Color.WHITE
            axisRight.textColor = Color.WHITE
            xAxis.textColor = Color.WHITE
            description.isEnabled = false
        }


    }

}
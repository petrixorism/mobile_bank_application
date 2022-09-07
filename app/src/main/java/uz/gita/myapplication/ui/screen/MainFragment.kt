package uz.gita.myapplication.ui.screen

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setLineChartData()

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
            viewModel.totalSumFlow.collect {
                binding.totalSumTv.text = it
            }
        }

    }

    private fun setLineChartData() {

        val lineValues = ArrayList<Entry>()
        lineValues.add(Entry(20f, 0.0F))
        lineValues.add(Entry(30f, 3.0F))
        lineValues.add(Entry(40f, 2.0F))
        lineValues.add(Entry(50f, 4.0F))
        lineValues.add(Entry(60f, 3.0F))
        lineValues.add(Entry(70f, 6.0F))
        lineValues.add(Entry(80f, 3.0F))
        lineValues.add(Entry(90f, 2.0F))
        lineValues.add(Entry(100f, 5.0F))
//        lineValues.add(Entry(110f, 7.0F))
//        lineValues.add(Entry(120f, 5.0F))
//        lineValues.add(Entry(130f, 2.0F))
//        lineValues.add(Entry(140f, 4.0F))

        val linedataset = LineDataSet(lineValues, "").apply {
            setDrawFilled(true)
            setCircleColor(requireContext().getColor(R.color.yellow))

            fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.yellow_gradient)
            circleRadius = 4f
            color = requireContext().getColor(R.color.yellow)
            lineWidth = 3f
            mode = LineDataSet.Mode.CUBIC_BEZIER
            valueTextColor = Color.WHITE
        }

        val lineData = LineData(linedataset)
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
package com.hoomanholding.jpamanager.view.fragment.report.sale

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.charts.CombinedChart.DrawOrder
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.hoomanholding.applibrary.model.data.response.report.VisitorSalesReportModel
import com.hoomanholding.applibrary.view.fragment.JpaFragment
import com.hoomanholding.jpamanager.R
import com.hoomanholding.jpamanager.databinding.FragmentReportVisitorSaleBinding
import com.hoomanholding.jpamanager.view.activity.MainActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * create by m-latifi on 4/25/2023
 */

@AndroidEntryPoint
class VisitorSaleReportFragment(override var layout: Int = R.layout.fragment_report_visitor_sale) :
    JpaFragment<FragmentReportVisitorSaleBinding>() {

    private val viewModel: VisitorSaleReportViewModel by viewModels()

    //---------------------------------------------------------------------------------------------- onViewCreated
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        viewModel.requestVisitorSalesReport()
//        setChart()
    }
    //---------------------------------------------------------------------------------------------- onViewCreated


    //---------------------------------------------------------------------------------------------- showMessage
    private fun showMessage(message: String) {
        activity?.let {
            (it as MainActivity).showMessage(message)
        }
    }
    //---------------------------------------------------------------------------------------------- showMessage


    //---------------------------------------------------------------------------------------------- observeLiveData
    private fun observeLiveData() {
        viewModel.errorLiveDate.observe(viewLifecycleOwner) {
            showMessage(it.message)
        }

        viewModel.visitorSaleReportLiveData.observe(viewLifecycleOwner){
            setChart(it)
        }

    }
    //---------------------------------------------------------------------------------------------- observeLiveData


    private fun setChart(items: List<VisitorSalesReportModel>) {

        binding.chart.description.isEnabled = false
        binding.chart.setBackgroundColor(requireContext().getColor(R.color.primaryColorVariant))
        binding.chart.setDrawGridBackground(false)
        binding.chart.setDrawBarShadow(false)
        binding.chart.setDrawValueAboveBar(true)
        binding.chart.isHighlightFullBarEnabled = false
        binding.chart.drawOrder = arrayOf(DrawOrder.BAR, DrawOrder.LINE)

        val l: Legend = binding.chart.legend
        l.isWordWrapEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.formSize = 12f
        l.textSize = 15f
        l.xEntrySpace = 10f
        l.textColor = Color.WHITE

        val rightAxis: YAxis = binding.chart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val leftAxis: YAxis = binding.chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)

        val xAxis: XAxis = binding.chart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = 270f
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.WHITE
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.labelCount = items.size
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val position = value.toInt() % items.size
                return items[position].visitorName ?: ""
            }
        }

        val data = CombinedData()
        data.setData(generateLineData(items))
        data.setData(generateBarData(items))
//        data.setData(generateBubbleData())
//        data.setData(generateScatterData())
//        data.setData(generateCandleData())
//        data.setValueTypeface(tfLight)
        xAxis.axisMaximum = data.xMax + 0.25f
        binding.chart.data = data
        binding.chart.invalidate()

    }


    private fun generateLineData(items: List<VisitorSalesReportModel>): LineData {
        val d = LineData()
        val entries = mutableListOf<Entry>()
        for (index in items.indices)
            entries.add(Entry(index.toFloat(), items[index].salesAmount.toFloat()))
        val set = LineDataSet(entries, "نمودار فروش")
        set.color = Color.rgb(240, 238, 70)
        set.lineWidth = 2.5f
        set.setCircleColor(Color.rgb(240, 238, 70))
        set.circleRadius = 5f
        set.fillColor = Color.rgb(240, 238, 70)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.rgb(240, 238, 70)
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return d
    }


    private fun generateBarData(items: List<VisitorSalesReportModel>): BarData {
        val entries1: ArrayList<BarEntry> = ArrayList()
//        val entries2: ArrayList<BarEntry> = ArrayList()
        for (index in items.indices) {
            entries1.add(BarEntry(index.toFloat(), items[index].expectedSales.toFloat()))

            // stacked
/*            entries2.add(BarEntry(0f, floatArrayOf((10..25).random().toFloat(),
                (12..13).random().toFloat()
            )))*/
        }
        val set1 = BarDataSet(entries1, "نمودار هدف")
        set1.color = Color.rgb(60, 220, 78)
        set1.valueTextColor = Color.rgb(60, 220, 78)
        set1.valueTextSize = 8f
        set1.axisDependency = YAxis.AxisDependency.LEFT
/*        val set2 = BarDataSet(entries2, "")
        set2.stackLabels = arrayOf("Stack 1", "Stack 2")
        set2.setColors(Color.rgb(61, 165, 255), Color.rgb(23, 197, 255))
        set2.valueTextColor = Color.rgb(61, 165, 255)
        set2.valueTextSize = 10f
        set2.axisDependency = YAxis.AxisDependency.LEFT*/
//        val groupSpace = 0.06f
//        val barSpace = 0.02f // x2 dataset
//        val barWidth = 0.45f // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
//        val d = BarData(set2)
//        d.barWidth = barWidth

        // make this BarData object grouped
//        d.groupBars(0f, groupSpace, barSpace) // start at x = 0
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.barWidth = 0.5f
        return data
    }


/*
    private fun generateScatterData(): ScatterData {
        val d = ScatterData()
        val entries = mutableListOf<Entry>()
        var index = 0f
        while (index < count) {
            entries.add(Entry(index + 0.25f, (10..55).random().toFloat()))
            index += 0.5f
        }
        val set = ScatterDataSet(entries, "Scatter DataSet")
        set.setColors(*ColorTemplate.MATERIAL_COLORS)
        set.scatterShapeSize = 7.5f
        set.setDrawValues(false)
        set.valueTextSize = 10f
        d.addDataSet(set)
        return d
    }


    private fun generateCandleData(): CandleData {
        val d = CandleData()
        val entries: ArrayList<CandleEntry> = ArrayList()
        var index = 0
        while (index < count) {
            entries.add(CandleEntry(index + 1f, 90f, 70f, 85f, 75f))
            index += 2
        }
        val set = CandleDataSet(entries, "Candle DataSet")
        set.decreasingColor = Color.rgb(142, 150, 175)
        set.shadowColor = Color.DKGRAY
        set.barSpace = 0.3f
        set.valueTextSize = 10f
        set.setDrawValues(false)
        d.addDataSet(set)
        return d
    }


    private fun generateBubbleData(): BubbleData {
        val bd = BubbleData()
        val entries: ArrayList<BubbleEntry> = ArrayList()
        for (index in 0 until count) {
            val y: Float = (10..105).random().toFloat()
            val size: Float = (100..105).random().toFloat()
            entries.add(BubbleEntry(index + 0.5f, y, size))
        }
        val set = BubbleDataSet(entries, "Bubble DataSet")
        set.setColors(*ColorTemplate.VORDIPLOM_COLORS)
        set.valueTextSize = 10f
        set.valueTextColor = Color.WHITE
        set.highlightCircleWidth = 1.5f
        set.setDrawValues(true)
        bd.addDataSet(set)
        return bd
    }
*/

}
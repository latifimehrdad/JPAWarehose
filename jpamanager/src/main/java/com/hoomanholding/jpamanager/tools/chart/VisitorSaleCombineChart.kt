package com.hoomanholding.jpamanager.tools.chart

import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.hoomanholding.applibrary.model.data.response.report.VisitorSalesReportModel
import com.hoomanholding.jpamanager.R


/**
 * create by m-latifi on 4/26/2023
 */

class VisitorSaleCombineChart(
    private val chart: CombinedChart,
    private val items: List<VisitorSalesReportModel>
) {

    //---------------------------------------------------------------------------------------------- init
    init {
        chart.data = null
        chart.notifyDataSetChanged()
        chart.invalidate()
        chart.visibility = View.VISIBLE
        val data = CombinedData()
        data.setData(generateLineData())
        data.setData(generateBarData())
        setChartViewConfig()
        setLegendConfig()
        setYAxisConfig()
        setXAxisConfig(data.xMax)
        chart.data = data
        chart.invalidate()
    }
    //---------------------------------------------------------------------------------------------- init


    //---------------------------------------------------------------------------------------------- setChartViewConfig
    private fun setChartViewConfig(){
        chart.description.isEnabled = false
        chart.setBackgroundColor(chart.context.getColor(R.color.primaryColorVariant))
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.setMaxVisibleValueCount(60)
        chart.setPinchZoom(false)
        chart.isHighlightFullBarEnabled = false
        chart.drawOrder = arrayOf(CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE)
    }
    //---------------------------------------------------------------------------------------------- setChartViewConfig


    //---------------------------------------------------------------------------------------------- setLegendConfig
    private fun setLegendConfig(){
        val l: Legend = chart.legend
        l.isWordWrapEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.formSize = 12f
        l.textSize = 12f
        l.xEntrySpace = 10f
        l.textColor = Color.WHITE
    }
    //---------------------------------------------------------------------------------------------- setLegendConfig


    //---------------------------------------------------------------------------------------------- setYAxisConfig
    private fun setYAxisConfig(){
        val rightAxis: YAxis = chart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)

        val leftAxis: YAxis = chart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.setDrawLabels(false)
        leftAxis.spaceTop = 15f
        leftAxis.textColor = Color.WHITE
        leftAxis.textSize = 8f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
    }
    //---------------------------------------------------------------------------------------------- setYAxisConfig


    //---------------------------------------------------------------------------------------------- setXAxisConfig
    private fun setXAxisConfig(xMax: Float) {
        val xAxis: XAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelRotationAngle = 270f
        xAxis.setDrawGridLines(false)
        xAxis.textColor = Color.WHITE
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.labelCount = items.size
        xAxis.axisMaximum = xMax + 0.5f
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val position = value.toInt() % items.size
                return if (position > -1)
                    items[position].visitorName ?: ""
                else
                    ""
            }
        }
    }
    //---------------------------------------------------------------------------------------------- setXAxisConfig


    //---------------------------------------------------------------------------------------------- generateLineData
    private fun generateLineData(): LineData {
        val d = LineData()
        val entries = mutableListOf<Entry>()
        for (index in items.indices)
            entries.add(Entry(index.toFloat(), items[index].salesAmount.toFloat()))
        val set = LineDataSet(entries, "نمودار فروش")
        set.color = Color.rgb(255, 245, 157)
        set.lineWidth = 1.5f
        set.setCircleColor(Color.rgb(255, 245, 157))
        set.circleRadius = 4f
        set.fillColor = Color.rgb(255, 245, 157)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 7f
        set.valueTextColor = Color.rgb(255, 245, 157)
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return d
    }
    //---------------------------------------------------------------------------------------------- generateLineData


    //---------------------------------------------------------------------------------------------- generateLineData
    private fun generateBarData(): BarData {
        val entries1: ArrayList<BarEntry> = ArrayList()
        for (index in items.indices) {
            entries1.add(BarEntry(index.toFloat(), items[index].expectedSales.toFloat()))
        }
        val set1 = BarDataSet(entries1, "نمودار هدف")
        set1.color = Color.rgb(255, 84, 84)
        set1.valueTextColor = Color.rgb(255, 84, 84)
        set1.valueTextSize = 7f
        set1.axisDependency = YAxis.AxisDependency.LEFT
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        val data = BarData(dataSets)
        data.setValueTextSize(10f)
        data.barWidth = 0.7f
        return data
    }
    //---------------------------------------------------------------------------------------------- generateLineData


}
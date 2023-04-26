package com.hoomanholding.jpamanager.tools.chart

import android.graphics.Color
import android.view.View
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.hoomanholding.applibrary.model.data.response.report.VisitorSalesReportModel
import com.hoomanholding.jpamanager.R


/**
 * create by m-latifi on 4/26/2023
 */

class VisitorCustomerCombineChart(
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
    private fun setChartViewConfig() {
        chart.description.isEnabled = false
        chart.setBackgroundColor(chart.context.getColor(R.color.primaryColorVariant))
        chart.setDrawGridBackground(false)
        chart.setDrawBarShadow(false)
        chart.setDrawValueAboveBar(true)
        chart.setMaxVisibleValueCount(60)
        chart.isHighlightFullBarEnabled = false
        chart.drawOrder = arrayOf(CombinedChart.DrawOrder.BAR)
    }
    //---------------------------------------------------------------------------------------------- setChartViewConfig


    //---------------------------------------------------------------------------------------------- setLegendConfig
    private fun setLegendConfig() {
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
    private fun setYAxisConfig() {
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
        xAxis.setCenterAxisLabels(true)
        xAxis.textColor = Color.WHITE
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f
        xAxis.labelCount = items.size
        xAxis.axisMaximum = xMax + 1f
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
    private fun generateBarData(): BarData {
        val entries1: ArrayList<BarEntry> = ArrayList()
        val entries2: ArrayList<BarEntry> = ArrayList()
        val entries3: ArrayList<BarEntry> = ArrayList()

        for (index in items.indices) {
            entries1.add(BarEntry(index.toFloat(), items[index].customerCount.toFloat()))
            entries2.add(BarEntry(index.toFloat(), items[index].availableCustomerCount.toFloat()))
            entries3.add(BarEntry(index.toFloat(), items[index].coverageRate.toFloat()))
        }

        val set1 = BarDataSet(entries1, "تعداد مشتری")
        set1.color = Color.rgb(248, 187, 208)
        set1.valueTextColor = Color.rgb(248, 187, 208)
        set1.valueTextSize = 7f
        set1.axisDependency = YAxis.AxisDependency.LEFT

        val set2 = BarDataSet(entries2, "تعداد مشتری فعال")
        set2.color = Color.rgb(128, 203, 196)
        set2.valueTextColor = Color.rgb(128, 203, 196)
        set2.valueTextSize = 7f
        set2.axisDependency = YAxis.AxisDependency.LEFT

        val set3 = BarDataSet(entries3, "نرخ پوشش %")
        set3.color = Color.rgb(255, 236, 179)
        set3.valueTextColor = Color.rgb(255, 236, 179)
        set3.valueTextSize = 7f
        set3.axisDependency = YAxis.AxisDependency.LEFT

        val groupSpace = 0.07f
        val barSpace = 0.01f
        val barWidth = 0.3f

        val data = BarData(set1, set2, set3)
        data.barWidth = barWidth
        data.groupBars(0f, groupSpace, barSpace)
        data.setValueTextSize(10f)
        return data
    }
    //---------------------------------------------------------------------------------------------- generateLineData


}
package pl.polsl.peoplecounter.statistics_presentation

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat


class MyAxisValueFormatter : ValueFormatter() {
    private val mFormat: DecimalFormat
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        return mFormat.format(value.toDouble()) + " $"
    }

    init {
        mFormat = DecimalFormat("###,###,###,##0.0")
    }
}
package pl.polsl.peoplecounter.statistics_presentation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.Legend.LegendForm
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.IDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
//import com.github.mikephil.charting.utils.F
import com.github.mikephil.charting.utils.MPPointF
import pl.polsl.peoplecounter.R
import java.util.*


class BarChartSimpleActivity() : OnSeekBarChangeListener, AppCompatActivity(),
    OnChartValueSelectedListener {
    private var chart: BarChart? = null
    private var seekBarX: SeekBar? = null
    private var seekBarY: SeekBar? = null
    private var tvX: TextView? = null
    private var tvY: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_barchart_simple)
        setTitle("BarChartActivity")
        tvX = findViewById(R.id.tvXMax)
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY!!.setOnSeekBarChangeListener(this)
        seekBarX!!.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart!!.setOnChartValueSelectedListener(this)
        chart!!.setDrawBarShadow(false)
        chart!!.setDrawValueAboveBar(true)
        chart!!.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart!!.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart!!.setPinchZoom(false)
        chart!!.setDrawGridBackground(false)
        // chart.setDrawYLabels(false);
        val xAxisFormatter = DayAxisValueFormatter(chart!!)
        val xAxis = chart!!.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.typeface = Typeface.SANS_SERIF
        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f // only intervals of 1 day
        xAxis.labelCount = 7
        xAxis.setValueFormatter(xAxisFormatter)
        val custom: ValueFormatter = MyAxisValueFormatter()
        val leftAxis = chart!!.axisLeft
        leftAxis.typeface = Typeface.SANS_SERIF
        leftAxis.setLabelCount(8, false)
        leftAxis.setValueFormatter(custom)
        leftAxis.setPosition(YAxisLabelPosition.OUTSIDE_CHART)
        leftAxis.spaceTop = 15f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        val rightAxis = chart!!.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.typeface = Typeface.SANS_SERIF
        rightAxis.setLabelCount(8, false)
        rightAxis.setValueFormatter(custom)
        rightAxis.spaceTop = 15f
        rightAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        val l = chart!!.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
        l.form = LegendForm.SQUARE
        l.formSize = 9f
        l.textSize = 11f
        l.xEntrySpace = 4f
        val mv = XYMarkerView(this, xAxisFormatter)
        mv.setChartView(chart) // For bounds control
        chart!!.marker = mv // Set the marker to the chart

        // setting data
        seekBarY!!.progress = 50
        seekBarX!!.progress = 12

        // chart.setDrawLegend(false);
    }

    private fun setData(count: Int, range: Float) {
        val start = 1f
        val values = ArrayList<BarEntry>()
        var i = start.toInt()
        while (i < start + count) {
            val my_val = (Math.random() * (range + 1)).toFloat()
            if (Math.random() * 100 < 25) {
                values.add(BarEntry(i.toFloat(), my_val, getResources().getDrawable(R.drawable.gradient_bg)))
            } else {
                values.add(BarEntry(i.toFloat(), my_val))
            }
            i++
        }
        val set1: BarDataSet
        if (chart!!.data != null &&
            chart!!.data.dataSetCount > 0
        ) {
            set1 = chart!!.data.getDataSetByIndex(0) as BarDataSet
            set1.values = values
            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(values, "The year 2017")
            set1.setDrawIcons(false)
            val startColor1 = ContextCompat.getColor(this, R.color.purple_200)
            val startColor2 = ContextCompat.getColor(this, R.color.purple_200)
            val startColor3 = ContextCompat.getColor(this, R.color.purple_200)
            val startColor4 = ContextCompat.getColor(this, R.color.purple_200)
            val startColor5 = ContextCompat.getColor(this, R.color.purple_200)
            val endColor1 = ContextCompat.getColor(this, R.color.black)
            val endColor2 = ContextCompat.getColor(this, R.color.black)
            val endColor3 = ContextCompat.getColor(this, R.color.black)
            val endColor4 = ContextCompat.getColor(this, R.color.black)
            val endColor5 = ContextCompat.getColor(this, R.color.black)
            val gradientFills: MutableList<Fill> = ArrayList<Fill>()
            gradientFills.add(Fill(startColor1, endColor1))
            gradientFills.add(Fill(startColor2, endColor2))
            gradientFills.add(Fill(startColor3, endColor3))
            gradientFills.add(Fill(startColor4, endColor4))
            gradientFills.add(Fill(startColor5, endColor5))
            //set1.setColors(gradientFills)
            //set1.setFills(gradientFills)
            val dataSets = ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            data.setValueTextSize(10f)
            data.setValueTypeface(Typeface.SANS_SERIF)
            data.barWidth = 0.9f
            chart!!.data = data
        }
    }
    /*
    fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.bar, menu)
        return true
    }

    fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartActivity.java")
                ContextCompat.startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set: IDataSet<*> in chart!!.data.dataSets) set.setDrawValues(!set.isDrawValuesEnabled)
                chart!!.invalidate()
            }
            R.id.actionToggleIcons -> {
                for (set: IDataSet<*> in chart!!.data.dataSets) set.setDrawIcons(!set.isDrawIconsEnabled)
                chart!!.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart!!.data != null) {
                    chart!!.data.isHighlightEnabled = !chart!!.data.isHighlightEnabled
                    chart!!.invalidate()
                }
            }
            R.id.actionTogglePinch -> {
                if (chart!!.isPinchZoomEnabled) chart!!.setPinchZoom(false) else chart!!.setPinchZoom(
                    true
                )
                chart!!.invalidate()
            }
            R.id.actionToggleAutoScaleMinMax -> {
                chart!!.isAutoScaleMinMaxEnabled = !chart!!.isAutoScaleMinMaxEnabled
                chart!!.notifyDataSetChanged()
            }
            R.id.actionToggleBarBorders -> {
                for (set: IBarDataSet in chart!!.data.dataSets) (set as BarDataSet).barBorderWidth =
                    if (set.getBarBorderWidth() == 1f) 0f else 1f
                chart!!.invalidate()
            }
            R.id.animateX -> {
                chart!!.animateX(2000)
            }
            R.id.animateY -> {
                chart!!.animateY(2000)
            }
            R.id.animateXY -> {
                chart!!.animateXY(2000, 2000)
            }
            R.id.actionSave -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveToGallery()
                } else {
                    requestStoragePermission(chart)
                }
            }
        }
        return true
    }*/

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        tvX!!.text = seekBarX!!.progress.toString()
        tvY!!.text = seekBarY!!.progress.toString()
        setData(seekBarX!!.progress, seekBarY!!.progress.toFloat())
        chart!!.invalidate()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    private val onValueSelectedRectF = RectF()
    override fun onValueSelected(e: Entry, h: Highlight) {
        if (e == null) return
        val bounds = onValueSelectedRectF
        chart!!.getBarBounds(e as BarEntry, bounds)
        val position = chart!!.getPosition(e, AxisDependency.LEFT)
        Log.i("bounds", bounds.toString())
        Log.i("position", position.toString())
        Log.i(
            "x-index",
            "low: " + chart!!.lowestVisibleX + ", high: "
                    + chart!!.highestVisibleX
        )
        MPPointF.recycleInstance(position)
    }

    override fun onNothingSelected() {}
}
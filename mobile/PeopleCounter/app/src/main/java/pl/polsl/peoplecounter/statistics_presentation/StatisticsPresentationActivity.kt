package pl.polsl.peoplecounter.statistics_presentation


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import pl.polsl.peoplecounter.R
import java.util.*


class StatisticsPresentationActivity : AppCompatActivity(){
    private var chart: BarChart? = null
    private var seekBarX: SeekBar? = null
    private var seekBarY: SeekBar? = null
    private var tvX: TextView? = null
    private var tvY: TextView? = null
    private val colors_1: Array<String>? = arrayOf("green", "red", "blue")
    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_barchart)
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        var dataSets: ArrayList<BarDataSet?> = ArrayList()
        val xAxisValues: List<String> = ArrayList(
            Arrays.asList(
                "Jan",
                "Feb",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "Decemeber"
            )
        )
        val incomeEntries: MutableList<BarEntry> = getIncomeEntries()
        dataSets = ArrayList()
        val set1: BarDataSet
        set1 = BarDataSet(incomeEntries, "Income")
        set1.color = Color.rgb(65, 168, 121)
        set1.valueTextColor = Color.rgb(55, 70, 73)
        set1.valueTextSize = 10f
        dataSets.add(set1)

//customization

//customization
        val mLineGraph: BarChart = findViewById<BarChart>(R.id.bar_chart)
        mLineGraph.setTouchEnabled(true)
        mLineGraph.isDragEnabled = true
        mLineGraph.setScaleEnabled(false)
        mLineGraph.setPinchZoom(false)
        mLineGraph.setDrawGridBackground(false)
        mLineGraph.extraLeftOffset = 15f
        mLineGraph.extraRightOffset = 15f
//to hide background lines
//to hide background lines
        mLineGraph.xAxis.setDrawGridLines(false)
        mLineGraph.axisLeft.setDrawGridLines(false)
        mLineGraph.axisRight.setDrawGridLines(false)

//to hide right Y and top X border

//to hide right Y and top X border
        val rightYAxis = mLineGraph.axisRight
        rightYAxis.isEnabled = false
        val leftYAxis = mLineGraph.axisLeft
        leftYAxis.isEnabled = false
        val topXAxis = mLineGraph.xAxis
        topXAxis.isEnabled = false


        val xAxis = mLineGraph.xAxis
        xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.isEnabled = true
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        //set1.lineWidth = 4f
        //set1.circleRadius = 3f
        set1.setDrawValues(false)
        //set1.circleHoleColor = resources.getColor(R.color.purple_200)
        //set1.setCircleColor(resources.getColor(R.color.teal_700))

//String setter in x-Axis

//String setter in x-Axis
        mLineGraph.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        val data = BarData(dataSets as List<IBarDataSet>?)
        mLineGraph.data = data
        mLineGraph.animateX(2000)
        mLineGraph.invalidate()
        mLineGraph.legend.isEnabled = false
        mLineGraph.description.isEnabled = false
        /*
        setContentView(R.layout.activity_barchart)
        setTitle("BarChartActivityMultiDataset")
        tvX = findViewById(R.id.tvXMax)
        tvX!!.textSize = 10f
        tvY = findViewById(R.id.tvYMax)
        seekBarX = findViewById(R.id.seekBar1)
        seekBarX!!.max = 5
        //seekBarX!!.receiveContentMimeTypes(colors_1)
        seekBarX!!.setOnSeekBarChangeListener(this)
        seekBarY = findViewById(R.id.seekBar2)
        seekBarY!!.setOnSeekBarChangeListener(this)
        chart = findViewById(R.id.chart1)
        chart!!.setOnChartValueSelectedListener(this)
        chart!!.description.isEnabled = false

//        chart.setDrawBorders(true);

        // scaling can now only be done on x- and y-axis separately
        chart!!.setPinchZoom(false)
        chart!!.setDrawBarShadow(false)
        chart!!.setDrawGridBackground(false)

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it
        val mv = MyMarkerView(this, R.layout.custom_marker_view)
        mv.setChartView(chart) // For bounds control
        chart!!.marker = mv // Set the marker to the chart
        seekBarX!!.progress = 1
        seekBarY!!.progress = 100
        val l = chart!!.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(true)
        l.typeface = SANS_SERIF
        l.yOffset = 0f
        l.xOffset = 10f
        l.yEntrySpace = 0f
        l.textSize = 8f
        val xAxis = chart!!.xAxis
        if (colors_1 != null) {
            xAxis.mEntryCount = colors_1.size
        }
        xAxis.typeface = SANS_SERIF
       // xAxis.granularity = 1f
        xAxis.setCenterAxisLabels(true)
        xAxis.setValueFormatter(IndexAxisValueFormatter(colors_1))
        val leftAxis = chart!!.axisLeft
        leftAxis.typeface = SANS_SERIF
        leftAxis.valueFormatter = LargeValueFormatter()
        leftAxis.setDrawGridLines(false)
        leftAxis.spaceTop = 35f
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
        chart!!.axisRight.isEnabled = false
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        val groupSpace = 0.08f
        val barSpace = 0.03f // x4 DataSet
        val barWidth = 0.2f // x4 DataSet
        // (0.2 + 0.03) * 4 + 0.08 = 1.00 -> interval per "group"
        val groupCount = seekBarX!!.progress + 1
        val startYear = 1900
        val endYear = startYear + groupCount
        tvX!!.text = String.format(Locale.ENGLISH, "%d-%d", startYear, endYear)
        tvY!!.text = seekBarY!!.progress.toString()
        //wartości tworzące słupki
        val values1 = ArrayList<BarEntry>()
        val values2 = ArrayList<BarEntry>()
        val values3 = ArrayList<BarEntry>()
        val values4 = ArrayList<BarEntry>()
        val randomMultiplier = seekBarY!!.progress * 100000f
        //for (i in startYear until endYear) {
        for (i in 0 until colors_1!!.size) {
            values1.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
            values2.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
            values3.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
            values4.add(BarEntry(i.toFloat(), (Math.random() * randomMultiplier).toFloat()))
        }
        val set1: BarDataSet
        val set2: BarDataSet
        val set3: BarDataSet
        val set4: BarDataSet
        if (chart!!.data != null && chart!!.data.dataSetCount > 0) {
            set1 = chart!!.data.getDataSetByIndex(0) as BarDataSet
            set2 = chart!!.data.getDataSetByIndex(1) as BarDataSet
            set3 = chart!!.data.getDataSetByIndex(2) as BarDataSet
            set4 = chart!!.data.getDataSetByIndex(3) as BarDataSet
            set1.values = values1
            set2.values = values2
            set3.values = values3
            set4.values = values4
            chart!!.data.notifyDataChanged()
            chart!!.notifyDataSetChanged()
        } else {
            // create 4 DataSets
            set1 = BarDataSet(values1, "Company A")
            set1.color = Color.rgb(104, 241, 175)
            set2 = BarDataSet(values2, "Company B")
            set2.color = Color.rgb(164, 228, 251)
            set3 = BarDataSet(values3, "Company C")
            set3.color = Color.rgb(242, 247, 158)
            set4 = BarDataSet(values4, "Company D")
            set4.color = Color.rgb(255, 102, 0)
            val data = BarData(set1, set2, set3, set4)
            data.setValueFormatter(LargeValueFormatter())
            //data.setValueFormatter(IndexAxisValueFormatter(colors_1))
            data.setValueTypeface(SANS_SERIF)
            chart!!.data = data
        }

        // specify the width each bar should have
        chart!!.barData.barWidth = barWidth

        // restrict the x-axis range
        chart!!.xAxis.axisMinimum = startYear.toFloat()

        // barData.getGroupWith(...) is a helper that calculates the width each group needs based on the provided parameters
        chart!!.xAxis.axisMaximum =
            startYear + chart!!.barData.getGroupWidth(groupSpace, barSpace) * groupCount
        chart!!.groupBars(startYear.toFloat(), groupSpace, barSpace)
        chart!!.invalidate()*/
    }
/*
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.data =
                    Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/BarChartActivityMultiDataset.java")
                startActivity(i)
            }
            R.id.actionToggleValues -> {
                for (set in chart!!.data.dataSets) set.setDrawValues(!set.isDrawValuesEnabled)
                chart!!.invalidate()
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
                for (set in chart!!.data.dataSets) (set as BarDataSet).barBorderWidth =
                    if (set.getBarBorderWidth() == 1f) 0f else 1f
                chart!!.invalidate()
            }
            R.id.actionToggleHighlight -> {
                if (chart!!.data != null) {
                    chart!!.data.isHighlightEnabled = !chart!!.data.isHighlightEnabled
                    chart!!.invalidate()
                }
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
            R.id.animateX -> {
                chart!!.animateX(2000)
            }
            R.id.animateY -> {
                chart!!.animateY(2000)
            }
            R.id.animateXY -> {
                chart!!.animateXY(2000, 2000)
            }
        }
        return true
    }*/
private fun getIncomeEntries(): MutableList<BarEntry> {
    val incomeEntries: ArrayList<BarEntry> = ArrayList()
    incomeEntries.add(BarEntry(1F, 11300F))
    incomeEntries.add(BarEntry(2F, 1390F))
    incomeEntries.add(BarEntry(3F, 1190F))
    incomeEntries.add(BarEntry(4F, 7200F))
    incomeEntries.add(BarEntry(5F, 4790F))
    incomeEntries.add(BarEntry(6F, 4500F))
    incomeEntries.add(BarEntry(7F, 8000F))
    incomeEntries.add(BarEntry(8F, 7034F))
    incomeEntries.add(BarEntry(9F, 4307F))
    incomeEntries.add(BarEntry(10F, 8762F))
    incomeEntries.add(BarEntry(11F, 4355F))
    incomeEntries.add(BarEntry(12F, 6000F))
    return incomeEntries.subList(0, 12)
}
    /*
    override fun onStartTrackingTouch(seekBar: SeekBar) {}
    override fun onStopTrackingTouch(seekBar: SeekBar) {}
    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.dataSetIndex)
    }

    override fun onNothingSelected() {
        Log.i("Activity", "Nothing selected.")
    }*/
}
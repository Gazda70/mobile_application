package pl.polsl.peoplecounter.statistics_presentation

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import pl.polsl.peoplecounter.R
import pl.polsl.peoplecounter.datatypes.DetectionDate
import pl.polsl.peoplecounter.web_services.APIDataProvider
import retrofit2.Call
import retrofit2.Callback

class StatisticPresentation : Fragment() {

    private lateinit var chart: BarChart
    val groupSpace = 0.08f
    val barSpace = 0.01f
    val barWidth = 0.3f

    private lateinit var detectionDate: DetectionDate

    private val detectionPeriodNames: ArrayList<String> = arrayListOf<String>()

    companion object {
        fun newInstance() = StatisticPresentation()
    }

    private lateinit var viewModel: StatisticPresentationViewModel

    private lateinit var data: BarData

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val infl = inflater.inflate(R.layout.statistic_presentation_fragment, container, false)
        chart = infl.findViewById<BarChart>(R.id.bar_chart)
        setFragmentResultListener("date_for_statistics_display") { key, bundle ->
            detectionDate = DetectionDate(bundle.getString("year")!!,
                bundle.getString("month")!!,
                bundle.getString("day")!!)
            getDetectionStatistics()
        }
        return infl
    }

    private fun configureChartAppearance() {
        chart.getDescription().setEnabled(false)
        chart.setDrawValueAboveBar(false)
        val xAxis: XAxis = chart.getXAxis()
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                if(value.toInt() >= detectionPeriodNames.size){
                    return ""
                }
                return detectionPeriodNames[value.toInt()]
            }
        }
        xAxis.textSize = 1.0f
        val axisLeft: YAxis = chart.getAxisLeft()
        axisLeft.granularity = 5f
        axisLeft.axisMinimum = 0f
        val axisRight: YAxis = chart.getAxisRight()
        axisRight.granularity = 5f
        axisRight.axisMinimum = 0f
    }

    private fun createChartData(detectionData: JSONObject): BarData? {
        val mins = arrayListOf<Int>()
        val maxes = arrayListOf<Int>()
        val averages = arrayListOf<Int>()

        for(idx in
        0 until(detectionData["detection_period_stats"]
                as JSONArray).length()){
            val element = (detectionData["detection_period_stats"] as JSONArray)[idx] as JSONObject
            mins.add(element["people_min"].toString().toInt())
            maxes.add(element["people_max"].toString().toInt())
            averages.add(element["people_avg"].toString().toInt())
            detectionPeriodNames.add(element["start_time"].toString()
            + "-" + element["end_time"].toString())
        }

        mins.add((detectionData["whole_day_stats"] as JSONObject)["people_min"].toString().toInt())
        maxes.add((detectionData["whole_day_stats"] as JSONObject)["people_max"].toString().toInt())
        averages.add((detectionData["whole_day_stats"] as JSONObject)["people_avg"].toString().toInt())
        detectionPeriodNames.add("Whole day")
        detectionPeriodNames.add("")


        val entriesGroup1:ArrayList<BarEntry> = ArrayList()
        val entriesGroup2:ArrayList<BarEntry> = ArrayList()
        val entriesGroup3:ArrayList<BarEntry> = ArrayList()
        for(i in mins.indices) {
            entriesGroup1.add( i, BarEntry(i.toFloat(), mins.get(i).toFloat()))
        }
        for(i in maxes.indices) {
            entriesGroup2.add(i, BarEntry(i.toFloat(), maxes.get(i).toFloat()))
        }
        for(i in averages.indices) {
            entriesGroup3.add(i, BarEntry(i.toFloat(), averages.get(i).toFloat()))
        }
        entriesGroup1.add(averages.size, BarEntry(averages.size + 1.toFloat(), 0f))
        val set1: BarDataSet = BarDataSet(entriesGroup1, "Minimal people count")
        val set2: BarDataSet = BarDataSet(entriesGroup2, "Maximal people count")
        val set3: BarDataSet = BarDataSet(entriesGroup3, "Average people count")
        set1.color = Color.YELLOW
        set2.color = Color.RED
        set3.color = Color.BLUE
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        dataSets.add(set2)
        dataSets.add(set3)
        val data = BarData(dataSets)
        data.barWidth = barWidth
        return data
    }

    private fun prepareChartData(data: BarData) {
        data.setValueTextSize(12f)
        chart.data = data
        chart.groupBars(0f, groupSpace, barSpace)
        chart.invalidate()
    }

    private fun getDetectionStatistics(){
        val gson = Gson()
        val jsonObj = JsonObject()
        jsonObj.addProperty("mode", "single_day")
        jsonObj.addProperty("startDate", gson.toJson(detectionDate))
        val response = APIDataProvider.service.getDetectionStatistics(jsonObj).enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                        response.body()
                            ?.string()
                    )
                    val jsonResponse = JSONObject(prettyJson.substring(1,
                        prettyJson.length - 1).replace("\\", ""))
                    data = createChartData(jsonResponse)!!
                    configureChartAppearance()
                    prepareChartData(data)
                }
            }
        })
    }
}
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

    private val MAX_X_VALUE = 7
    private val MAX_Y_VALUE = 50
    private val MIN_Y_VALUE = 5
    private val SET_LABEL = "App Downloads"
    private lateinit var chart: BarChart
    val groupSpace = 0.06f
    val barSpace = 0.02f // x2 dataset

    val barWidth = 0.45f // x2 dataset

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
            // Any type can be passed via to the bundle
            detectionDate = DetectionDate(bundle.getString("year")!!,
                bundle.getString("month")!!,
                bundle.getString("day")!!)
            // Do something with the result...
            Log.i("DATE FROM LISTENER", "DETECTION DATE " + detectionDate)
            getDetectionStatistics()
        }
        return infl
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(StatisticPresentationViewModel::class.java)
        // TODO: Use the ViewModel
    }
    private fun configureChartAppearance() {
        chart.getDescription().setEnabled(false)
        chart.setDrawValueAboveBar(false)
        val xAxis: XAxis = chart.getXAxis()
        xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                return detectionPeriodNames[value.toInt()]
            }
        }
        val axisLeft: YAxis = chart.getAxisLeft()
        axisLeft.granularity = 10f
        axisLeft.axisMinimum = 0f
        val axisRight: YAxis = chart.getAxisRight()
        axisRight.granularity = 10f
        axisRight.axisMinimum = 0f
    }

    private fun createChartData(detectionData: JSONObject): BarData? {
        /*val values: ArrayList<BarEntry> = ArrayList()
        for (i in 0 until MAX_X_VALUE) {
            val x = i.toFloat()
            val y: Float = Random.nextFloat() % (MAX_Y_VALUE - MIN_Y_VALUE) + MIN_Y_VALUE

            values.add(BarEntry(x, y))
        }
        val set1 = BarDataSet(values, SET_LABEL)
        val dataSets: ArrayList<IBarDataSet> = ArrayList()
        dataSets.add(set1)
        return BarData(dataSets)*/
        /*Log.i("DETECTION DATA: ",
    ((jsonResponse["detection_period_stats"] as JSONArray)[0]
            as JSONObject)["start_time"].toString())*/
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


        val entriesGroup1:ArrayList<BarEntry> = ArrayList()
        val entriesGroup2:ArrayList<BarEntry> = ArrayList()
        val entriesGroup3:ArrayList<BarEntry> = ArrayList()
// fill the lists
        for(i in mins.indices) {
            entriesGroup1.add(i, BarEntry(i.toFloat(), mins.get(i).toFloat()))
            entriesGroup2.add(i, BarEntry(i.toFloat(), maxes.get(i).toFloat()))
            entriesGroup3.add(i, BarEntry(i.toFloat(), averages.get(i).toFloat()))
        }

        val set1: BarDataSet = BarDataSet(entriesGroup1, "Minimal people count")
        val set2: BarDataSet = BarDataSet(entriesGroup2, "Maximal people count")
        val set3: BarDataSet = BarDataSet(entriesGroup2, "Average people count")
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
                Log.i("Error", "My error")
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
                    /*Log.i("DETECTION DATA: ",
                        ((jsonResponse["detection_period_stats"] as JSONArray)[0]
                                as JSONObject)["start_time"].toString())*/
                    data = createChartData(jsonResponse)!!
                    configureChartAppearance()
                    prepareChartData(data)
                }
            }
        })
    }
}
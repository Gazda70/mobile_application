package pl.polsl.peoplecounter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class StatisticsPresentationActivity : AppCompatActivity() {
    val detectionsDurationChartData = ArrayList<Entry>()

    val detectionPersonPerFramerChartData = ArrayList<Entry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_statistics_presentation)
        getDetectionStatistics()

        val backButton = findViewById<Button>(R.id.backButton)
        //postRequest(resultButton)

        backButton.setOnClickListener {
            try {
                val k = Intent(this@StatisticsPresentationActivity, MainActivity::class.java)
                startActivity(k)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun initializeDetectionsDurationChart(detectionData: JSONArray){
        for(i in 0..detectionData.length() - 1) {
            Log.i("DETECTION DATA" + i.toString(), detectionData[i].toString())
            detectionsDurationChartData.add(
                Entry(i.toFloat(),
                    (detectionData[i] as JSONObject)["secondsOfDetection"]
                        .toString().toFloat())
            )
        }
    }

    fun initializeDetectionsPersonNumberChart(detectionData: JSONArray){
        var lastDetection = detectionData[detectionData.length() - 1]
        val detFrames = (lastDetection as JSONObject)["detections"] as JSONArray
        for(i in 0..detFrames.length() - 1) {
            detectionPersonPerFramerChartData.add(Entry(
                i.toFloat(),
                detFrames[i].toString()
                    .split('=')[3]
                    .split('{').size.toFloat() - 1.0f)
            )
        }
    }

    fun createDetectionsDurationChart(){
        val vl = LineDataSet(detectionsDurationChartData, "Czas trwania detekcji [s]")

//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.purple_200
        vl.fillAlpha = R.color.white

        val lineChart = findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.detectionsDurationChart)
//Part5
        lineChart.xAxis.labelRotationAngle = 0f

//Part6
        lineChart.data = LineData(vl)

//Part7
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.axisMaximum = 10.0f + 0.1f

//Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

//Part9
        lineChart.description.text = "Numer próby"
        lineChart.setNoDataText("Brak danych")

//Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
        /*val markerView = CustomMarker(this@ShowForexActivity, R.layout.marker_view)
        lineChart.marker = markerView*/
    }

    fun createDetectionPersonPerFramerChartData(){
        val vl = LineDataSet(detectionPersonPerFramerChartData, "Ilość osób na klatkę obrazu")

//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.purple_200
        vl.fillAlpha = R.color.white

        val lineChart = findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.detectionPersonPerFramerChart)
//Part5
        lineChart.xAxis.labelRotationAngle = 0f

//Part6
        lineChart.data = LineData(vl)

//Part7
        lineChart.axisRight.isEnabled = false
        lineChart.xAxis.axisMaximum = 10.0f + 0.1f

//Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

//Part9
        lineChart.description.text = "Klatka detekcji"
        lineChart.setNoDataText("Brak danych")

//Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
        /*val markerView = CustomMarker(this@ShowForexActivity, R.layout.marker_view)
        lineChart.marker = markerView*/
    }
    fun getDetectionStatistics(){
        val response = APIDataProvider.service.getDetectionStatistics().enqueue(object :
            Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                val toast = Toast.makeText(applicationContext,
                    "Could not retrieve detection data!", Toast.LENGTH_SHORT)
                toast.show()
            }
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson =
                        response.body()
                            ?.string()
                    Log.i("JSON", prettyJson?:"nonne")
                    initializeDetectionsDurationChart(JSONArray(prettyJson?.replace("\n", "")))
                    createDetectionsDurationChart()
                    initializeDetectionsPersonNumberChart(JSONArray(prettyJson))
                    createDetectionPersonPerFramerChartData()
                }
            }

        })
    }
}
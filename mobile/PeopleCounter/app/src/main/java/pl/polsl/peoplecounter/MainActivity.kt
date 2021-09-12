package pl.polsl.peoplecounter

import android.graphics.Color.red
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import okhttp3.ResponseBody
import pl.polsl.peoplecounter.APIDataProvider.Companion.service

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var resultButton = findViewById<TextView>(R.id.resultField)
        postRequest(resultButton)

        //Part1
        val entries = ArrayList<Entry>()

//Part2
        entries.add(Entry(1f, 10f))
        entries.add(Entry(2f, 2f))
        entries.add(Entry(3f, 7f))
        entries.add(Entry(4f, 20f))
        entries.add(Entry(5f, 16f))

//Part3
        val vl = LineDataSet(entries, "My Type")

//Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
        vl.fillColor = R.color.purple_200
        vl.fillAlpha = R.color.white

        val lineChart = findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)
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
        lineChart.description.text = "Days"
        lineChart.setNoDataText("No forex yet!")

//Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

//Part11
        /*val markerView = CustomMarker(this@ShowForexActivity, R.layout.marker_view)
        lineChart.marker = markerView*/
    }

    fun postRequest(resultButton:TextView){
//  POST demo
        val jsonObj = JsonObject()
        jsonObj.addProperty("title", "rhythm")
        jsonObj.addProperty("singer", "meee")
        jsonObj.addProperty("text", "Jack and jill went up the hill to fetch a pail of water!")

        // Convert JSONObject to String
       /* val jsonObjectString = jsonObj.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())*/

        CoroutineScope(Dispatchers.IO).launch {
            val response = APIDataProvider.service.getVectors(jsonObj).execute()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )

                    Log.d("Pretty Printed JSON :", prettyJson)
                    resultButton.setText(prettyJson)
                } else {

                    Log.e("RETROFIT_ERROR", response.toString())
                    resultButton.setText(response.code().toString())
                }
            }
        }
            /*.enqueue(object : Callback<ResponseBody!>! {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("---TTTT :: POST Throwable EXCEPTION:: " + t.message)
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.string()
                        println("---TTTT :: POST msg from server :: " + msg)
                        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })*/
    }

    fun getRequest(resultButton:TextView){
        CoroutineScope(Dispatchers.IO).launch {
            /*
             * For @Query: You need to replace the following line with val response = service.getEmployees(2)
             * For @Path: You need to replace the following line with val response = service.getEmployee(53)
             */

            // Do the GET request and get response
            val response = service.greetUser().execute()

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {

                    // Convert raw JSON to pretty JSON using GSON library
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val prettyJson = gson.toJson(
                            response.body()
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                    )
                    resultButton.setText(prettyJson)
                    Log.d("Pretty Printed JSON :", prettyJson)

                } else {
                    resultButton.setText(response.code().toString())
                    Log.e("RETROFIT_ERROR", response.code().toString())

                }
            }
        }
        /*APIDataProvider
            .service
            .greetUser("Audhil")
            .enqueue(object : Callback{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    println("---TTTT :: GET Throwable EXCEPTION:: " + t.message)
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.isSuccessful) {
                        val msg = response.body()?.string()
                        println("---TTTT :: GET msg from server :: " + msg)
                        Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            })*/
    }
}
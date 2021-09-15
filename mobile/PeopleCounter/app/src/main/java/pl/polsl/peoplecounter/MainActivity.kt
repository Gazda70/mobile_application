package pl.polsl.peoplecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Response
import okhttp3.ResponseBody
import pl.polsl.peoplecounter.APIDataProvider.Companion.service
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception
import java.lang.Integer.parseInt
import java.util.*
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.hours
import kotlin.time.minutes


class MainActivity : AppCompatActivity() {

    private var numberOfSecondsForDetection = 0


    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewStatisticsButton = findViewById<Button>(R.id.viewStatisticsButton)
        //postRequest(resultButton)

        viewStatisticsButton.setOnClickListener {
            try {
                val k = Intent(this@MainActivity, StatisticsPresentationActivity::class.java)
                startActivity(k)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val startDetectionButton = findViewById<Button>(R.id.startDetectionButton)

        val detectionTimeString = findViewById<TextInputEditText>(R.id.detectionDurationInput)

        startDetectionButton.setOnClickListener {
            if(processTimeString(detectionTimeString.text.toString())){
                startDetectionRequest()
                val toast = Toast.makeText(applicationContext,
                    "Detection has started!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

    }

    @ExperimentalTime
    fun processTimeString(givenTime:String): Boolean{
        /*val re = Regex("/^(?:([01]?|2[0-3]):([0-5]?))?\$/")
        if(re.matches(givenTime)){*/
            val hhMM = givenTime.split(':')

        val rightNow = Calendar.getInstance()
        val currentHourIn24Format =
            rightNow[Calendar.HOUR_OF_DAY] // return the hour in 24 hrs format (ranging from 0-23)
        val currentMinute =
            rightNow[Calendar.MINUTE] // return the hour in 24 hrs format (ranging from 0-23)

        Log.i("parseInt(hhMM[0], 10)", parseInt(hhMM[0], 10).toString())
            Log.i("currentdate.time.hours.inWholeHours", currentHourIn24Format.toString())
            val resultHours = parseInt(hhMM[0], 10) - currentHourIn24Format
            var resultMinutes = parseInt(hhMM[1], 10) - currentMinute
            if (resultHours < 0){
                val toast = Toast.makeText(applicationContext,
                    "You can't setup detection for time in past. Increase hour !", Toast.LENGTH_SHORT)
                toast.show()
                return false
            }
            if(resultMinutes < 0){
                if(resultHours == 0){
                    val toast = Toast.makeText(applicationContext,
                        "You can't setup detection for time in past. Increase minutes !", Toast.LENGTH_SHORT)
                    toast.show()
                    return false
                }
                resultMinutes = 60 - resultMinutes;
            }
            this.calculateSecondsForDetection(resultHours.toInt(), resultMinutes.toInt());
            return true
        /*}else{
            val toast = Toast.makeText(applicationContext,
                "You need to put detection end time in the format of HH:MM !", Toast.LENGTH_SHORT)
            toast.show()
            return false
        }*/
        /*val hhMM = givenTime.split(':')
        if(hhMM.size == 2){
            val regCheck = Regex("/^(?:([01]?\\d|2[0-3]):([0-5]?\\d))?\$/")
            if(regCheck.matches(hhMM[0]) && regCheck.matches(hhMM[1])){
                numberOfSeconds = hhMM[0].toInt() * 3600 +
            }
        }*/
    }

    fun calculateSecondsForDetection(hours: Int, minutes:Int){
        this.numberOfSecondsForDetection = hours * 3600 + minutes * 60;
    }

    fun startDetectionRequest(){
//  POST demo
        val jsonObj = JsonObject()
        jsonObj.addProperty("networkType", "CUSTOM")
        jsonObj.addProperty("numberOfSecondsForDetection", this.numberOfSecondsForDetection)
        jsonObj.addProperty("objThreshold", "0.3")
        jsonObj.addProperty("iouThreshold", "0.1")

        // Convert JSONObject to String
       /* val jsonObjectString = jsonObj.toString()

        // Create RequestBody ( We're not using any converter, like GsonConverter, MoshiConverter e.t.c, that's why we use RequestBody )
        val requestBody = jsonObjectString.toRequestBody("application/json".toMediaTypeOrNull())*/

            val response = APIDataProvider.service.setupNewDetection(jsonObj).enqueue(object :
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
                                ?.string() // About this thread blocking annotation : https://github.com/square/retrofit/issues/3255
                        )
                    }
                }

            })
            /*CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.Main) {
                }
            }*/
    }
}
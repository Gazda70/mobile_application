package pl.polsl.peoplecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import pl.polsl.peoplecounter.web_services.APIDataProvider
import retrofit2.Call
import retrofit2.Callback
import kotlin.time.ExperimentalTime


class MainActivity : AppCompatActivity() {

    private var numberOfSecondsForDetection = 0


    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val viewStatisticsButton = findViewById<Button>(R.id.viewStatisticsButton)
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
        }*/

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
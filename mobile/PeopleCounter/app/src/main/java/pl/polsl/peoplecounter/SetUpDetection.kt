package pl.polsl.peoplecounter

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.lang.Exception
import java.util.*
import kotlin.time.ExperimentalTime

class SetUpDetection : Fragment() {

    private var numberOfSecondsForDetection = 0

    companion object {
        fun newInstance() = SetUpDetection()
    }

    private lateinit var viewModel: SetUpDetectionViewModel

    @ExperimentalTime
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.set_up_detection_fragment, container, false)
        val viewStatisticsButton = infl.findViewById<Button>(R.id.viewStatisticsButton)
        //postRequest(resultButton)

        viewStatisticsButton.setOnClickListener {
            infl.findNavController().navigate(R.id.action_setUpDetection_to_statisticsPresentationActivity)
            /*try {
                val k = Intent(context, StatisticsPresentationActivity::class.java)
                startActivity(k)
            } catch (e: Exception) {
                e.printStackTrace()
            }*/
        }

        val startDetectionButton = infl.findViewById<Button>(R.id.startDetectionButton)

        startDetectionButton.setOnClickListener {
            infl.findNavController().navigate(R.id.action_setUpDetection_to_calendarFragment)
            /*if(processTimeString(detectionTimeString.text.toString())){
                startDetectionRequest()
                val toast = Toast.makeText(context,
                    "Detection has started!", Toast.LENGTH_SHORT)
                toast.show()
            }*/
        }
        return infl
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SetUpDetectionViewModel::class.java)
        // TODO: Use the ViewModel

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
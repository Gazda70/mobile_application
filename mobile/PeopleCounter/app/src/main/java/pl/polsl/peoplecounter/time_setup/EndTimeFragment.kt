package pl.polsl.peoplecounter.time_setup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import pl.polsl.peoplecounter.web_services.APIDataProvider
import pl.polsl.peoplecounter.R
import pl.polsl.peoplecounter.datatypes.DetectionDate
import pl.polsl.peoplecounter.datatypes.DetectionTime
import retrofit2.Call
import retrofit2.Callback
import java.time.Duration

class EndTimeFragment : Fragment() {

    private lateinit var detectionDate: DetectionDate

    private lateinit var  detectionStartTime: DetectionTime

    private lateinit var  detectionEndTime: DetectionTime

    companion object {
        fun newInstance() = EndTimeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("detection_date") { key, bundle ->
            // Any type can be passed via to the bundle
            detectionDate = DetectionDate(bundle.getString("year")!!, bundle.getString("month")!!, bundle.getString("day")!!)
            // Do something with the result...
            Log.i("DATE FROM LISTENER", "DETECTION DATE " + detectionDate)
        }
        setFragmentResultListener("detection_start_time") { key, bundle ->
            // Any type can be passed via to the bundle
            detectionStartTime = DetectionTime(bundle.getString("hour")!!, bundle.getString("minute")!!)
            // Do something with the result...
            Log.i("START TIME FROM LISTENER", "START TIME " + detectionStartTime)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.end_time_fragment, container, false)
        val sendDetectionRequestButton = infl.findViewById<Button>(R.id.send_detection_request_button)

        sendDetectionRequestButton.setOnClickListener {
            //Log.i("DATE", "DETECTION DATE " + dateViewModel.detectionDate)
            //Log.i("START TIME", "DETECTION START TIME " + startTimeViewModel.detectionStartTime.value)
            //Log.i("END TIME", "DETECTION END TIME " + endTimeViewModel.detectionEndTime.value)
            infl.findNavController().navigate(R.id.action_endTimeFragment_to_setUpDetection)
            Toast.makeText(context, "Detection request sent", Toast.LENGTH_LONG).show()
            startDetectionRequest()
        }

        val timePicker = infl.findViewById<TimePicker>(R.id.detection_end_time_time_picker)
        detectionEndTime = DetectionTime(timePicker.hour.toString(), timePicker.minute.toString())

        return infl
    }

    fun startDetectionRequest(){
        //zrobić JSONA
        val gson = Gson()
        val jsonObj = JsonObject()
        jsonObj.addProperty("objThreshold", "0.3")
        jsonObj.addProperty("startDate", gson.toJson(detectionDate))
        jsonObj.addProperty("startTime", gson.toJson(detectionStartTime))
        jsonObj.addProperty("endTime", gson.toJson(detectionEndTime))

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
                            ?.string()
                    )
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
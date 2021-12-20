package pl.polsl.peoplecounter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class EndTimeFragment : Fragment() {

    private lateinit var detectionDate:DetectionDate

    private lateinit var  detectionStartTime:DetectionTime

    private lateinit var  detectionEndTime:DetectionTime

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
        }

        val timePicker = infl.findViewById<TimePicker>(R.id.detection_end_time_time_picker)
        //timePicker.set
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            detectionEndTime = DetectionTime(hourOfDay.toString(), minute.toString())
        }
        return infl
    }

    fun startDetectionRequest(){
        /*
        networkType:string,
                    objThreshold:number,
                    startDate:DetectionDate,
                    endDate:DetectionDate,
                    startTime:DetectionTime,
                    endTime:DetectionTime
        * */
        //zrobić JSONA
        val jsonObj = JsonObject()
        jsonObj.addProperty("objThreshold", "0.3")
        jsonObj.addProperty("startDate", detectionDate.toString())
        jsonObj.addProperty("endDate", detectionDate.toString())
        jsonObj.addProperty("startTime", detectionStartTime.toString())
        jsonObj.addProperty("endTime", detectionEndTime.toString())

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
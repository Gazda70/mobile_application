package pl.polsl.peoplecounter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener

class StartTimeFragment : Fragment() {

    companion object {
        fun newInstance() = StartTimeFragment()
    }

    private val viewModel: StartTimeViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener("requestKey") { key, bundle ->
            // Any type can be passed via to the bundle
            val result = bundle.getString("data")
            // Do something with the result...
            Log.i("DATE FROM LISTENER", "DETECTION DATE " + result)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.start_time_fragment, container, false)
        val goToEndTimeButton = infl.findViewById<Button>(R.id.setup_detection_end_time_button)
        goToEndTimeButton.setOnClickListener {
            infl.findNavController().navigate(R.id.action_startTimeFragment_to_endTimeFragment)
        }
        val timePicker = infl.findViewById<TimePicker>(R.id.detection_start_time_time_picker)
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            viewModel.detectionStartTime.value = DetectionTime(hourOfDay.toString(), minute.toString())
        }
        return infl
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(StartTimeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
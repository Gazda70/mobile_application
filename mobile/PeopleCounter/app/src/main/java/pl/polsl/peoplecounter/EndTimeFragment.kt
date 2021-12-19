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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController

class EndTimeFragment : Fragment() {

    companion object {
        fun newInstance() = EndTimeFragment()
    }

    private val endTimeViewModel: EndTimeViewModel by activityViewModels()
    private val startTimeViewModel: StartTimeViewModel by activityViewModels()
    private val dateViewModel: DateViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.end_time_fragment, container, false)
        val sendDetectionRequestButton = infl.findViewById<Button>(R.id.send_detection_request_button)
        /*dateViewModel.detectionDate.observe(viewLifecycleOwner, Observer {
                detectionDate:DetectionDate ->
            // Update the list UI
            Log.i("DATE", "DETECTION DATE " + detectionDate)
        })*/

        sendDetectionRequestButton.setOnClickListener {
            //Log.i("DATE", "DETECTION DATE " + dateViewModel.detectionDate)
            Log.i("START TIME", "DETECTION START TIME " + startTimeViewModel.detectionStartTime.value)
            Log.i("END TIME", "DETECTION END TIME " + endTimeViewModel.detectionEndTime.value)
        }

        val timePicker = infl.findViewById<TimePicker>(R.id.detection_end_time_time_picker)
        timePicker.setOnTimeChangedListener { view, hourOfDay, minute ->
            endTimeViewModel.detectionEndTime.value = DetectionTime(hourOfDay.toString(), minute.toString())
        }
        return infl
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //viewModel = ViewModelProvider(this).get(EndTimeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
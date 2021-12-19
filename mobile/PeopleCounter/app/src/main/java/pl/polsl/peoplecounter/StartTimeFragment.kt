package pl.polsl.peoplecounter

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController

class StartTimeFragment : Fragment() {

    companion object {
        fun newInstance() = StartTimeFragment()
    }

    private val viewModel: StartTimeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.start_time_fragment, container, false)
        val goToEndTimeButton = infl.findViewById<Button>(R.id.setup_detection_end_time_button)
        goToEndTimeButton.setOnClickListener {
            infl.findNavController().navigate(R.id.action_calendarFragment_to_clockFragment)
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
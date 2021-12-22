package pl.polsl.peoplecounter.time_setup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.fragment.app.setFragmentResult
import pl.polsl.peoplecounter.R

class StartTimeFragment : Fragment() {

    companion object {
        fun newInstance() = StartTimeFragment()
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
            setFragmentResult("detection_start_time", bundleOf("hour" to hourOfDay.toString(), "minute" to minute.toString()))
        }
        return infl
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
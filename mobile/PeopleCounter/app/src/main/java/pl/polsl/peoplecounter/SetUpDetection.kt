package pl.polsl.peoplecounter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import kotlin.time.ExperimentalTime

class SetUpDetection : Fragment() {

    companion object {
        fun newInstance() = SetUpDetection()
    }

    @ExperimentalTime
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.set_up_detection_fragment, container, false)
        val viewStatisticsButton = infl.findViewById<Button>(R.id.viewStatisticsButton)

        viewStatisticsButton.setOnClickListener {
            infl.findNavController().navigate(R.id.action_setUpDetection_to_dateForStatistics)
        }

        val startDetectionButton = infl.findViewById<Button>(R.id.startDetectionButton)

        startDetectionButton.setOnClickListener {
            infl.findNavController().navigate(R.id.action_setUpDetection_to_calendarFragment)
        }
        return infl
    }
}
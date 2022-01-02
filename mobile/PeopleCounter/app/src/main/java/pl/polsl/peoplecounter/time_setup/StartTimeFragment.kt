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
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import pl.polsl.peoplecounter.R
import pl.polsl.peoplecounter.datatypes.DetectionDate
import pl.polsl.peoplecounter.datatypes.DetectionTime
import java.time.LocalDateTime
import java.util.*

class StartTimeFragment : Fragment() {

    private lateinit var detectionDate: DetectionDate

    companion object {
        fun newInstance() = StartTimeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val infl = inflater.inflate(R.layout.start_time_fragment, container, false)
        val goToEndTimeButton = infl.findViewById<Button>(R.id.setup_detection_end_time_button)
        val timePicker = infl.findViewById<TimePicker>(R.id.detection_start_time_time_picker)
        Log.i("TIME", "MY TIME IS:" + timePicker.hour.toString())

        setFragmentResultListener("detection_date") { key, bundle ->
            detectionDate = DetectionDate(bundle.getString("year")!!, bundle.getString("month")!!, bundle.getString("day")!!)
        }

        goToEndTimeButton.setOnClickListener {
            if(checkStartTime(timePicker.hour, timePicker.minute)) {
                setFragmentResult(
                    "detection_start_time",
                    bundleOf(
                        "hour" to timePicker.hour.toString(),
                        "minute" to timePicker.minute.toString()
                    )
                )
                setFragmentResult("detection_date_2", bundleOf("year" to detectionDate.year,
                    "month" to detectionDate.month,
                    "day" to detectionDate.day))
                infl.findNavController().navigate(R.id.action_startTimeFragment_to_endTimeFragment)
            }else{
                Toast.makeText(context, "Start time cannot be in the past !",
                    Toast.LENGTH_LONG).show()
            }
        }
        return infl
    }

    private fun checkStartTime(startHour:Int, startMinute:Int):Boolean{
        val currentDate = LocalDateTime.now()
        Log.i("currentDate", currentDate.year.toString() + " "
        + currentDate.monthValue.toString() + " " + currentDate.dayOfMonth +
        " " + currentDate.hour.toString() + " " + currentDate.minute.toString())
        Log.i("start time", startHour.toString() + " " + startMinute.toString())
        Log.i("detection date", detectionDate.year + " " + detectionDate.month.toInt() +
                " " + detectionDate.day.toInt())
        Log.i("check result", (!(currentDate.year == detectionDate.year.toInt() && currentDate.monthValue==
                detectionDate.month.toInt()
                && currentDate.dayOfMonth == detectionDate.day.toInt() && currentDate.hour > startHour
                && currentDate.minute > startMinute)).toString())
        return !(currentDate.year == detectionDate.year.toInt() && currentDate.monthValue==
                detectionDate.month.toInt()
            && currentDate.dayOfMonth == detectionDate.day.toInt() && (currentDate.hour > startHour
                || currentDate.minute > startMinute && currentDate.hour == startHour))
    }

}
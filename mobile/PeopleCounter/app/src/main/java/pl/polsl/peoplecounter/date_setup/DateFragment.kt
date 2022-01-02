package pl.polsl.peoplecounter.date_setup

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import pl.polsl.peoplecounter.R
import android.text.format.DateFormat
import java.util.*

class DateFragment : Fragment() {

    companion object {
        fun newInstance() = DateFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val infl = inflater.inflate(R.layout.date_fragment, container, false)
        val goToTimeButton = infl.findViewById<Button>(R.id.got_to_clock_button)
        val calendarView = infl.findViewById<CalendarView>(R.id.set_detection_date_calendar)
        val calender: Calendar = Calendar.getInstance()
        calendarView.minDate = calender.timeInMillis
        calendarView.date = calender.timeInMillis
        goToTimeButton.setOnClickListener {
            val dateMillis: Long = calendarView.date
            val date: Date = Date(dateMillis)
            val selectedDay = DateFormat.format("dd", date) as String
            val selectedYear = DateFormat.format("yyyy", date) as String
            val selectedMonthNumber = DateFormat.format("MM", date) as String
            setFragmentResult("detection_date", bundleOf("year" to selectedYear,
                "month" to selectedMonthNumber, "day" to selectedDay))

            infl.findNavController().navigate(R.id.action_calendarFragment_to_startTimeFragment)
        }
        return infl
    }
}
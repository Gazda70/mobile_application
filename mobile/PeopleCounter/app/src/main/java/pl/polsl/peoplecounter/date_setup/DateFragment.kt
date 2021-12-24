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
import pl.polsl.peoplecounter.datatypes.DetectionDate
import android.text.format.DateFormat
import android.util.Log
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
        goToTimeButton.setOnClickListener {
            // Fetch long milliseconds from calenderView.
            val dateMillis: Long = calendarView.date

            // Create Date object from milliseconds.
            val date: Date = Date(dateMillis)

            // Get Date values and created formatted string date to show in Toast.
            val selectedDay = DateFormat.format("dd", date) as String // 05
            val selectedMonthString = DateFormat.format("MMM", date) as String // Jul
            val selectedYear = DateFormat.format("yyyy", date) as String // 2021
            val selectedMonthNumber = DateFormat.format("MM", date) as String
            val strFormattedSelectedDate = "$selectedDay-$selectedMonthString-$selectedYear"
            Log.i("DATE", "MY DATE IS:" + strFormattedSelectedDate)
            setFragmentResult("detection_date", bundleOf("year" to selectedYear,
                "month" to DetectionDate.formatMonthNumberToLiteralShortcut(selectedMonthNumber.toInt()),
                "day" to selectedDay))

            infl.findNavController().navigate(R.id.action_calendarFragment_to_startTimeFragment)
        }

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Create calender object with which will have system date time.
            val calender: Calendar = Calendar.getInstance()

            // Set attributes in calender object as per selected date.
            calender.set(year, month, dayOfMonth)

            // Now set calenderView with this calender object to highlight selected date on UI.
            calendarView.setDate(calender.timeInMillis, true, true)
            //Log.i("DATE", "MY DATE IS:" + viewModel.detectionDate.toString())
        }
        return infl
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
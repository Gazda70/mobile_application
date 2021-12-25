package pl.polsl.peoplecounter.statistics_presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.TimePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import pl.polsl.peoplecounter.R
import pl.polsl.peoplecounter.datatypes.DetectionDate
import pl.polsl.peoplecounter.time_setup.StartTimeFragment
import java.util.*

class DateForStatistics : Fragment() {

    companion object {
        fun newInstance() = StartTimeFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val infl = inflater.inflate(R.layout.date_for_statistics_fragment, container, false)
        val goToStatisticsPresentationActivityButton = infl.findViewById<Button>(R.id.got_to_statistics_button)
        val calendarView = infl.findViewById<CalendarView>(R.id.set_day_for_statistics_display_calendar)
        goToStatisticsPresentationActivityButton.setOnClickListener {
            // Fetch long milliseconds from calenderView.
            val dateMillis: Long = calendarView.date

            // Create Date object from milliseconds.
            val date: Date = Date(dateMillis)

            // Get Date values and created formatted string date to show in Toast.
            val selectedDay = DateFormat.format("dd", date) as String // 05
            val selectedMonthString = DateFormat.format("MMM", date) as String // Jul
            val selectedMonthNumber = DateFormat.format("MM", date) as String // 6 --> Month Code as Jan = 0 till Dec = 11
            val selectedYear = DateFormat.format("yyyy", date) as String // 2021

            val strFormattedSelectedDate = "$selectedDay-$selectedMonthString-$selectedYear"
            Log.i("DATE", "MY DATE IS:" + strFormattedSelectedDate)
            setFragmentResult("date_for_statistics_display", bundleOf("year" to selectedYear,
                "month" to DetectionDate.formatMonthNumberToLiteralShortcut(selectedMonthNumber.toInt()),
                "day" to selectedDay))
            infl.findNavController().navigate(R.id.action_dateForStatistics_to_statisticPresentation)
        }
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Create calender object with which will have system date time.
            val calender: Calendar = Calendar.getInstance()

            // Set attributes in calender object as per selected date.
            calender.set(year, month, dayOfMonth)

            // Now set calenderView with this calender object to highlight selected date on UI.
            calendarView.setDate(calender.timeInMillis, true, true)
        }

        return infl
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
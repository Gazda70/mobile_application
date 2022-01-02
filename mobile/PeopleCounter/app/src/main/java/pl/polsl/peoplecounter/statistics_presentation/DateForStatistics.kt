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
            val dateMillis: Long = calendarView.date
            val date: Date = Date(dateMillis)
            val selectedDay = DateFormat.format("dd", date) as String
            val selectedMonthNumber = DateFormat.format("MM", date) as String
            val selectedYear = DateFormat.format("yyyy", date) as String
            setFragmentResult("date_for_statistics_display", bundleOf("year" to selectedYear,
                "month" to DetectionDate.formatMonthNumberToLiteralShortcut(selectedMonthNumber.toInt()),
                "day" to selectedDay))
            infl.findNavController().navigate(R.id.action_dateForStatistics_to_statisticPresentation)
        }
        return infl
    }
}
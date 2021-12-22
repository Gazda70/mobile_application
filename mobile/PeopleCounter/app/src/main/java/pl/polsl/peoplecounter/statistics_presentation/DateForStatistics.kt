package pl.polsl.peoplecounter.statistics_presentation

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
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
import pl.polsl.peoplecounter.time_setup.StartTimeFragment

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
        goToStatisticsPresentationActivityButton.setOnClickListener {
            infl.findNavController().navigate(R.id.action_dateForStatistics_to_statisticsPresentationActivity)
        }

        val calendarView = infl.findViewById<CalendarView>(R.id.set_day_for_statistics_display_calendar)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            setFragmentResult("date_for_statistics_display", bundleOf("year" to year.toString(),
                "month" to formatMonthNumberToLiteralShortcut(month),
                "day" to dayOfMonth.toString()))
            //Log.i("DATE", "MY DATE IS:" + viewModel.detectionDate.toString())
        }

        return infl
    }

    fun formatMonthNumberToLiteralShortcut(numberMonth:Int):String{
        return when(numberMonth){
            1 -> "Jan"
            2 -> "Feb"
            3 -> "Mar"
            4 -> "Apr"
            5 -> "May"
            6 -> "Jun"
            7 -> "Jul"
            8 -> "Aug"
            9 -> "Sep"
            10 -> "Oct"
            11 -> "Nov"
            12 -> "Dec"
            else -> "Err"
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
    }

}
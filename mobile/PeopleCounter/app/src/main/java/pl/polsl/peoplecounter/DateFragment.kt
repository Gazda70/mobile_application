package pl.polsl.peoplecounter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.findNavController
import androidx.navigation.navGraphViewModels

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
        goToTimeButton.setOnClickListener {
            infl.findNavController().navigate(R.id.action_calendarFragment_to_startTimeFragment)
        }

        val calendarView = infl.findViewById<CalendarView>(R.id.set_detection_date_calendar)
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            setFragmentResult("detection_date", bundleOf("year" to year.toString(),
                "month" to formatMonthNumberToLiteralShortcut(month),
                "day" to dayOfMonth.toString()))
            //Log.i("DATE", "MY DATE IS:" + viewModel.detectionDate.toString())
        }
        return infl
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // TODO: Use the ViewModel
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

}
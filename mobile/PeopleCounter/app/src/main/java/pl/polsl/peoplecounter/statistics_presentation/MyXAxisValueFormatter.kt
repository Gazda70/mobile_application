package pl.polsl.peoplecounter.statistics_presentation

import android.annotation.SuppressLint
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.SimpleDateFormat
import java.util.*

class MyXAxisValueFormatter(): ValueFormatter() {

    @SuppressLint("SimpleDateFormat")
    @Override
    fun getXValue(day:String, month:String, year:String, index:Int, viewPortHandler:ViewPortHandler ):String {
        return year + '-' + month + '-' + day
        /*try {

            val sdf:SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            return sdf.format(Date (day, ))

        } catch (e:Exception) {

            return dateInMillisecons;
        }*/
    }
}
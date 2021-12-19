package pl.polsl.peoplecounter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class DateViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val detectionDate = MutableLiveData<DetectionDate>()
}
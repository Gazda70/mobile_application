package pl.polsl.peoplecounter.time_setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.polsl.peoplecounter.datatypes.DetectionTime

class EndTimeViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val detectionEndTime = MutableLiveData<DetectionTime>()
}
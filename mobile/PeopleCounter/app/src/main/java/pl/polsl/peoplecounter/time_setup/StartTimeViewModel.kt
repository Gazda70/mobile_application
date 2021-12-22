package pl.polsl.peoplecounter.time_setup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.polsl.peoplecounter.datatypes.DetectionTime

class StartTimeViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val detectionStartTime = MutableLiveData<DetectionTime>()
}
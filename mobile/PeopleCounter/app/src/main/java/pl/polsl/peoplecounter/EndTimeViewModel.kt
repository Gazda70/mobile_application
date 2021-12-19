package pl.polsl.peoplecounter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EndTimeViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val detectionEndTime = MutableLiveData<DetectionTime>()
}
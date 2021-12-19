package pl.polsl.peoplecounter

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StartTimeViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    val detectionStartTime = MutableLiveData<DetectionTime>()
}
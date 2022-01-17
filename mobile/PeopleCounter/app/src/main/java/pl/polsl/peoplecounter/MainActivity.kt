package pl.polsl.peoplecounter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.time.ExperimentalTime


class MainActivity : AppCompatActivity() {

    @ExperimentalTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
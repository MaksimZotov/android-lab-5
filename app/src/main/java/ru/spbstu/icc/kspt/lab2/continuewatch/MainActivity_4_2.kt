package ru.spbstu.icc.kspt.lab2.continuewatch

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity_4_2 : AppCompatActivity() {
    companion object {
        private const val SECONDS_ELAPSED_KEY = "SECONDS_ELAPSED_KEY"
    }

    private lateinit var textSecondsElapsed: TextView

    private var updateSeconds = false
    private var secondsElapsed = 0

    private val backgroundThread = Thread {
        while (true) {
            Thread.sleep(1000)
            if (updateSeconds) {
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed, ++secondsElapsed)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed)
        backgroundThread.start()
    }

    override fun onResume() {
        super.onResume()
        updateSeconds = true
    }

    override fun onPause() {
        super.onPause()
        updateSeconds = false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SECONDS_ELAPSED_KEY, secondsElapsed)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        secondsElapsed = savedInstanceState.getInt(SECONDS_ELAPSED_KEY)
        textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed)
    }
}
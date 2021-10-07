package ru.spbstu.icc.kspt.lab2.continuewatch

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity_4_3 : AppCompatActivity() {
    companion object {
        private const val SECONDS_ELAPSED_KEY = "SECONDS_ELAPSED_KEY"
    }

    private lateinit var sharedPreferences: SharedPreferences

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
        sharedPreferences = getSharedPreferences(
            getString(R.string.seconds_elapsed_key),
            Context.MODE_PRIVATE
        )
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        secondsElapsed = sharedPreferences.getInt(SECONDS_ELAPSED_KEY, 0)
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
        with(sharedPreferences.edit()) {
            putInt(SECONDS_ELAPSED_KEY, secondsElapsed)
            apply()
        }
    }
}
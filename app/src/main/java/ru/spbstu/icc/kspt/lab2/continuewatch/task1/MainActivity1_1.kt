package ru.spbstu.icc.kspt.lab2.continuewatch.task1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.spbstu.icc.kspt.lab2.continuewatch.R

class MainActivity1_1 : AppCompatActivity() {
    companion object {
        private const val SECONDS_ELAPSED_KEY = "SECONDS_ELAPSED_KEY"
    }

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var textSecondsElapsed: TextView

    private var updateSeconds = false
    private var secondsElapsed = 0

    private val backgroundThread = Thread {
        try {
            while (true) {
                Thread.sleep(1000)
                if (updateSeconds) {
                    textSecondsElapsed.post {
                        textSecondsElapsed.text = getString(R.string.seconds_elapsed, ++secondsElapsed)
                    }
                }
            }
        } catch (ex: InterruptedException) { }
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

    override fun onDestroy() {
        backgroundThread.interrupt()
        super.onDestroy()
    }
}
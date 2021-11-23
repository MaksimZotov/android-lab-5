package ru.spbstu.icc.kspt.lab2.continuewatch.task1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import ru.spbstu.icc.kspt.lab2.continuewatch.R
import java.util.*

class MainActivity1_3 : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity1_3"
        private const val MILLISECONDS_ELAPSED_KEY = "MILLISECONDS_KEY"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textSecondsElapsed: TextView
    private var initMilliseconds = 0L
    private var millisecondsElapsed = 0L
    private var secondsElapsed = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences(
            getString(R.string.milliseconds_elapsed_key),
            Context.MODE_PRIVATE
        )
        textSecondsElapsed = findViewById(R.id.textSecondsElapsed)
        millisecondsElapsed = sharedPreferences.getLong(MILLISECONDS_ELAPSED_KEY, 0)
        textSecondsElapsed.text = getString(
            R.string.seconds_elapsed,
            millisecondsElapsed.toSeconds()
        )

        lifecycleScope.launchWhenResumed {
            while (true) {
                Log.i(TAG, "Seconds: $secondsElapsed")
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed++)
                }
                delay(1000)
            }
        }
    }

    override fun onResume() {
        Log.i(TAG, "onResume")
        initMilliseconds = Date().time
        secondsElapsed = millisecondsElapsed.toSeconds()
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        millisecondsElapsed += (Date().time - initMilliseconds)
    }

    override fun onStop() {
        super.onStop()
        with(sharedPreferences.edit()) {
            putLong(MILLISECONDS_ELAPSED_KEY, millisecondsElapsed)
            apply()
        }
    }
}
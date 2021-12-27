package ru.spbstu.icc.kspt.lab2.continuewatch.task1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CancellationException
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
            var startTime = Date().time
            try {
                Log.i(TAG, "Coroutine is launched")
                Log.i(TAG, "Seconds (first): $secondsElapsed")

                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed)
                }

                val difMillisecondsAndNextSeconds =
                    if (millisecondsElapsed == 0L) 1000
                    else 1000 - (millisecondsElapsed - secondsElapsed * 1000)

                Log.i(
                    TAG, "DIF between milliseconds " +
                        "and next seconds: $difMillisecondsAndNextSeconds")

                Thread.sleep(difMillisecondsAndNextSeconds)

                var nextDelay = 1000L
                while (true) {
                    Log.i(TAG, "Seconds: ${++secondsElapsed}")
                    textSecondsElapsed.post {
                        textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed)
                    }
                    startTime = Date().time
                    Thread.sleep(nextDelay)
                    val endTime = Date().time
                    val correction = endTime - startTime - 1000
                    nextDelay = (1000 - correction)
                }
            } catch (ex: CancellationException) {
                millisecondsElapsed = (Date().time - startTime) + secondsElapsed * 1000
                Log.i(TAG, "Coroutine is cancelled")
                Log.i(TAG, "Elapsed milliseconds: $millisecondsElapsed")
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
    }

    override fun onStop() {
        super.onStop()
        with(sharedPreferences.edit()) {
            putLong(MILLISECONDS_ELAPSED_KEY, millisecondsElapsed)
            apply()
        }
    }
}
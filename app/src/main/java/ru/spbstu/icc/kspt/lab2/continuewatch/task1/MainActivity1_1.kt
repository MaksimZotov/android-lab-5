package ru.spbstu.icc.kspt.lab2.continuewatch.task1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.spbstu.icc.kspt.lab2.continuewatch.R
import java.util.*

class MainActivity1_1 : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity1_1"
        private const val MILLISECONDS_ELAPSED_KEY = "MILLISECONDS_KEY"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textSecondsElapsed: TextView
    private var millisecondsElapsed = 0L
    private var secondsElapsed = 0

    private lateinit var backgroundThread: Thread
    private val threadRunnable = Runnable {
        var startTime = Date().time
        try {
            Log.i(TAG, "Thread is launched")

            val difMillisecondsAndNextSeconds =
                if (millisecondsElapsed == 0L) 1000
                else 1000 - (millisecondsElapsed - secondsElapsed * 1000)

            Log.i(TAG, "DIF between milliseconds " +
                    "and next seconds: $difMillisecondsAndNextSeconds")

            Log.i(TAG, "Seconds (first): $secondsElapsed")

            textSecondsElapsed.post {
                textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed)
            }

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
        } catch (ex: InterruptedException) {
            millisecondsElapsed +=
                (Date().time - startTime) +
                        (secondsElapsed - millisecondsElapsed.toSeconds()) * 1000
            Log.i(TAG, "Thread is interrupted")
            Log.i(TAG, "Elapsed milliseconds: $millisecondsElapsed")
        }
    }

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
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume")
        secondsElapsed = millisecondsElapsed.toSeconds()
        backgroundThread = Thread(threadRunnable).apply { start() }
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        backgroundThread.interrupt()
    }

    override fun onStop() {
        super.onStop()
        with(sharedPreferences.edit()) {
            putLong(MILLISECONDS_ELAPSED_KEY, millisecondsElapsed)
            apply()
        }
    }
}
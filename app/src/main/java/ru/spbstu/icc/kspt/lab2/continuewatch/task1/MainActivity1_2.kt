package ru.spbstu.icc.kspt.lab2.continuewatch.task1

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import ru.spbstu.icc.kspt.lab2.continuewatch.R
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity1_2 : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity1_2"
        private const val MILLISECONDS_ELAPSED_KEY = "MILLISECONDS_KEY"
    }

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var textSecondsElapsed: TextView
    private var initMilliseconds = 0L
    private var millisecondsElapsed = 0L
    private var secondsElapsed = 0

    private lateinit var executorService: ExecutorService
    private val threadRunnable = Runnable {
        try {
            Log.i(TAG, "Thread is launched")
            while (true) {
                Log.i(TAG, "Seconds: $secondsElapsed")
                textSecondsElapsed.post {
                    textSecondsElapsed.text = getString(R.string.seconds_elapsed, secondsElapsed++)
                }
                Thread.sleep(1000)
            }
        } catch (ex: InterruptedException) {
            Log.i(TAG, "Thread is interrupted")
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
        initMilliseconds = Date().time
        secondsElapsed = millisecondsElapsed.toSeconds()
        executorService = Executors.newSingleThreadExecutor().apply { execute(threadRunnable) }
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause")
        executorService.shutdownNow()
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
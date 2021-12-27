package ru.spbstu.icc.kspt.lab2.continuewatch.task3

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import ru.spbstu.icc.kspt.lab2.continuewatch.R
import ru.spbstu.icc.kspt.lab2.continuewatch.task2.MainActivity2
import java.io.InterruptedIOException
import java.net.URL


class MainActivity3 : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity3"
        private const val URL = "https://open.spbstu.ru/wp-content/uploads/2018/09/04KOTLIN2.jpg"
    }

    private var job: Job? = null
    private val iconBitmap: MutableLiveData<Bitmap?> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_download)

        val image = findViewById<ImageView>(R.id.iv_image)
        val download = findViewById<Button>(R.id.btn_download)

        iconBitmap.observe(this) { icon ->
            image.setImageBitmap(icon)
        }

        download.setOnClickListener {
            job?.cancel() // just for tests
            job = lifecycleScope.launch(Dispatchers.IO) {
                try {
                    Log.i(TAG, "Coroutine is launched")
                    val url = URL(URL)
                    val icon = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    iconBitmap.postValue(icon)
                    Log.i(TAG, "Coroutine is done")
                } catch (ex: Exception) {
                    Log.i(TAG, "Coroutine is cancelled")
                    ex.printStackTrace()
                }
            }
        }
    }
}
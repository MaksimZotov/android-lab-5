package ru.spbstu.icc.kspt.lab2.continuewatch.task2

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import ru.spbstu.icc.kspt.lab2.continuewatch.R
import ru.spbstu.icc.kspt.lab2.continuewatch.getExecutorService
import java.io.InterruptedIOException
import java.net.URL
import java.util.concurrent.Future


class MainActivity2 : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity2"
        private const val URL = "https://open.spbstu.ru/wp-content/uploads/2018/09/04KOTLIN2.jpg"
    }

    private var task: Future<*>? = null
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
            task?.let {
                if (!it.isDone) {
                    it.cancel(true)
                }
            }
            task = application.getExecutorService()?.submit {
                try {
                    Log.i(TAG, "Thread is launched")
                    val url = URL(URL)
                    val icon = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                    iconBitmap.postValue(icon)
                    Log.i(TAG, "Task is done")
                } catch (ex: InterruptedIOException) {
                    Log.i(TAG, "Thread is interrupted")
                    ex.printStackTrace()
                }
            }
        }
    }

    override fun onDestroy() {
        task?.cancel(true)
        super.onDestroy()
    }
}
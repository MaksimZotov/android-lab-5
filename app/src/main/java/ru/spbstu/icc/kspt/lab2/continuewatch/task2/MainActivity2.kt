package ru.spbstu.icc.kspt.lab2.continuewatch.task2

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import ru.spbstu.icc.kspt.lab2.continuewatch.R
import ru.spbstu.icc.kspt.lab2.continuewatch.getExecutorService
import java.net.URL


class MainActivity2 : AppCompatActivity() {
    companion object {
        private const val URL = "https://open.spbstu.ru/wp-content/uploads/2018/09/04KOTLIN2.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_download)

        val image = findViewById<ImageView>(R.id.iv_image)
        val download = findViewById<Button>(R.id.btn_download)

        download.setOnClickListener {
            application.getExecutorService()?.execute {
                val url = URL(URL)
                val icon = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                runOnUiThread { image.setImageBitmap(icon) }
            }
        }
    }
}
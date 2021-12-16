package ru.spbstu.icc.kspt.lab2.continuewatch.task4

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import coil.load
import ru.spbstu.icc.kspt.lab2.continuewatch.R


class MainActivity4 : AppCompatActivity() {
    companion object {
        private const val URL = "https://open.spbstu.ru/wp-content/uploads/2018/09/04KOTLIN2.jpg"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_download)

        val image = findViewById<ImageView>(R.id.iv_image)
        val download = findViewById<Button>(R.id.btn_download)

        download.setOnClickListener { image.load(URL) }
    }
}
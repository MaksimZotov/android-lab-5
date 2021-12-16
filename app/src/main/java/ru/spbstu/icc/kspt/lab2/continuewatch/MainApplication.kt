package ru.spbstu.icc.kspt.lab2.continuewatch

import android.app.Application
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainApplication() : Application() {
    val executorService: ExecutorService = Executors.newSingleThreadExecutor()
}

fun Application?.getExecutorService(): ExecutorService? {
    return if (this is MainApplication) executorService else null
}
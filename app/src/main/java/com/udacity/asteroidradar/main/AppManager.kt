package com.udacity.asteroidradar.main

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.asteroid.DataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AppManager : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)
    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }
    private fun delayedInit() = applicationScope.launch {
        setupWork()
    }
    private fun setupWork() {
        // Setting up constraints for the work, specifying that it should only run when the device
        // is connected to an un-metered network (e.g. wifi), is charging, and the battery isn't low on charge.
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .setRequiresBatteryNotLow(true)
            .build()
        // Using the DataWorker class, which will be responsible for performing the work.
        // The PeriodicWorkRequest is set to run every day, where it refreshes the cache.
        val refreshRequest = PeriodicWorkRequestBuilder<DataWorker>(1, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            DataWorker.WORK_NAME,
            // if there is already a periodic work with the same name, the old work is kept and the new work is not enqueued.
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )
    }
}
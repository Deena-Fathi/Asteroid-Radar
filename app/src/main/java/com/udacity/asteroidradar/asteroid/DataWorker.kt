package com.udacity.asteroidradar.asteroid

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.network.getEndDate
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.database.*

/**
 * This is a class that extends CoroutineWorker and is used to perform a specific task in the background.
 */
class DataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "DataWorker"
    }

    /**
     * The task involves deleting all the data in the AsteroidDatabase, and then refreshing it by calling
     * the refreshAsteroid method from the AsteroidRepository. The refreshAsteroid method makes a network
     * request to fetch new data and inserts it into the database. The doWork method returns a Result
     * object indicating the success or failure of the task. If the task fails, the Result.retry()
     * method tells the system to try again later.
     */
    override suspend fun doWork(): Result {
        val database = AsteroidDatabase.getDatabase(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.deleteAll()
            repository.refreshAsteroid(getStartDate(), getEndDate(), API_KEY)
            Result.success()
        } catch (e: Exception) {
            Log.e("DataWorker", "Retrying to fetch data")
            Result.retry()
        }
    }
}
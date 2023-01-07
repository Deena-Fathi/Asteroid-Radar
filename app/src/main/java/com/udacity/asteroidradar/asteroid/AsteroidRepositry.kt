package com.udacity.asteroidradar.asteroid

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.network.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.*
import com.udacity.asteroidradar.network.Service.apiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

/**
 * This is a class  represents a repository for storing and retrieving Asteroid data.
 * It has a private property database of type AsteroidDatabase, which is a reference to an object
 * that provides access to the database where the asteroid data is stored.
 */
class AsteroidRepository(private val database: AsteroidDatabase) {

    // The asteroids property is a LiveData object that returns a list of Asteroid objects.
    // It is initialized using the Transformations.map function, which transforms the data emitted
    // by the database.asteroidDao.getAllAsteroids() function into a new form. In this case,
    // the asDomainModel function is called on the list of Asteroid objects returned by the database
    // access object, which converts the objects from the database model to the domain model.
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAllAsteroids()
    ) {
        it.asDomainModel()
    }

    // The getTodayAsteroids and getWeekAsteroids functions return LiveData objects that return lists
    // of Asteroid objects for a given date range. These lists are obtained by calling the corresponding
    // functions on the database access object and passing the required date range as arguments.
    fun getTodayAsteroids(todayDate: String) =
        database.asteroidDao.getTodayAsteroids(todayDate)

    fun getWeekAsteroids(startDate: String, endDate: String) =
        database.asteroidDao.getWeekAsteroids(startDate, endDate)

    /**
     * The deleteAll function is a suspending function that calls the deleteAll function on the
     * database access object to delete all Asteroid objects from the database.
     */
    suspend fun deleteAll() = database.asteroidDao.deleteAll()

    /**
     * The refreshAsteroid function is a suspending function that runs on a background thread using
     * the Dispatchers.IO context. It calls the getAsteroid function on the apiService
     * object, passing it the start and end dates and the API key as arguments.
     * It then parses the JSON result returned by the function and inserts the resulting Asteroid
     * objects into the database using the insertAll function on the database access object.
     */
    suspend fun refreshAsteroid(startDate: String, endDate: String, apiKey: String) =
        withContext(Dispatchers.IO) {
            val asteroid = apiService.getAsteroid(startDate, endDate, apiKey)
            Log.i("Repo", "Received from Network")
            val data = parseAsteroidsJsonResult(JSONObject(asteroid))
            database.asteroidDao.insertAll(*data.asDatabaseModel())
            Log.i("Repo", "Inserted to the database")
        }


    /**
     * The pictureOfDay function is a suspending function that calls the getPictureOfDay function on
     * the apiService object and returns the result.
     */
    suspend fun pictureOfDay(apiKey: String) = withContext(Dispatchers.IO) {
        apiService.getPictureOfDay(apiKey)
    }
}
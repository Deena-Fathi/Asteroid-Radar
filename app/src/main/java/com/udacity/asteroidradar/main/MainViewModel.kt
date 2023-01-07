package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.asteroid.Asteroid
import com.udacity.asteroidradar.asteroid.AsteroidRepository
import com.udacity.asteroidradar.asteroid.PictureOfDay
import com.udacity.asteroidradar.asteroid.asDomainModel
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.network.getEndDate
import com.udacity.asteroidradar.network.getStartDate
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //enumeration class called NasaApiStatus with three values: LOADING, DONE, and ERROR
    enum class NasaApiStatus { LOADING, DONE, ERROR }

    //private val called database that is initialized with the result of calling the getDatabase function,
    // passing it the application parameter
    private val database = getDatabase(application)

    //private val called asteroidRepository which is initialized with a new instance of the AsteroidRepository class,
    // passing it the database as an argument.
    private val asteroidRepository = AsteroidRepository(database)

    //asteroidList val is a LiveData object that is initialized with
    // the result of calling the asteroids val on the asteroidRepository object.
    val asteroidList: LiveData<List<Asteroid>> = asteroidRepository.asteroids

    //The _pictureOfDay, _status and _navtoDetails vals are MutableLiveData objects
    // that are used to hold the result of the asteroidRepository.pictureOfDay
    // function call and the status of the API call,
    // object that is used to hold an Asteroid object that the user wants to see more details about, respectively.
    //
    // The pictureOfDay, status, and navtoDetails  vals are LiveData objects that provide read-only
    // access to the values held by _pictureOfDay, _status, and _navtoDetails
    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _navtoDetails = MutableLiveData<Asteroid?>()
    val navtoDetails: LiveData<Asteroid?>
        get() = _navtoDetails

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    // The init block calls the getPictureOfDay and
    // refreshAsteroid functions when the view model is created.
    init {
        getPictureOfDay()
        refreshAsteroid()
        Log.i("ViewModel", getStartDate())
        Log.i("ViewModel", getEndDate())
    }

    /**
     * getTodayAsteroids return LiveData object that is a transformed versions of the results
     * of calling the getTodayAsteroids function on the asteroidRepository object
     */
    fun getTodayAsteroids(): LiveData<List<Asteroid>> {
        return asteroidRepository.getTodayAsteroids(getStartDate()).map { it.asDomainModel() }
    }

    /**
     * getWeekAsteroids return LiveData object that is a transformed versions of the results
     * of calling the getWeekAsteroids function on the asteroidRepository object
     */
    fun getWeekAsteroids(): LiveData<List<Asteroid>> {
        return Transformations.map(
            asteroidRepository.getWeekAsteroids(getStartDate(), getEndDate())
        ) {
            it.asDomainModel()
        }
    }

    /**
     * used to set the value of the _navtoDetails val.
     */
    fun dispDetailsStart(asteroid: Asteroid) {
        _navtoDetails.value = asteroid
    }

    /**
     * used to clear  the value of the _navtoDetails val.
     */
    fun dispDetailsEnd() {
        _navtoDetails.value = null
    }

    /**
     * The refreshAsteroid function uses the viewModelScope to launch a coroutine that calls the
     * refreshAsteroid function on the asteroidRepository object,
     * passing it the getStartDate, getEndDate, and API_KEY values as arguments
     */
    private fun refreshAsteroid() {
        viewModelScope.launch {
            try {
                asteroidRepository.refreshAsteroid(getStartDate(), getEndDate(), API_KEY)
            } catch (e: Exception) {
                Log.e("ViewModel", "Can't Refresh asteroids", e)
            }
        }
    }

    /**
     * The getPictureOfDay function viewModelScope to launch a coroutine that makes a call
     * to the asteroidRepository.pictureOfDay function and passes it the API_KEY as an argument.
     * The function sets the value of the _status object to NasaApiStatus.LOADING before making the call,
     * and then sets it to NasaApiStatus.DONE if the call is successful, or NasaApiStatus.ERROR if it is not.
     * The _pictureOfDay object is also being set to the result of the call to asteroidRepository.pictureOfDay.
     */
    private fun getPictureOfDay() {
        viewModelScope.launch {
            _status.value = NasaApiStatus.LOADING
            try {
                _pictureOfDay.value = asteroidRepository.pictureOfDay(API_KEY)
                _status.value = NasaApiStatus.DONE
            } catch (e: Exception) {
                _status.value = NasaApiStatus.ERROR
                Log.e("ViewModel", "Can't Refresh Picture", e)
            }
        }
    }

}
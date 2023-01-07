package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.asteroid.Asteroid


@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    /**
     * A suspend function that inserts one or more asteroids into the database.
     * If a conflict occurs, such as when an asteroid with the same primary key already exists,
     * the function replaces the existing asteroid with the new one.
     */
    suspend fun insertAll(vararg asteroid: Asteroid)

    @Query("DELETE FROM Asteroid")
    /**
     * deleteAll is a suspend function that deletes all asteroids from the database.
     */
    suspend fun deleteAll()

    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate ASC")
    /**
     * Returns a LiveData list of all asteroids in the database,
     * ordered by closeApproachDate in ascending order.
     */
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Query("SELECT * FROM Asteroid WHERE closeApproachDate == :todayDate ORDER BY closeApproachDate ASC")
    /**
     * Returns a LiveData list of asteroids in the database whose closeApproachDate is equal to a specific date,
     * also ordered by closeApproachDate in ascending order.
     */
    fun getTodayAsteroids(todayDate: String): LiveData<List<Asteroid>>

    @Query(
        "SELECT * FROM Asteroid WHERE closeApproachDate " +
                "BETWEEN :startDate AND :endDate " +
                "ORDER BY closeApproachDate ASC"
    ) /**
     * Returns a LiveData list of asteroids in the database whose closeApproachDate is between two
     * specific dates, also ordered by closeApproachDate in ascending order.
     */
    fun getWeekAsteroids(startDate: String, endDate: String): LiveData<List<Asteroid>>

}
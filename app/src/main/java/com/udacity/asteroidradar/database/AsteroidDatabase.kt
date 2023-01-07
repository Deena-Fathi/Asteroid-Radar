package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.asteroid.Asteroid


@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao


    companion object {
        private lateinit var INSTANCE: AsteroidDatabase

        /**
         * Used to get an instance of the database. If the instance has already been initialized,
         * it is simply returned. If not, it is synchronized and then initialized using the
         * Room.databaseBuilder function and returned. The databaseBuilder function is used to
         * create an instance of the database. It requires a Context object, the class of the database,
         * and the name of the database as its arguments. The database is created in the application
         * context and has a name of "asteroids".
         */
        fun getDatabase(context: Context): AsteroidDatabase {
            //Creating an instance as the database is expensive to make
            if (this::INSTANCE.isInitialized) return INSTANCE
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroids"
                ).build()
                return INSTANCE
            }
        }
    }
}

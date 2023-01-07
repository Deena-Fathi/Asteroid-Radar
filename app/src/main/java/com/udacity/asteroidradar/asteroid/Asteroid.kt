package com.udacity.asteroidradar.asteroid

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Asteroid(
    @PrimaryKey
    val id: Long,
    val codename: String,
    val closeApproachDate: String,
    val absoluteMagnitude: Double,
    val estimatedDiameter: Double,
    val relativeVelocity: Double,
    val distanceFromEarth: Double,
    val isPotentiallyHazardous: Boolean
) : Parcelable

/**
 * This is a function extension on the List type. It takes a list of Asteroid objects as input,
 * and returns an array of Asteroid objects as output. The function appears to simply map the
 * elements of the input list to new Asteroid objects with the same properties as the original elements,
 * and then returns the resulting list as an array. The toTypedArray() function is called on the mapped
 * list to convert it to an array.
 */
fun List<Asteroid>.asDatabaseModel(): Array<Asteroid> {
    return this.map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}
/**
 * This function is an extension function on List<Asteroid>. It is being used to convert a list of
 * Asteroid objects from a database model to a domain model. The function does this by iterating
 * over the list of Asteroid objects and creating a new Asteroid object for each one using the properties
 * of the original object. The new Asteroid object is then added to the list that is returned by the function.
 */
fun List<Asteroid>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            codename = it.codename,
            closeApproachDate = it.closeApproachDate,
            absoluteMagnitude = it.absoluteMagnitude,
            estimatedDiameter = it.estimatedDiameter,
            relativeVelocity = it.relativeVelocity,
            distanceFromEarth = it.distanceFromEarth,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }
}
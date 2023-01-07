package com.udacity.asteroidradar.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.asteroid.Asteroid
import com.udacity.asteroidradar.databinding.ItemAsteroidBinding

/**
 * This is an adapter class in Kotlin for an Android RecyclerView that displays a list of Asteroid
 * objects. The adapter takes an OnClickListener object as a constructor parameter, which is used to
 * handle click events on the list items.
 */
class AsteroidAdapter(private val onClickListener: OnClickListener) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(DifferentItemsCallBack) {
    /**
     * The OnClickListener is a nested class within the AsteroidAdapter class that has a single
     * function, onClick, which takes an Asteroid object as an argument and calls a function passed
     * to it as a constructor parameter.
     */
    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }

    /**
     * The AsteroidViewHolder is a nested class within the AsteroidAdapter class that holds a
     * reference to an ItemAsteroidBinding object and has a bindAsteroid function that takes an
     * Asteroid object as an argument and binds it to the view holder's layout.
     */
    class AsteroidViewHolder(private val binding: ItemAsteroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /**
         * The bindAsteroid function also sets the content description of the asteroidStatus view
         * based on whether the asteroid is potentially hazardous or not.
         * The executePendingBindings function is called to ensure that the binding is immediately executed.
         */
        fun bindAsteroid(asteroid: Asteroid) {
            binding.asteroid = asteroid
            if (asteroid.isPotentiallyHazardous) {
                binding.asteroidStatus.contentDescription =
                    itemView.context.getString(R.string.potentially_hazardous_asteroid_image)
            } else {
                binding.asteroidStatus.contentDescription =
                    itemView.context.getString(R.string.not_hazardous_asteroid_image)
            }
            binding.executePendingBindings()
        }
    }

    /**
     * The onCreateViewHolder function is overridden to inflate an ItemAsteroidBinding layout and
     * return a new instance of the AsteroidViewHolder class, passing it the inflated layout as an argument.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAsteroidBinding.inflate(inflater, parent, false)
        return AsteroidViewHolder(binding)
    }

    /**
     * The onBindViewHolder function is overridden to bind the Asteroid object at the specified
     * place to the view holder, and to set an OnClickListener on the view holder's item view that
     * calls the onClick function of the OnClickListener object passed to the adapter's constructor.
     */
    override fun onBindViewHolder(viewholder: AsteroidViewHolder, place: Int) {
        val asteroid = getItem(place)
        viewholder.bindAsteroid(asteroid)
        viewholder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid)
        }
    }

    /**
     * The adapter class is a subclass of the Android ListAdapter class, and it uses the
     * DifferentItemsCallBack object as a companion object to provide implementation for the
     * areItemsTheSame and areContentsTheSame functions. These functions are used by the ListAdapter
     * to determine whether two items are the same and whether their contents are the same, respectively.
     */
    companion object DifferentItemsCallBack : DiffUtil.ItemCallback<Asteroid>() {
        /**
         * checks whether the two objects have the same id value.
         */
        override fun areContentsTheSame(newAsteroid: Asteroid, oldAsteroid: Asteroid): Boolean {
            return oldAsteroid.id == newAsteroid.id
        }

        /**
         *  checks whether the two Asteroid objects are the same object
         */
        override fun areItemsTheSame(newAsteroid: Asteroid, oldAsteroid: Asteroid): Boolean {
            return oldAsteroid == newAsteroid
        }
    }

}

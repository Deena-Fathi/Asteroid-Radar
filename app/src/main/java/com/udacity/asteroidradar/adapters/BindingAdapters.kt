package com.udacity.asteroidradar.adapters

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.asteroid.Asteroid
import com.udacity.asteroidradar.asteroid.PictureOfDay
import com.udacity.asteroidradar.main.MainViewModel

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
    }
    imageView.contentDescription = if (isHazardous)
        context.getString(R.string.potentially_hazardous_asteroid_image)
    else
        context.getString(R.string.not_hazardous_asteroid_image)
}

@BindingAdapter("pictureOfDayDescription")
fun bindPictureOfDayDescription(imageView: ImageView, PicOfDay: PictureOfDay?){
    val context = imageView.context
    imageView.contentDescription = when (PicOfDay) {
        null -> context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
        else -> context.getString(R.string.nasa_picture_of_day_content_description_format, PicOfDay.title)
    }
}


@BindingAdapter("astronomicalUnitText")
fun bindTextViewToAstronomicalUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.astronomical_unit_format), number)
}

@BindingAdapter("kmUnitText")
fun bindTextViewToKmUnit(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_unit_format), number)
}

@BindingAdapter("velocityText")
fun bindTextViewToDisplayVelocity(textView: TextView, number: Double) {
    val context = textView.context
    textView.text = String.format(context.getString(R.string.km_s_unit_format), number)
}

@BindingAdapter("listData")
        /**
         * Takes a RecyclerView and a list of Asteroid objects as arguments. It retrieves the
         * RecyclerView's adapter, which is expected to be of type AsteroidAdapter, and calls the
         * adapter's submitList method with the list of Asteroid objects as an argument.
         * This updates the data displayed in the RecyclerView.
         */
fun bindRecyclerView(recyclerView: RecyclerView, asteroidList: List<Asteroid>?) {
    val adapter = recyclerView.adapter as AsteroidAdapter
    adapter.submitList(asteroidList)
}

@BindingAdapter(value = ["pictureOfDay", "status"])
        /**
         * Takes an ImageView, a PictureOfDay object, and a MainViewModel.NasaApiStatus as arguments.
         * If the PictureOfDay object's mediaType is "image", the method uses the Picasso library to
         * load the image at the URL contained in the PictureOfDay object's url field and displays
         * it in the ImageView. It also sets the contentDescription of the ImageView to a string
         * describing the image. If the mediaType is not "image", the method sets the contentDescription
         * to a string indicating that there is no image to display. If the NasaApiStatus is ERROR,
         * the method sets the ImageView to display a placeholder image.
         */
fun bindImageView(
    imageView: ImageView,
    pictureOfDay: PictureOfDay?,
    status: MainViewModel.NasaApiStatus
) {

    if (pictureOfDay?.mediaType == "image") {
        Picasso.get()
            .load(pictureOfDay.url)
            .into(imageView)
        imageView.contentDescription = imageView.context.getString(
            R.string.nasa_picture_of_day_content_description_format,
            pictureOfDay.title
        )
    } else {
        imageView.contentDescription = imageView.context.getString(
            R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet
        )
    }

    if (status == MainViewModel.NasaApiStatus.ERROR) {
        imageView.setImageResource(R.drawable.placeholder_picture_of_day)
    }
}

@BindingAdapter("progressBar")
        /**
         * Takes a ProgressBar and a MainViewModel.NasaApiStatus as arguments.
         * Depending on the value of the NasaApiStatus, it sets the visibility of the ProgressBar
         * to either VISIBLE, or GONE.
         */
fun bindProgressBar(progressBar: ProgressBar, status: MainViewModel.NasaApiStatus) {
    when (status) {
        MainViewModel.NasaApiStatus.LOADING ->
            progressBar.visibility = View.VISIBLE

        MainViewModel.NasaApiStatus.DONE ->
            progressBar.visibility = View.GONE

        MainViewModel.NasaApiStatus.ERROR -> {
            progressBar.visibility = View.VISIBLE
        }
    }
}
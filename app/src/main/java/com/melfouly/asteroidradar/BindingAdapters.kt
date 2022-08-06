package com.melfouly.asteroidradar

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.melfouly.asteroidradar.network.PictureOfDay
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

@BindingAdapter("statusIcon")
fun bindAsteroidStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        imageView.contentDescription = context.getString(R.string.potentially_hazardous_status)
    } else {
        imageView.setImageResource(R.drawable.ic_status_normal)
        imageView.contentDescription = context.getString(R.string.not_hazardous_status)
    }
}

@BindingAdapter("asteroidStatusImage")
fun bindDetailsStatusImage(imageView: ImageView, isHazardous: Boolean) {
    val context = imageView.context
    if (isHazardous) {
        imageView.setImageResource(R.drawable.asteroid_hazardous)
        imageView.contentDescription =
            context.getString(R.string.potentially_hazardous_asteroid_image)
    } else {
        imageView.setImageResource(R.drawable.asteroid_safe)
        imageView.contentDescription = context.getString(R.string.not_hazardous_asteroid_image)
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

// Binding Adapter to get the picture of the day and handle or other cases.
@BindingAdapter("pictureOfDay")
fun bindImageViewToDisplayPictureOfDay(imageView: ImageView, pictureOfDay: PictureOfDay?) {
    val context = imageView.context
    if (pictureOfDay != null) {
        Picasso.get().load(pictureOfDay.url).placeholder(R.drawable.placeholder_picture_of_day)
            .error(R.drawable.placeholder_picture_of_day).into(imageView)

        val description = String.format(
            context.getString(R.string.nasa_picture_of_day_content_description_format),
            pictureOfDay.title
        )
        imageView.contentDescription = description
    } else {
        imageView.setImageResource(R.drawable.placeholder_picture_of_day)
        imageView.contentDescription =
            context.getString(R.string.this_is_nasa_s_picture_of_day_showing_nothing_yet)
    }
}
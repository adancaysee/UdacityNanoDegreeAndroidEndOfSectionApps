package com.udacity.politicalpreparedness.util

import android.view.View
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.udacity.politicalpreparedness.R



@BindingAdapter("imageUrl")
fun bindImageUrl(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .circleCrop()
            .into(imgView)
    } ?: imgView.setImageResource(R.drawable.ic_profile)
}

@BindingAdapter("app:fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = true) {
    view.visibility = if (visible == true) View.VISIBLE else View.GONE
}







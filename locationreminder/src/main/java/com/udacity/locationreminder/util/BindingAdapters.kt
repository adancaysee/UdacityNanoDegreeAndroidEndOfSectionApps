package com.udacity.locationreminder.util

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Use this binding adapter to show and hide the views using boolean variables
 */
@BindingAdapter("app:fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = true) {
    if (visible == true) {
        view.visibility = View.VISIBLE
    }else {
        view.visibility = View.GONE
    }
}




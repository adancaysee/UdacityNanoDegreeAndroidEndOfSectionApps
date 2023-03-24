package com.udacity.locationreminder.util

import android.view.View
import androidx.databinding.BindingAdapter

/**
 * Use this binding adapter to show and hide the views using boolean variables
 */
@BindingAdapter("app:fadeVisible")
fun setFadeVisible(view: View, visible: Boolean? = true) {
    if (view.tag == null) {
        view.tag = true
        view.visibility = if (visible == true) View.VISIBLE else View.GONE
    } else {
        view.animate().cancel()
        if (visible == true) {
            if (view.visibility == View.GONE)
                view.fadeIn()
        } else {
            if (view.visibility == View.VISIBLE)
                view.fadeOut()
        }
    }
}



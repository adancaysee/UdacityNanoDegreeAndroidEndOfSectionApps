package com.udacity.politicalpreparedness.util

import android.view.View
import com.google.android.material.snackbar.Snackbar


fun View.showSnackbar(snackbarText: String, timeLength: Int = Snackbar.LENGTH_LONG) {
    Snackbar.make(this, snackbarText, timeLength).run {
        show()
    }
}
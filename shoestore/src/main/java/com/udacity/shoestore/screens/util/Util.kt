package com.udacity.shoestore.screens.util

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.shoestore.R
import com.udacity.shoestore.model.Shoe
import java.util.*


@SuppressLint("SetTextI18n")
@BindingAdapter("shoeTitleFormatted")
fun setShoeTitle(textView: TextView, shoe: Shoe) {
    val boldSpan = StyleSpan(Typeface.BOLD)
    val spannable =
        SpannableStringBuilder("${shoe.company.uppercase(Locale.getDefault())} - ${shoe.name}")
    spannable.setSpan(boldSpan, 0, shoe.company.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    textView.text = spannable
}

@BindingAdapter("shoeImage")
fun setShoeImage(view: ImageView, shoe: Shoe) {
    if (shoe.images.isNotEmpty()) {
        view.setImageResource(shoe.images[0])
    }

}

fun getShoeImageList(): List<Int> = listOf(
    R.drawable.images_1,
    R.drawable.images_2,
    R.drawable.images_3,
    R.drawable.images_4,
    R.drawable.images_5
)

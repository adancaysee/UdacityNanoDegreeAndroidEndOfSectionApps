package com.udacity.shoestore.screens.shoe_list

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.shoestore.model.Shoe
import java.util.*


@SuppressLint("SetTextI18n")
@BindingAdapter("shoeTitleFormatted")
fun TextView.setShoeTitle(shoe:Shoe) {

    val boldSpan = StyleSpan(Typeface.BOLD)

    val spannable = SpannableStringBuilder("${shoe.company.uppercase(Locale.getDefault())} - ${shoe.name}")
    spannable.setSpan(boldSpan,0,shoe.company.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

    text = spannable
}
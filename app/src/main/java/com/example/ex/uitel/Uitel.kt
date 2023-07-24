package com.example.ex.uitel

import android.content.Context
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.ex.R

fun getProgressDrawable(c: Context): CircularProgressDrawable {
    val progressDrawable = CircularProgressDrawable(c)
    progressDrawable.apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
    return progressDrawable
}

fun ImageView.loadImage(uri: String?, progressDrawable: CircularProgressDrawable) {
    val options = RequestOptions().placeholder(progressDrawable)
        .error(R.mipmap.ic_launcher)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    Glide.with(context)
        .setDefaultRequestOptions(options)
        .load(uri)
        .into(this)
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    view.loadImage(url, getProgressDrawable(view.context))
}
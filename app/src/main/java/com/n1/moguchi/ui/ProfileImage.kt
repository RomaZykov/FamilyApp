package com.n1.moguchi.ui

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.view.MenuItem
import android.widget.ImageView
import com.n1.moguchi.R
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import javax.inject.Inject

class ProfileImage @Inject constructor() : LoadImage {
    override fun load(url: String, menuItem: MenuItem) {
        Picasso.get().load(url)
            .placeholder(R.drawable.profile)
            .error(R.drawable.baseline_account_circle_24)
            .into(object : Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                    menuItem.icon = BitmapDrawable(Resources.getSystem(), bitmap)
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                }

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
                }
            })
    }

    override fun load(url: String, imageView: ImageView) {
        Picasso.get().load(url)
            .placeholder(R.drawable.profile)
            .error(R.drawable.baseline_account_circle_24)
            .into(imageView)
    }
}

interface LoadImage {

    fun load(url: String, menuItem: MenuItem)

    fun load(url: String, imageView: ImageView)
}
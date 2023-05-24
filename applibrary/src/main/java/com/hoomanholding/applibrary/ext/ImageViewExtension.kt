package com.hoomanholding.applibrary.ext

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.hoomanholding.applibrary.R
import com.hoomanholding.applibrary.di.Providers
import com.hoomanholding.applibrary.model.api.Api
import com.hoomanholding.applibrary.model.data.enums.EnumEntityType


/**
 * create by m-latifi on 3/6/2023
 */


//-------------------------------------------------------------------------------------------------- downloadProfileImage
@SuppressLint("UseCompatLoadingForDrawables")
fun ImageView.downloadImage(url: String?, placeholder: Drawable?) {
    if (url.isNullOrEmpty()) {
        this.setImageDrawable(context.getDrawable(R.drawable.profile_image))
        return
    }
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    val link = "${Providers.url}$url"
    Glide
        .with(this)
        .load(link)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .placeholder(placeholder)
        .into(this)
}
//-------------------------------------------------------------------------------------------------- downloadProfileImage



//-------------------------------------------------------------------------------------------------- downloadProfileImage
@SuppressLint("UseCompatLoadingForDrawables")
fun ImageView.downloadProfileImage(
    url: String?,
    systemType: String?,
    entityType: String = EnumEntityType.ProfileImage.name,
    token: String) {
    if (url.isNullOrEmpty()) {
        this.setImageDrawable(context.getDrawable(R.drawable.profile_image))
        return
    }
    val circularProgressDrawable = CircularProgressDrawable(this.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    val link = "${Providers.url}${Api.v1}/Files/files-get-file?SystemType=$systemType&EntityType=$entityType&FileName=$url"
    val glideUrl = GlideUrl(
        link,
        LazyHeaders.Builder().addHeader("Authorization", token).build()
    )
    Glide
        .with(this)
        .load(glideUrl)
        .skipMemoryCache(true)
        .placeholder(R.drawable.profile_image)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(this)
}
//-------------------------------------------------------------------------------------------------- downloadProfileImage


//-------------------------------------------------------------------------------------------------- ImageView.setItemIcon
@BindingAdapter("setItemIcon")
fun ImageView.setItemIcon(icon: Int) {
    setImageResource(icon)
}
//-------------------------------------------------------------------------------------------------- ImageView.setItemIcon


//-------------------------------------------------------------------------------------------------- loadImage
@BindingAdapter("loadImage", "SystemType", "setEntityType", "bearerToken")
fun ImageView.loadImage(url: String?, systemType: String?, entityType: String, token: String) {
    this.downloadProfileImage(url,systemType,entityType, token)
}
//-------------------------------------------------------------------------------------------------- loadImage


package com.diacono.rediget.commons

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.provider.MediaStore
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

object ImageDownloader {

    fun saveImage(
        context: Context,
        thumb: String,
        title: String
    ): CustomTarget<Bitmap?> {
        return Glide.with(context).asBitmap().load(thumb)
            .into(object : CustomTarget<Bitmap?>() {
                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    sendToGallery(context, resource, title)
                }
            })
    }

    private fun sendToGallery(
        context: Context,
        resource: Bitmap,
        title: String
    ) {
        val savedImageURL = MediaStore.Images.Media.insertImage(
            context.contentResolver,
            resource,
            title,
            "Image of $title"
        )
    }

}
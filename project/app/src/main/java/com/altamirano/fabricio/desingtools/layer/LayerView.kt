package com.altamirano.fabricio.desingtools.layer

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.view.children

class LayerView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    val images = ArrayList<ImageInfo>()

    fun add(bitmap: Bitmap, opacity:Int){
        val position = images.size
        val image = ImageView(this.context)
        image.setImageBitmap(bitmap)
        image.alpha = opacity/100f
        images.add(ImageInfo(image, opacity,position))
        this.addView(image)
    }

    fun remove(position: Int){
        images.removeAt(position)
        this.notityDataChange()
    }

    fun update(position: Int):ImageInfo{
       return images[position]
    }

    fun notityDataChange(){
        this.removeAllViews()
        images.forEach {
            it.view.alpha = it.opacity/100f
            this.addView(it.view)
        }
    }


     data class ImageInfo(val  view: ImageView, var opacity:Int, val position:Int)
}
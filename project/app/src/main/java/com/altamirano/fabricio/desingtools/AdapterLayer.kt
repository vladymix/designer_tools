package com.altamirano.fabricio.desingtools

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.altamirano.fabricio.core.tools.inflate
import com.altamirano.fabricio.desingtools.databinding.ImageItemBinding
import com.altamirano.fabricio.desingtools.models.ImageLayer

class AdapterLayer(val listener: (ImageLayer) -> Unit) :
    RecyclerView.Adapter<AdapterLayer.ImageHolder>() {

    private val source = ArrayList<ImageLayer>()

    fun addLayer(imageLayer: ImageLayer) {
        source.add(imageLayer)
        this.notifyDataSetChanged()
    }


    inner class ImageHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ImageItemBinding.bind(itemView)

        fun bindData(imageLayer: ImageLayer) {
            binding.image.setImageBitmap(imageLayer.image)
            binding.image.setOnClickListener {
                val image = source[adapterPosition]
                image.position = adapterPosition
                listener.invoke(image)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        return ImageHolder(parent.inflate(R.layout.image_item))
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        holder.bindData(source[position])
    }

    override fun getItemCount(): Int {
        return source.size
    }

    fun get(position: Int): ImageLayer {
        return source[position]
    }

    fun remove(position: Int) {
        this.notifyItemRemoved(position)
        source.removeAt(position)
    }
}
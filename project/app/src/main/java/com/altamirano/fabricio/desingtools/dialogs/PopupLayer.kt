package com.altamirano.fabricio.desingtools.dialogs

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.altamirano.fabricio.desingtools.R
import com.altamirano.fabricio.desingtools.databinding.DialogConfigLayerBinding
import com.altamirano.fabricio.desingtools.models.ImageLayer

class PopupLayer(private val context: Context, val imageLayer: ImageLayer, val listener: Result) {

    interface Result {
        fun changeOpacity(opacity: Int, imageLayer: ImageLayer)
        fun delete(imageLayer: ImageLayer)
    }

    private lateinit var binding: DialogConfigLayerBinding

    fun show() {
        val inflater = context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.dialog_config_layer, null)
        binding = DialogConfigLayerBinding.bind(view)

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.textView.text = "$p1 %"
                listener.changeOpacity(p1, imageLayer)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                binding.content.setBackgroundColor(Color.TRANSPARENT)

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                binding.content.setBackgroundColor(ContextCompat.getColor(context, R.color.color_dialog))
            }
        })

        binding.seekBar.progress = imageLayer.opacity

        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        val popUp = PopupWindow(view, width, height, true)

        popUp.showAtLocation(view, Gravity.BOTTOM, 0, 0)

        binding.btnDelte.setOnClickListener {
            listener.delete(imageLayer)
            popUp.dismiss()
        }

        binding.btnReady.setOnClickListener {
            popUp.dismiss()
        }

    }
}
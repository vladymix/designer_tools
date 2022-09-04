package com.altamirano.fabricio.desingtools.dialogs


import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.altamirano.fabricio.core.commons.ColorPicker
import com.altamirano.fabricio.core.dialogs.ColorPickerDialog
import com.altamirano.fabricio.desingtools.databinding.DialogConfigGuidesBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DialogGuidesConfig : BottomSheetDialogFragment() {

    private lateinit var binding:DialogConfigGuidesBinding
    lateinit var listener :OnColorResult

    companion object{
        fun createDilaog(vertical: Int, horizontal: Int):DialogGuidesConfig{
            val dialog = DialogGuidesConfig()
            dialog.arguments = Bundle().apply {
                putInt("Vertical", vertical)
                putInt("Horizontal", horizontal)
            }
            return dialog
        }
    }

    interface OnColorResult{
        fun colorSelected(vertical: Int, horizontal: Int)
    }

    private var colorVertical = ColorPicker(255,255,0,0)
    private var colorHorizontal = ColorPicker(255,255,0,0)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as OnColorResult
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfigGuidesBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun executeListener(){
        listener.colorSelected(colorVertical.getAsColor(), colorHorizontal.getAsColor())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            setColor(it.getInt("Vertical"),it.getInt("Horizontal") )
        }


        binding.btnHorizontal.setOnClickListener {
           val dialog = ColorPickerDialog()
            dialog.onColorChangeListener = { colorPicker->
                colorHorizontal = colorPicker!!
                binding.indicatorHorizontal.setColorFilter(colorPicker.getAsColor())
                executeListener()
            }
            dialog.colorInit = colorHorizontal
            dialog.show(parentFragmentManager,"color")
        }

        binding.btnVertical.setOnClickListener {
            val dialog = ColorPickerDialog()
            dialog.colorInit = colorVertical
            dialog.onColorChangeListener = { colorPicker->
                colorVertical = colorPicker!!
                binding.indicatorVertical.setColorFilter(colorPicker.getAsColor())
                executeListener()
            }
            dialog.show(parentFragmentManager,"color")

        }
    }


    fun setColor(vertical:Int, horizontal:Int){
        colorHorizontal = ColorPicker(255,Color.red(horizontal),Color.green(horizontal),Color.blue(horizontal))
        colorVertical = ColorPicker(255,Color.red(vertical),Color.green(vertical),Color.blue(vertical))

        binding.indicatorHorizontal.setColorFilter(colorHorizontal.getAsColor())
        binding.indicatorVertical.setColorFilter(colorVertical.getAsColor())
    }

}
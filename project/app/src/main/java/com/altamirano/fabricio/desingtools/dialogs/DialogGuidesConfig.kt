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

    private lateinit var binding: DialogConfigGuidesBinding
    private lateinit var listener: OnColorResult
    private var colorVertical = ColorPicker(255, 255, 0, 0)
    private var colorHorizontal = ColorPicker(255, 255, 0, 0)
    private var dialogModeHorizontal = true

    companion object {
        const val TAG_VERTICAL = "Vertical"
        const val TAG_HORIZONTAL = "Horizontal"

        fun createDialog(vertical: Int, horizontal: Int): DialogGuidesConfig {
            val dialog = DialogGuidesConfig()
            dialog.arguments = Bundle().apply {
                putInt(TAG_VERTICAL, vertical)
                putInt(TAG_HORIZONTAL, horizontal)
            }
            return dialog
        }
    }

    interface OnColorResult {
        fun colorSelected(vertical: Int, horizontal: Int)
    }

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

    private fun showDialogChooseColor() {
        val dialog = ColorPickerDialog()
        dialog.colorInit = if (dialogModeHorizontal) colorHorizontal else colorVertical
        dialog.onColorChangeListener = { colorPicker ->
            if (dialogModeHorizontal) {
                colorHorizontal = colorPicker!!
                binding.indicatorHorizontal.setColorFilter(colorPicker.getAsColor())
            } else {
                colorVertical = colorPicker!!
                binding.indicatorVertical.setColorFilter(colorPicker.getAsColor())
            }
            executeListener()
        }
        dialog.show(parentFragmentManager, "color")
    }

    private fun setColor(vertical: Int, horizontal: Int) {
        colorHorizontal =
            ColorPicker(255, Color.red(horizontal), Color.green(horizontal), Color.blue(horizontal))
        colorVertical =
            ColorPicker(255, Color.red(vertical), Color.green(vertical), Color.blue(vertical))

        binding.indicatorHorizontal.setColorFilter(colorHorizontal.getAsColor())
        binding.indicatorVertical.setColorFilter(colorVertical.getAsColor())
    }

    private fun executeListener() {
        listener.colorSelected(colorVertical.getAsColor(), colorHorizontal.getAsColor())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            setColor(it.getInt(TAG_VERTICAL), it.getInt(TAG_HORIZONTAL))
        }

        binding.btnHorizontal.setOnClickListener {
            dialogModeHorizontal = true
            showDialogChooseColor()
        }

        binding.btnVertical.setOnClickListener {
            dialogModeHorizontal = false
            showDialogChooseColor()

        }
    }

}
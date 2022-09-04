package com.altamirano.fabricio.desingtools

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.altamirano.fabricio.core.dialogs.AstDialog
import com.altamirano.fabricio.desingtools.databinding.ActivityMainBinding
import com.altamirano.fabricio.desingtools.dialogs.DialogGuidesConfig
import com.altamirano.fabricio.desingtools.dialogs.PopupLayer
import com.altamirano.fabricio.desingtools.models.ImageLayer
import java.io.InputStream

class MainActivity : AppCompatActivity(), DialogGuidesConfig.OnColorResult {

    private val adapterLayer = AdapterLayer(::onImageSelected)
    private lateinit var binding: ActivityMainBinding

    private fun onImageSelected(imageLayer: ImageLayer) {
        val dialog = PopupLayer(this, imageLayer, object : PopupLayer.Result {
            override fun changeOpacity(opacity: Int, imageLayer: ImageLayer) {
                adapterLayer.get(imageLayer.position).opacity = opacity
                binding.layerView.update(imageLayer.position).opacity = opacity
                binding.layerView.notityDataChange()
            }

            override fun delete(imageLayer: ImageLayer) {
                adapterLayer.remove(imageLayer.position)
                binding.layerView.remove(imageLayer.position)
            }

        })
        dialog.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.listLayer.adapter = adapterLayer
        binding.btnAddLayer.setOnClickListener {
            addImage()
        }

        binding.btnCustomGuides.setOnClickListener {
            DialogGuidesConfig.createDialog(
                binding.guideLines.verticalColor,
                binding.guideLines.horizontalColor
            ).show(supportFragmentManager, "guides")
        }
    }

    private fun addImage() {
        val chooseFile = Intent(Intent.ACTION_GET_CONTENT)
        chooseFile.type = "image/*"
        val intent = Intent.createChooser(chooseFile, "Selecciona una imágen")
        startActivityForResult(intent, AppLogic.REQUEST_IMAGE_PICKER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, returnIntent: Intent?) {
        super.onActivityResult(requestCode, resultCode, returnIntent)

        if (resultCode != Activity.RESULT_OK) {
            // Exit without doing anything else
            return
        } else {
            when (requestCode) {
                AppLogic.REQUEST_IMAGE_PICKER -> {
                    val uriData = returnIntent?.data
                    this.processImageInput(this.contentResolver?.openInputStream(uriData!!))
                }
            }
        }

    }

    private fun processImageInput(inputStream: InputStream?) {
        if (inputStream == null) {
            Toast.makeText(this, "Error al recuperar datos", Toast.LENGTH_LONG)
                .show()
            return
        }
        val progressDialog = AstDialog(this)
        progressDialog.setMessage("Cargando imagen...")
        progressDialog.setViewProgress(true)
        progressDialog.show()

        AppLogic.processImageInputStream(this, inputStream) {
            processImageBitmap(it)
            progressDialog.dismiss()
        }
    }

    private fun processImageBitmap(bitmap: Bitmap?) {
        if (bitmap == null) {
            Toast.makeText(this, "No se ha reconocido ninguna imagen", Toast.LENGTH_LONG).show()
            return
        }

        bitmap.let { image ->
            adapterLayer.addLayer(ImageLayer(image, 100))
            binding.layerView.add(image, 100)
        }
    }

    override fun colorSelected(vertical: Int, horizontal: Int) {
        binding.guideLines.setColorGuides(vertical, horizontal)
    }

}
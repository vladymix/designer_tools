package com.altamirano.fabricio.desingtools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.widget.Toast
import com.altamirano.fabricio.core.tools.asyncTask
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object AppLogic {
    val REQUEST_IMAGE_PICKER: Int = 1020

    fun Context.getAsDp(px:Float):Float{
        return px / ( this.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun processImageInputStream(context: Context, input: InputStream?, callback: (Bitmap?) -> Unit) { input ?: return
        asyncTask(doInBackground = {
            try {
                val file = createTemFile(context, input)
                file?.let {
                    return@asyncTask BitmapFactory.decodeFile(file.path)
                }

            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return@asyncTask null

        }, postExecute = {
            callback.invoke(it)
            if( it==null){
                Toast.makeText(context,"Image processing error", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun createTemFile(context: Context, input: InputStream): File? {
        var file: File? = File(context.cacheDir, "${System.currentTimeMillis()}.png")
        try {
            FileOutputStream(file!!).use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        } catch (ex: Exception) {
            file = null
        } finally {
            try {
                input.close()
            } catch (ex: Exception) {
            }
        }
        return file
    }
}
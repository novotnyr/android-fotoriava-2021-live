package com.github.novotnyr.android.fotoriava

import android.content.Intent
import android.media.MediaScannerConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import java.io.File
import java.util.*

//1. cvaknut fotku, a nestarat sa
//Budeme pouzivat kniznicu Natario1/CameraView

class MainActivity : AppCompatActivity() {
    private lateinit var cameraView: CameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cameraView = findViewById(R.id.cameraView)
        cameraView.setLifecycleOwner(this)
        cameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toFile(createPhotoFile()) {
                    // subor sa uspesne ulozil
                    Snackbar.make(cameraView, "Cvaknuté do $it", Snackbar.LENGTH_LONG)
                        .show()

                    MediaScannerConnection.scanFile(this@MainActivity,
                        arrayOf(it?.absolutePath),
                        null, null)
                }
            }
        })
    }

    fun createPhotoFile() : File {
        val directory = externalMediaDirs.firstOrNull()
        return File(directory, UUID.randomUUID().toString() + ".jpg")
    }

    fun onTakePhotoClick(view: View) {
        cameraView.takePicture()
    }

    fun onGalleryClick(view: View) {
        startActivity(Intent(this, GalleryActivity::class.java))
    }
}
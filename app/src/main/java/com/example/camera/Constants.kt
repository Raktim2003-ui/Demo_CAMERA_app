package com.example.camera
import android.Manifest

object Constants {

    const val TAG = "cameraX"
    const val FILE_NAME_FORMAT = "yy-MM-dd-HH-mm-ss-SSS"
    const val REQUEST_CODE_PERMISSIONS = 123
//    const val FILENAME = "photo.jpg"
     val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA,
         Manifest.permission.RECORD_AUDIO)



}
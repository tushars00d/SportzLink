package com


import android.app.Application
import com.cloudinary.android.MediaManager
import com.utils.CloudinaryConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Cloudinary (or any other SDK)
        initializeCloudinary()
    }

    private fun initializeCloudinary() {
        val config = mapOf(
            "cloud_name" to CloudinaryConfig.CLOUD_NAME,
            "api_key" to CloudinaryConfig.API_KEY
        )
        MediaManager.init(this, config)
    }
}
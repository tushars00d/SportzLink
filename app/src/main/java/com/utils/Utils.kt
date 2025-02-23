package com.utils

import android.content.Context
import android.net.Uri
import com.cloudinary.Cloudinary
import com.cloudinary.android.MediaManager
import com.cloudinary.android.callback.ErrorInfo
import com.cloudinary.android.callback.UploadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class CloudinaryConfig {
    companion object {
        // Get these from Cloudinary dashboard (https://cloudinary.com/console)
        const val CLOUD_NAME = "dhdt9o5n9" //cloudname
        const val API_KEY = "472651461187353"
        const val UPLOAD_PRESET = "sportzlink_upload_preset" // Create unsigned preset in dashboard
    }
}

// Initialize once in your Application class
fun initializeCloudinary(context: Context) {
    val config = mapOf(
        "cloud_name" to CloudinaryConfig.CLOUD_NAME,
        "api_key" to CloudinaryConfig.API_KEY
    )
    MediaManager.init(context, config)
}

suspend fun uploadImage(context: Context, uri: Uri, folderName: String): String {
    return suspendCancellableCoroutine { continuation ->
        MediaManager.get().upload(uri)
            .option("folder", folderName)
            .option("upload_preset", CloudinaryConfig.UPLOAD_PRESET)
            .callback(object : UploadCallback {
                override fun onStart(requestId: String?) {
                    TODO("Not yet implemented")
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                    TODO("Not yet implemented")
                }

                override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                    val secureUrl = resultData?.get("secure_url") as? String
                        ?: resultData?.get("url") as? String

                    secureUrl?.let {
                        continuation.resume(it)
                    } ?: continuation.resumeWithException(Exception("Upload failed: No URL returned"))
                }

                override fun onError(requestId: String?, error: ErrorInfo?) {
                    continuation.resumeWithException(
                        Exception(error?.description ?: "Unknown upload error")
                    )
                }

                override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long?) {}
                override fun onReschedule(requestId: String?, error: ErrorInfo?) {}
            })
            .dispatch()
    }
}
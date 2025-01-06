package com.paykids.presentation.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

object ImageMapper {
    fun Uri.toMultipartBody(context: Context, paramName: String): MultipartBody.Part {
        val inputStream: InputStream? = context.contentResolver.openInputStream(this)

        var fileName = ""
        var fileSize = -1L

        context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
            fileSize = cursor.getLong(sizeIndex)
        }

        val tempFile = File(context.cacheDir, fileName)
        inputStream?.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }

        val requestBody = tempFile.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(paramName, fileName, requestBody)
    }

}
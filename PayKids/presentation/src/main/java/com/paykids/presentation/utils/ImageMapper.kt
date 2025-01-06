package com.paykids.presentation.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream

object ImageMapper {
    // Uri를 File로 변환하는 확장 함수
    fun Uri.toFile(context: Context): File {
        val contentResolver = context.contentResolver
        val fileName = contentResolver.query(this, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            cursor.getString(nameIndex)
        } ?: "temp_image"

        val file = File(context.cacheDir, fileName)
        contentResolver.openInputStream(this)?.use { inputStream ->
            FileOutputStream(file).use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }

    // File을 MultipartBody.Part로 변환하는 함수
    fun createMultipartBodyPart(file: File, partName: String): MultipartBody.Part {
        val requestBody = RequestBody.create("image/*".toMediaType(), file)
        return MultipartBody.Part.createFormData(partName, file.name, requestBody)
    }
}
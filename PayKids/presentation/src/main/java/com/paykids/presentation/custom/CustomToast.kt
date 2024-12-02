package com.paykids.presentation.custom

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.paykids.presentation.R
import com.paykids.presentation.databinding.ToastCustomBinding

object CustomToast {
    fun makeToast(context: Context?, msg: String): Toast {
        val binding = ToastCustomBinding.inflate(LayoutInflater.from(context))
        binding.tvMsg.text = msg

        val toast = Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            setText(msg)
        }

        binding.root.background =
            ContextCompat.getDrawable(context!!, R.drawable.shape_toast_custom)

        return toast
    }
}
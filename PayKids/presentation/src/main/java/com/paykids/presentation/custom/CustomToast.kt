package com.paykids.presentation.custom

import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import com.paykids.presentation.databinding.ToastCustomBinding

object CustomToast {
    fun makeToast(context: Context?, msg: String): Toast {
        val binding = ToastCustomBinding.inflate(LayoutInflater.from(context))

        binding.tvMsg.text = msg

        return Toast(context).apply {
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }
}
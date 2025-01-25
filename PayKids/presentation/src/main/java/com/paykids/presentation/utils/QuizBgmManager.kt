package com.paykids.presentation.utils

import android.content.Context
import android.media.MediaPlayer
import com.paykids.presentation.R

object QuizBgmManager {
    private var mediaPlayer: MediaPlayer? = null

    fun startBgm(context: Context) {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(context, R.raw.quiz_bgm).apply {
                isLooping = true
                start()
            }
        }
    }

    fun stopBgm() {
        mediaPlayer?.apply {
            stop()
            release()
        }
        mediaPlayer = null
    }
}
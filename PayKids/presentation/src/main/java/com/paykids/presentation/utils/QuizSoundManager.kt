package com.paykids.presentation.utils

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import com.paykids.presentation.R

object QuizSoundManager {
    private var soundPool: SoundPool? = null
    private var soundMap = mutableMapOf<String, Int>()
    private var bgmPlayer: MediaPlayer? = null

    fun init(context: Context) {
        if (soundPool == null) {
            soundPool = SoundPool.Builder()
                .setMaxStreams(5)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .build()

            soundMap["correct"] = soundPool?.load(context, R.raw.coin1, 1) ?: 0
            soundMap["wrong"] = soundPool?.load(context, R.raw.error5, 1) ?: 0

            soundMap["game_level_complete"] = soundPool?.load(context, R.raw.game_level_complete, 1) ?: 0
            soundMap["cartoon_fail_trumpet"] = soundPool?.load(context, R.raw.cartoon_fail_trumpet, 1) ?: 0
        }

        if (bgmPlayer == null) {
            bgmPlayer = MediaPlayer.create(context, R.raw.quiz_bgm)
            bgmPlayer?.isLooping = true
        }
    }

    fun playBGM() {
        bgmPlayer?.start()
    }

    fun stopBGM() {
        bgmPlayer?.stop()
        bgmPlayer?.prepare()
    }

    fun playEffect(effect: String) {
        soundMap[effect]?.let { soundId ->
            soundPool?.play(soundId, 1f, 1f, 0, 0, 1f)
        }
    }

    // 게임 레벨 완료 시 효과음 재생
    fun playStageComplete() {
        playEffect("game_level_complete")
    }

    // 게임 실패 시 효과음 재생
    fun playStageFail() {
        playEffect("cartoon_fail_trumpet")
    }

    fun release() {
        bgmPlayer?.release()
        bgmPlayer = null
        soundPool?.release()
        soundPool = null
    }
}

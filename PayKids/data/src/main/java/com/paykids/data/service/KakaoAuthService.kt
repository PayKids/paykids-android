package com.paykids.data.service

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.SignInInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoAuthService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val client: UserApiClient,
) {

    companion object {
        const val KAKAO_TALK = "카카오톡"
        const val KAKAO_ACCOUNT = "카카오계정"
        const val KAKAO_ID_TOKEN = "카카오 ID 토큰"
    }

    private val isKakaoTalkLoginAvailable: Boolean
        get() = client.isKakaoTalkLoginAvailable(context)

    suspend fun signInWithKakao(): SignInInfo {
        return suspendCoroutine { continuation ->
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if (error != null) {
                    continuation.resumeWithException(error)
                } else if (token != null) {
                    val idToken = token.accessToken
                    val provider = AuthProvider.KAKAO
                    continuation.resume(SignInInfo(idToken, provider))
                }
            }

            if (isKakaoTalkLoginAvailable) {
                client.loginWithKakaoTalk(context, callback = callback)
            } else {
                client.loginWithKakaoAccount(context, callback = callback)
            }
        }
    }

    private fun signInSuccess(
        token: OAuthToken,
        signInListener: (String, AuthProvider) -> Unit // 수정된 부분
    ) {
        val idToken = token.accessToken
        val provider = AuthProvider.KAKAO
        signInListener.invoke(idToken, provider)
    }

    private fun signInError(error: Throwable) {
        Log.e("KakaoAuthService", "카카오 로그인 실패: ${error.message}")
    }

    fun signOut(signOutListener: ((Throwable?) -> Unit)? = null) {
        client.logout { error ->
            if (error != null) {
                Log.e("KakaoAuthService", "로그아웃 실패. SDK에서 토큰 삭제됨 $error")
            } else {
                Log.i("KakaoAuthService", "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
            signOutListener?.invoke(error)
        }
    }
}
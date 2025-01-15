package com.paykids.data.service

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.model.auth.SignInInfo
import com.paykids.util.LoggerUtils
import javax.inject.Inject
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class KakaoAuthService @Inject constructor(
    private val client: UserApiClient,
) {

    private fun Context.isKakaoTalkLoginAvailable(): Boolean =
        client.isKakaoTalkLoginAvailable(this)

    /**
    +     * 카카오 로그인을 수행합니다.
    +     * @throws KakaoSdkError SDK 초기화 또는 네트워크 오류 발생 시
    +     * @throws IllegalStateException 토큰 발급 실패 시
    +     * @return SignInInfo 로그인 성공 시 사용자 정보
    +     */
    suspend fun signInWithKakao(context: Context): SignInInfo {
        return suspendCoroutine { continuation ->
            val callback = createLoginCallback(context, continuation)

            if (context.isKakaoTalkLoginAvailable()) {
                loginWithKakaoTalk(context, callback)
            } else {
                loginWithKakaoAccount(context, callback)
            }
        }
    }

    private fun createLoginCallback(
        context: Context,
        continuation: Continuation<SignInInfo>
    ): (OAuthToken?, Throwable?) -> Unit {
        return { token, error ->
            if (error != null) {
                handleLoginError(context, error, continuation)
            } else if (token != null) {
                val idToken = token.idToken!!
                val provider = AuthProvider.KAKAO
                continuation.resume(SignInInfo(idToken, provider))
            } else {
                continuation.resumeWithException(IllegalStateException("토큰 발급 실패"))
            }
        }
    }

    private fun handleLoginError(
        context: Context,
        error: Throwable, continuation: Continuation<SignInInfo>
    ) {
        LoggerUtils.e("로그인 실패 $error")
        if (error.toString().contains("statusCode=302")) {
            LoggerUtils.e("카카오 계정 로그인 시도")
            loginWithKakaoAccount(context, createLoginCallback(context, continuation))
        }
        continuation.resumeWithException(error)
    }

    private fun loginWithKakaoTalk(context: Context, callback: (OAuthToken?, Throwable?) -> Unit) {
        client.loginWithKakaoTalk(context, callback = callback)
    }

    private fun loginWithKakaoAccount(
        context: Context,
        callback: (OAuthToken?, Throwable?) -> Unit
    ) {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
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

    fun withdraw(withdrawListener: ((Throwable?) -> Unit)? = null) {
        client.unlink { error ->
            if (error != null) {
                Log.e("KAKAO", "회원탈퇴 실패 $error")
            } else {
                Log.i("KAKAO", "회원탈퇴 성공")
            }
            withdrawListener?.invoke(error)
        }
    }
}
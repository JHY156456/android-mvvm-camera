package com.example.kotlin_random_chat.domain.signin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin_random_chat.api.RandomChatApi
import com.example.kotlin_random_chat.api.response.ApiResponse
import com.example.kotlin_random_chat.api.response.SigninResponse
import com.example.kotlin_random_chat.domain.auth.Auth
import kotlinx.coroutines.launch
import splitties.toast.toast
import java.lang.ref.WeakReference

class SigninViewModel(app: Application) : AndroidViewModel(app) {

    var navigatorRef: WeakReference<SigninNavigator>? = null

    private val navigator get() = navigatorRef?.get()

    var nickName = MutableLiveData("")

    /**
     * let : 가장 흔한 용례는 널이 될 수 잇는 값을 널이 아닌 값만 인자로 받는 함수에 넘기는경우
     * fun sendEmailTo(email : String) {}
     * email?.let{email -> sendEmailTo(email)}
     * == email?.let{sendEmailTo(it)}
     * email이 널이면 아무일도 일어나지 않는다
     *
     */
    fun signIn() = viewModelScope.launch {
        nickName.value?.let {
            runCatching {
                validateNickName(it)
                RandomChatApi.signin(it)
            }.onSuccess { response ->
                handleSignin(response)
            }.onFailure { e ->
                Log.e("SigninViewModel", "sign-in failure.", e)
                toast(e.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        }
    }

    private fun validateNickName(nickName: String) {
        require(nickName.trim().isNotEmpty()) {
            "닉네임 형식이 잘못되었습니다."
        }
    }

    private fun handleSignin(response: ApiResponse<SigninResponse>) {
        if (response.success && response.data != null) {
            val signin = response.data

            Auth.signin(signin.token,
                        signin.refreshToken,
                        signin.nickName)

            navigator?.startRandomChatActivity(response)
        } else {
            toast(response.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }

}

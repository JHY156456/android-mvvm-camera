package com.example.mvvmappapplication.domain.signin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.kotlin_random_chat.api.response.ApiResponse
import com.example.kotlin_random_chat.api.response.SigninResponse
import com.example.kotlin_random_chat.domain.auth.Auth
import com.example.kotlin_random_chat.domain.signin.SigninNavigator
import com.example.kotlin_random_chat.domain.signin.SigninViewModel
import com.example.mvvmappapplication.R
import com.example.mvvmappapplication.databinding.ActivitySigninBinding
import com.example.mvvmappapplication.domain.randomchat.RandomChatActivity
import splitties.activities.start
import java.lang.ref.WeakReference

/**
 * 닉네임 입력 후
 * 해당하는 닉네임중에 일치하는 채팅방 목록
 * 채팅방 선택 후, 선택한 채팅방으로 입장
 */
class SigninActivity : AppCompatActivity(), SigninNavigator {
    private val viewModel by lazy {
        ViewModelProvider(this)
            .get(SigninViewModel::class.java).also {
                it.navigatorRef = WeakReference(this)
            }
    }

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivitySigninBinding>(
            this,
            R.layout.activity_signin
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Auth.signout()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
    }

    override fun startRandomChatActivity(
        response: ApiResponse<SigninResponse>
    ) {
        start<RandomChatActivity>()
        finish()
    }

    override fun startChatListActivity(response: ApiResponse<SigninResponse>) {
        TODO("Not yet implemented")
        start<RandomChatActivity>()
        finish()
    }

}

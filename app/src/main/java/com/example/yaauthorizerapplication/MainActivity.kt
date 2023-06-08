package com.example.yaauthorizerapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import coil.load
import com.example.yaauthorizerapplication.databinding.ActivityMainBinding
import com.example.yaauthorizerapplication.ui.YaAuthViewModel
import com.yandex.authsdk.YandexAuthException
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthToken

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    val binding: ActivityMainBinding
        get() = _binding

    private lateinit var loginSdk: YandexAuthSdk
    private lateinit var loginOptionsBuilder: YandexAuthLoginOptions.Builder
    private lateinit var loginIntent: Intent

    private val REQUEST_LOGIN_SDK: Int = 1
    private lateinit var token: YandexAuthToken

    private val viewModel: YaAuthViewModel by viewModels(factoryProducer = { YaAuthViewModel.Factory })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        hideInfoFields() // While we do not have information about the user, it is not necessary to display it

        viewModel.userInfo.observe(this@MainActivity) {
            disableLogButton()
            showInfoFields()

            it.let {
                _binding.avatarImageView.load("https://avatars.yandex.net/get-yapic/${it.defaultAvatarId}/islands-200")
                _binding.fullNameTextView.text = it.realName
                _binding.emailTextView.text = it.defaultEmail


            }
        }

        setLoginButton()
    }

    private fun hideInfoFields() {
        _binding.avatarImageView.visibility = View.GONE
        _binding.fullNameTextView.visibility = View.GONE
        _binding.emailTextView.visibility = View.GONE
    }

    private fun showInfoFields() {
        _binding.avatarImageView.visibility = View.VISIBLE
        _binding.fullNameTextView.visibility = View.VISIBLE
        _binding.emailTextView.visibility = View.VISIBLE
    }

    private fun setLoginButton() {
        _binding.loginButton.setOnClickListener {
            loginSdk = (application as YaAuthApplication).container.loginSdk
            loginOptionsBuilder = YandexAuthLoginOptions.Builder()
            loginIntent = loginSdk.createLoginIntent(loginOptionsBuilder.build())

            startActivityForResult(loginIntent, REQUEST_LOGIN_SDK)
        }
    }

    private fun disableLogButton() {
        _binding.loginButton.isEnabled = false
        _binding.loginButton.text = getString(R.string.logged_in)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_LOGIN_SDK) {
            try {
                val token: YandexAuthToken? = loginSdk.extractToken(resultCode, data)
                if (token != null) {
                    this.token = token
                    viewModel.getUserInformation(token)
                }
            } catch (e: YandexAuthException) {
                e.message?.let { Log.e("Authorization", it) }
            }
        }

        super.onActivityResult(requestCode, resultCode, data)
    }
}
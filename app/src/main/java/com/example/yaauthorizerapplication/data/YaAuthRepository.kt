package com.example.yaauthorizerapplication.data

import com.yandex.authsdk.YandexAuthToken

interface YaAuthRepository {
    suspend fun getUserInfo(authToken: YandexAuthToken): UserInfo
}

class YaAuthNetworkRepository(private val authApiService: YaAuthApiService) : YaAuthRepository {
    override suspend fun getUserInfo(authToken: YandexAuthToken): UserInfo {
        val headerAuthToken: String = "OAuth ${authToken.value}"
        return authApiService.getUserInfo(headerAuthToken)
    }
}

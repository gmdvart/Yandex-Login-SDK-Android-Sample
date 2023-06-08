package com.example.yaauthorizerapplication.data

import android.content.Context
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header

interface AppDataContainer {
    val loginSdk: YandexAuthSdk
    val authRepository: YaAuthRepository
}

class YaAuthContainer(private val context: Context) : AppDataContainer {
    private val authOptions: YandexAuthOptions = YandexAuthOptions(context, true)
    override val loginSdk: YandexAuthSdk by lazy { YandexAuthSdk(context, authOptions) }

    private val BASE_URL = "https://login.yandex.ru/"

    private val moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl(BASE_URL)
        .build()

    private val yaAuthApiService: YaAuthApiService by lazy { retrofit.create(YaAuthApiService::class.java) }
    override val authRepository: YaAuthRepository by lazy { YaAuthNetworkRepository(yaAuthApiService) }
}

interface YaAuthApiService {
    @GET("info")
    suspend fun getUserInfo(@Header("Authorization") authToken: String): UserInfo
}
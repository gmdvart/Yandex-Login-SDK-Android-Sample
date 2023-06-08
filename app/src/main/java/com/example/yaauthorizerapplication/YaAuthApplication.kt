package com.example.yaauthorizerapplication

import android.app.Application
import com.example.yaauthorizerapplication.data.AppDataContainer
import com.example.yaauthorizerapplication.data.YaAuthContainer

class YaAuthApplication : Application() {
    private lateinit var _container: AppDataContainer
    val container: AppDataContainer
        get() = _container

    override fun onCreate() {
        super.onCreate()
        _container = YaAuthContainer(this)
    }
}
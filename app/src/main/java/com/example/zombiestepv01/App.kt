package com.example.zombiestepv01

import android.app.Application
import com.example.zombiestepv01.stepCounter.SharedPreferencesManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.instance.init(this)
    }
}
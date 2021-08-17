package com.example.ktp

import android.app.Application
import com.example.ktp.di.AppComponent
import com.example.ktp.di.AppModule
import com.example.ktp.di.DaggerAppComponent

class App : Application() {

    companion object{
        lateinit var daggerAppComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()

        daggerAppComponent = DaggerAppComponent.builder()
                .appModule(AppModule(applicationContext))
                .build()
    }
}
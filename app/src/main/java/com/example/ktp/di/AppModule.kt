package com.example.ktp.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(val applicationContext: Context) {
    @Provides
    fun provideContext(): Context {
        return applicationContext
    }
}
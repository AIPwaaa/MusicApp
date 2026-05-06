package com.example.music

import android.app.Application
import com.example.music.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MusicApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MusicApp)
            modules(appModule)
        }
    }
}
package com.example.newgonggong

import android.app.Application
import appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext
import org.koin.core.logger.Level

class GongGongApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        GlobalContext.startKoin{
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@GongGongApplication)
            modules(appModule)
        }
    }

}
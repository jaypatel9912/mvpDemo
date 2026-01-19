package com.mvpdemo

import android.app.Application
import com.mvpdemo.di.networkModule
import com.mvpdemo.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MVPDemoApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MVPDemoApp)
            modules(listOf(networkModule, repositoryModule))
        }
    }
}
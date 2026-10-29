package com.example.sonora

import android.app.Application
import com.example.sonora.core.di.AppContainer

/** Application do app: cria o AppContainer usado pelos ViewModels. */
class SonoraApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}

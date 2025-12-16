package ru.margarita9733.exoplayerdemo

import android.app.Application
import ru.margarita9733.exoplayerdemo.di.AppContainer

class ExoPlayerDemoApplication : Application() {
    val appContainer: AppContainer = AppContainer(this)
}
package ru.margarita9733.exoplayerdemo.di

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import ru.margarita9733.exoplayerdemo.data.AppRepositoryImpl

class AppContainer(private val appContext: Context) {
    private val dispatcherDefault = Dispatchers.Default

    private val applicationScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcherDefault)

    val appRepository = AppRepositoryImpl(applicationScope, appContext)
}

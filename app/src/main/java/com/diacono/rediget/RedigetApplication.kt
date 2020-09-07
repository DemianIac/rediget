package com.diacono.rediget

import android.app.Application
import com.diacono.rediget.network.networkModule
import com.diacono.rediget.reader.di.actionsModule
import com.diacono.rediget.reader.di.readerModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.fragment.koin.fragmentFactory
import org.koin.core.context.startKoin

class RedigetApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            fragmentFactory()
            androidContext(this@RedigetApplication)
            modules(listOf(networkModule,actionsModule,readerModule))
        }
    }
}
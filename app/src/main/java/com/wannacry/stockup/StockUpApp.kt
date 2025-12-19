package com.wannacry.stockup

import android.app.Application
import com.wannacry.stockup.di.appModule
//import com.wannacry.stockup.config.ExpiryAndLowStockWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class StockUpApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@StockUpApp)
            modules(appModule)
        }

        // Schedule daily worker
//        ExpiryAndLowStockWorker.schedule(this)
    }
}
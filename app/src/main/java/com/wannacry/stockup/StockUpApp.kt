package com.wannacry.stockup

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import com.wannacry.stockup.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

@RequiresApi(Build.VERSION_CODES.O)
class StockUpApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@StockUpApp)
            modules(appModule)
        }
    }
}
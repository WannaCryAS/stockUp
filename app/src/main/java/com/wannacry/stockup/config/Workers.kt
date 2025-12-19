//package com.wannacry.stockup.config
//
//
//import android.content.Context
//import android.os.Build
//import androidx.annotation.RequiresApi
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import androidx.work.PeriodicWorkRequestBuilder
//import androidx.work.WorkManager
//import androidx.work.ExistingPeriodicWorkPolicy
//import com.wannacry.stockup.domain.usecase.StockUpUseCase
//import org.koin.core.component.KoinComponent
//import org.koin.core.component.inject
//import java.time.LocalDate
//import java.util.concurrent.TimeUnit
//
///**
// * Worker that:
// * - Finds items expiring today or earlier, mark them expired
// * - Optionally notify user for low stock (left as TODO: require Notification util)
// *
// * Note: This worker uses Koin to get StockUpUseCase. To use Koin inside worker we implement KoinComponent.
// */
//class ExpiryAndLowStockWorker(
//    appContext: Context,
//    params: WorkerParameters
//) : CoroutineWorker(appContext, params), KoinComponent {
//
//    private val useCases: StockUpUseCase by inject()
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override suspend fun doWork(): Result {
//        try {
//            val today = LocalDate.now()
//            // get expiring items (suspend list)
//            val expiring = useCases.getExpiringOnOrBeforeSuspend(today)
//            expiring.forEach { item ->
//                useCases.markItemExpired(item.id)
//                // TODO: send notification per item if needed
//            }
//
//            // Optionally: check low stock and notify (not implemented, placeholder)
//            // val lowStock = useCases.observeLowStock().first()
//            // TODO: notify for lowStock
//
//            return Result.success()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return Result.failure()
//        }
//    }
//
//    companion object {
//        private const val WORK_NAME = "pantry_expiry_and_low_stock"
//
//        fun schedule(context: Context) {
//            val request = PeriodicWorkRequestBuilder<ExpiryAndLowStockWorker>(1, TimeUnit.DAYS).build()
//            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
//                WORK_NAME,
//                ExistingPeriodicWorkPolicy.KEEP,
//                request
//            )
//        }
//    }
//}
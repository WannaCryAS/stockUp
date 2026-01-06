package com.wannacry.stockup.di

import android.os.Build
import androidx.annotation.RequiresApi
import org.koin.dsl.module
import com.wannacry.stockup.domain.db.StockUpDatabase
import com.wannacry.stockup.domain.repo.category.CategoryRepository
import com.wannacry.stockup.domain.repo.category.CategoryRepositoryImpl
import com.wannacry.stockup.domain.repo.stockUp.StockUpRepository
import com.wannacry.stockup.domain.repo.stockUp.StockUpRepositoryImpl
import com.wannacry.stockup.domain.repo.task.TaskRepository
import com.wannacry.stockup.domain.repo.task.TaskRepositoryImpl
import com.wannacry.stockup.domain.usecase.CategoryUseCase
import com.wannacry.stockup.domain.usecase.StockUpUseCase
import com.wannacry.stockup.domain.usecase.TaskUseCase
import com.wannacry.stockup.presentation.viewmodel.BaseStockUpViewModel
import com.wannacry.stockup.presentation.viewmodel.SettingViewModel
import com.wannacry.stockup.presentation.viewmodel.TaskViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

@RequiresApi(Build.VERSION_CODES.O)
val dataModule = module {
    single { StockUpDatabase.getDatabase(get()) }
    single { get<StockUpDatabase>().categoryDao() }
    single { get<StockUpDatabase>().itemDao() }
    single { get<StockUpDatabase>().stockHistoryDao() }
    single { get<StockUpDatabase>().taskDao() }
    single { get<StockUpDatabase>().taskItemDao() }

    single<StockUpRepository> { StockUpRepositoryImpl(get(), get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<TaskRepository> { TaskRepositoryImpl(get(), get()) }
}

val domainModule = module {
    single { StockUpUseCase(get()) }
    single { CategoryUseCase(get()) }
    single { TaskUseCase(get()) }
}

@RequiresApi(Build.VERSION_CODES.O)
val presentationModule = module {
    viewModel { BaseStockUpViewModel(get(), get()) }
    viewModel { TaskViewModel(get(), get(), get()) }
    viewModel { SettingViewModel(get(), get()) }
}

@RequiresApi(Build.VERSION_CODES.O)
val appModule = listOf(dataModule, domainModule, presentationModule)

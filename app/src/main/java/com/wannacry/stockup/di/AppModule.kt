package com.wannacry.stockup.di

import org.koin.dsl.module
import com.wannacry.stockup.domain.db.StockUpDatabase
import com.wannacry.stockup.domain.repo.StockUpRepository
import com.wannacry.stockup.domain.repo.StockUpRepositoryImpl
import com.wannacry.stockup.domain.usecase.StockUpUseCase
import com.wannacry.stockup.presentation.viewmodel.StockUpViewModel
import org.koin.androidx.viewmodel.dsl.viewModel

val dataModule = module {
    single { StockUpDatabase.getDatabase(get()) }
    single { get<StockUpDatabase>().categoryDao() }
    single { get<StockUpDatabase>().itemDao() }
    single { get<StockUpDatabase>().stockHistoryDao() }

    single<StockUpRepository> { StockUpRepositoryImpl(get(), get(), get()) }
}

val domainModule = module {
    single { StockUpUseCase(get()) }
}

val presentationModule = module {
    viewModel { StockUpViewModel(get()) }
}

val appModule = listOf(dataModule, domainModule, presentationModule)
package com.oseren.estock.di

import com.oseren.estock.domain.repository.AuthRepository
import com.oseren.estock.data.repository.AuthRepositoryImpl
import com.oseren.estock.domain.repository.StockRepository
import com.oseren.estock.data.repository.StockRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class, SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindsAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun bindsStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository
}
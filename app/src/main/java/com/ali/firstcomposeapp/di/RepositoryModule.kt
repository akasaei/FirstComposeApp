package com.ali.firstcomposeapp.di

import com.ali.firstcomposeapp.data.repository.DefaultOrderRepository
import com.ali.firstcomposeapp.data.repository.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindOrderRepository(
        repository: DefaultOrderRepository
    ): OrderRepository
}
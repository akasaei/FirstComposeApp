package com.ali.firstcomposeapp.di

import android.content.Context
import androidx.room.Room
import com.ali.firstcomposeapp.data.local.AppDatabase
import com.ali.firstcomposeapp.data.local.dao.OrderDao
import com.ali.firstcomposeapp.data.local.dao.OrderItemDao
import com.ali.firstcomposeapp.data.local.dao.RemoteKeysDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "orders.db"
        ).build()

    @Provides
    fun provideOrderDao(
        database: AppDatabase
    ): OrderDao =
        database.orderDao()

    @Provides
    fun provideOrderItemDao(
        database: AppDatabase
    ): OrderItemDao =
        database.orderItemDao()

    @Provides
    fun provideRemoteKeysDao(
        database: AppDatabase
    ): RemoteKeysDao =
        database.remoteKeysDao()
}
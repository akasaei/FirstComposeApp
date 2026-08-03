package com.ali.firstcomposeapp.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.ali.firstcomposeapp.data.local.AppDatabase
import com.ali.firstcomposeapp.data.local.ORDERS_REMOTE_KEY
import com.ali.firstcomposeapp.data.local.OrderEntity
import com.ali.firstcomposeapp.data.local.RemoteKeysEntity
import com.ali.firstcomposeapp.data.local.dao.OrderDao
import com.ali.firstcomposeapp.data.local.dao.RemoteKeysDao
import com.ali.firstcomposeapp.data.mapper.toEntity
import com.ali.firstcomposeapp.data.remote.api.OrderApi

@OptIn(ExperimentalPagingApi::class)
class OrderRemoteMediator(

    private val database: AppDatabase,

    private val orderApi: OrderApi

) : RemoteMediator<Int, OrderEntity>() {

    val remoteKeysDao: RemoteKeysDao
        get() = database.remoteKeysDao()

    val orderDao: OrderDao
        get() = database.orderDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, OrderEntity>
    ): MediatorResult {

        val page = when (loadType) {

            LoadType.REFRESH -> {

                1
            }

            LoadType.PREPEND -> {

                return MediatorResult.Success(
                    endOfPaginationReached = true
                )
            }

            LoadType.APPEND -> {

                val remoteKey =
                    remoteKeysDao.getByLabel(
                        ORDERS_REMOTE_KEY
                    )

                remoteKey?.nextPage
                    ?: return MediatorResult.Success(
                        endOfPaginationReached = true
                    )
            }
        }


        return try {

            val response =
                orderApi.getOrders(
                    page = page,
                    limit = state.config.pageSize
                )

            val endReached =
                response.size < state.config.pageSize

            database.withTransaction {

                if (loadType == LoadType.REFRESH) {

                    orderDao.deleteAll()

                    remoteKeysDao.deleteByLabel(
                        ORDERS_REMOTE_KEY
                    )
                }

                orderDao.insertAll(
                    response.map {
                        it.toEntity()
                    }
                )

                remoteKeysDao.insertOrReplace(

                    RemoteKeysEntity(
                        label = ORDERS_REMOTE_KEY,
                        nextPage =
                            if (endReached)
                                null
                            else
                                page + 1
                    )

                )
            }

            MediatorResult.Success(
                endOfPaginationReached = endReached
            )

        } catch (e: Exception) {

            MediatorResult.Error(e)

        }

    }

}
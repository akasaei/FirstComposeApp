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

/**
 * [OrderRemoteMediator] implements the Paging 3 [RemoteMediator] API to handle loading paginated
 * order data from a remote network source ([OrderApi]) into a local database ([AppDatabase]).
 *
 * It manages the synchronization of data to ensure that the local cache remains a consistent
 * representation of the remote data, facilitating seamless offline support and efficient
 * list scrolling.
 *
 * @property database The local Room database instance used for transaction management and data access.
 * @property orderApi The retrofit service interface for fetching order data from the remote server.
 *
 * Workflow:
 * 1. **REFRESH**: Triggered on initial load or manual refresh. Clears local [OrderEntity] and
 *    [RemoteKeysEntity] data, then fetches the first page.
 * 2. **PREPEND**: Currently not supported; returns success with pagination end reached.
 */
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
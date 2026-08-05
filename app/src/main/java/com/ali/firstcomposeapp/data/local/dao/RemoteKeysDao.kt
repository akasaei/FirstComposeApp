package com.ali.firstcomposeapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ali.firstcomposeapp.data.local.RemoteKeysEntity

/**
 * Data Access Object (DAO) for managing [RemoteKeysEntity] instances in the local database.
 *
 * This DAO provides the necessary operations to store and retrieve pagination metadata,
 * typically used by a `RemoteMediator` in the Paging 3 library to keep track of the
 * next and previous page keys for network-to-local synchronization.
 */
@Dao
interface RemoteKeysDao {

    @Query(
        "SELECT * FROM remote_keys  WHERE label = :label LIMIT 1"
    )
    suspend fun getByLabel(
        label: String
    ): RemoteKeysEntity?

    @Insert(
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun insertOrReplace(
        remoteKeys: RemoteKeysEntity
    )

    @Query(
        "DELETE FROM remote_keys WHERE label = :label"
    )
    suspend fun deleteByLabel(
        label: String
    )

    @Query("DELETE FROM remote_keys")
    suspend fun deleteAll()
}
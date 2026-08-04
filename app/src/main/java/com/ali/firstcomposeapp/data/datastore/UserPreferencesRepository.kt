package com.ali.firstcomposeapp.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
){
    suspend fun saveLastSyncTime(
        timestamp: Long
    ) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.LAST_SYNC_TIME] = timestamp
        }
    }

    val lastSyncTime = dataStore.data.map { preferences ->
        preferences[PreferenceKeys.LAST_SYNC_TIME]
    }
}
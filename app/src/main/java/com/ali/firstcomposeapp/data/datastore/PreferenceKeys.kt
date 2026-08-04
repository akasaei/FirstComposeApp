package com.ali.firstcomposeapp.data.datastore

import androidx.datastore.preferences.core.longPreferencesKey

object PreferenceKeys {

    val LAST_SYNC_TIME =
        longPreferencesKey("last_sync_time")

}
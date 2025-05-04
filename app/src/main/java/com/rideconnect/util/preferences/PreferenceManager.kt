package com.rideconnect.util.preferences

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface PreferenceManager {
    suspend fun <T> getPreference(key: Preferences.Key<T>, defaultValue: T): Flow<T>
    suspend fun <T> setPreference(key: Preferences.Key<T>, value: T)
    suspend fun <T> removePreference(key: Preferences.Key<T>)
    suspend fun clearAllPreferences()
}

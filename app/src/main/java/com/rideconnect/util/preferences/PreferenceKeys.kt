package com.rideconnect.util.preferences

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceKeys {
    val AUTH_TOKEN = stringPreferencesKey("auth_token")
    val USER_ID = stringPreferencesKey("user_id")
}

//@JvmName("stringKey")
//public fun stringPreferencesKey(name: String): Preferences.Key<String> = Preferences.Key(name)
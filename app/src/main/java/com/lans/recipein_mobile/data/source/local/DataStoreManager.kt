package com.lans.recipein_mobile.data.source.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(private val context: Context) {
    companion object {
        val USER_ID = intPreferencesKey("USER_ID")
        val ACCESS_TOKEN = stringPreferencesKey("ACCESS_TOKEN")
        val TOKEN_EXPIRED_IN = longPreferencesKey("TOKEN_EXPIRED_IN")
        val REFRESH_TOKEN = stringPreferencesKey("REFRESH_TOKEN")
        private const val DATASTORE = "storeapp"
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE)

    suspend fun <T> storeData(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun clear() {
        context.dataStore.edit { preferences ->
            preferences.remove(USER_ID)
            preferences.remove(ACCESS_TOKEN)
            preferences.remove(REFRESH_TOKEN)
            preferences.remove(TOKEN_EXPIRED_IN)
        }
    }

    val userId: Flow<Int>
        get() = context.dataStore.data.map { preferences ->
            preferences[USER_ID] ?: 0
        }

    val accessToken: Flow<String>
        get() = context.dataStore.data.map { preferences ->
            preferences[ACCESS_TOKEN] ?: ""
        }

    val isTokenExpired: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            val expired = preferences[TOKEN_EXPIRED_IN]
            if(expired != null) {
                expired <= System.currentTimeMillis()
            }
            else {
                false
            }
        }

    val refreshToken: Flow<String>
        get() = context.dataStore.data.map { preferences ->
            preferences[REFRESH_TOKEN] ?: ""
        }
}
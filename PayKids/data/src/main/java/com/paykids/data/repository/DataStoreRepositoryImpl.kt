package com.paykids.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.paykids.domain.repository.DataStoreRepository
import com.paykids.util.LoggerUtils
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStore<Preferences>
) : DataStoreRepository {

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    override suspend fun clearData(): Result<Boolean> {
        return try {
            dataStorePreferences.edit { preferences ->
                preferences.clear()
            }
            LoggerUtils.d("DataStore cleared successfully.")
            Result.success(true)
        } catch (e: Exception) {
            LoggerUtils.e("Failed to clear DataStore: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun clearUserData(): Result<Boolean> {
        return try {
            dataStorePreferences.edit { preferences ->
                val keys = listOf(ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY)

                keys.forEach { key ->
                    preferences.remove(key)
                }
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setAccessToken(accessToken: String): Result<Boolean> {
        return try {
            dataStorePreferences.edit { preferences ->
                preferences[ACCESS_TOKEN_KEY] = accessToken
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAccessToken(): Result<String> {
        return try {
            val preferences = dataStorePreferences.data.first()
            val accessToken = preferences[ACCESS_TOKEN_KEY] ?: ""
            Result.success(accessToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun setRefreshToken(refreshToken: String): Result<Boolean> {
        return try {
            dataStorePreferences.edit { preferences ->
                preferences[REFRESH_TOKEN_KEY] = refreshToken
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getRefreshToken(): Result<String> {
        return try {
            val preferences = dataStorePreferences.data.first()
            val refreshToken = preferences[REFRESH_TOKEN_KEY] ?: ""
            Result.success(refreshToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
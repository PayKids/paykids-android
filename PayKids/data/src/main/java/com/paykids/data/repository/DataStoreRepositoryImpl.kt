package com.paykids.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.paykids.domain.enums.AuthProvider
import com.paykids.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStorePreferences: DataStore<Preferences>
) : DataStoreRepository {

    private companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        private val LOGIN_PROVIDER_KEY = stringPreferencesKey("login_provider")
    }

    override suspend fun clearData(): Result<Boolean> {
        return try {
            dataStorePreferences.edit { preferences ->
                preferences.clear()
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun clearUserData(): Result<Boolean> {
        return try {
            dataStorePreferences.edit { preferences ->
                val keys = listOf(ACCESS_TOKEN_KEY, REFRESH_TOKEN_KEY, LOGIN_PROVIDER_KEY)

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

    override suspend fun setAuthProvider(provider: AuthProvider): Result<Boolean> {
        return try {
            dataStorePreferences.edit { preferences ->
                preferences[LOGIN_PROVIDER_KEY] = provider.name
            }
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAuthProvider(): Result<AuthProvider> {
        return try {
            val preferences = dataStorePreferences.data.first()
            val providerName = preferences[LOGIN_PROVIDER_KEY] ?: ""
            val provider = AuthProvider.valueOf(providerName)
            Result.success(provider)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
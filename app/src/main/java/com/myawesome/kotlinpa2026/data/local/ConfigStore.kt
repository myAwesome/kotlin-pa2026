package com.myawesome.kotlinpa2026.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.configDataStore by preferencesDataStore(name = "config")

private const val DEFAULT_BASE_URL = "https://pa2021.solop.cc/api/"

@Singleton
class ConfigStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val baseUrlKey = stringPreferencesKey("base_url")

    val baseUrl: Flow<String> = context.configDataStore.data
        .map { it[baseUrlKey] ?: DEFAULT_BASE_URL }

    suspend fun saveBaseUrl(url: String) {
        context.configDataStore.edit { it[baseUrlKey] = url }
    }
}

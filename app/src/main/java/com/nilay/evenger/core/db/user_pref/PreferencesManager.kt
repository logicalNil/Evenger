package com.nilay.evenger.core.db.user_pref

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

data class FilterPreferences(
    val defPercentage: Int = 75,
)

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext val context: Context
) {
    private val Context.dataStore by preferencesDataStore(name = "user_pref")

    val preferenceFlow = context.dataStore.data.catch { exception ->
        if (exception is IOException) {
            emit(emptyPreferences())
        } else {
            throw exception
        }
    }.map { preferences ->
        val defPercentage = preferences[PreferenceKeys.defPercentage] ?: 75
        FilterPreferences(defPercentage)
    }

    suspend fun updatePercentage(defPercentage: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.defPercentage] = defPercentage
        }
    }

    private object PreferenceKeys {
        val defPercentage = intPreferencesKey("defPercentage")
    }
}
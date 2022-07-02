package com.example.to_docompose.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.to_docompose.data.models.Priority
import com.example.to_docompose.ui.util.CONSTANTS
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = CONSTANTS.PREFERENCE_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {
    private object MyPreferenceKeys{
        val sortOrderKey = stringPreferencesKey(name = CONSTANTS.PREFERENCE_KEY)
    }
    private val dataStore = context.datastore
    suspend fun saveSortOrder(priority: Priority){
        dataStore.edit {
            it[MyPreferenceKeys.sortOrderKey] = priority.name
        }
    }
    fun getSortOrder(): Flow<String> {
        val idk = dataStore.data
            .catch { exception: Throwable ->
                if(exception is IOException)
                    emit(emptyPreferences())
                else throw exception
            }
            .map { myPreference ->
                return@map myPreference[MyPreferenceKeys.sortOrderKey] ?: Priority.NONE.name
        }
        return idk
    }

}
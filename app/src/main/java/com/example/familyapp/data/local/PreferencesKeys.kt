package com.example.familyapp.data.local

import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val CURRENT_PROFILE_MODE = stringPreferencesKey("current_profile_mode")
    val CURRENT_CHILD_ID = stringPreferencesKey("current_child_id")
}
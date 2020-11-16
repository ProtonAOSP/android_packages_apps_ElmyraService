/*
 * Copyright (C) 2020 The Proton AOSP Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package org.protonaosp.elmyra.settings

import android.os.Bundle
import android.content.SharedPreferences
import androidx.preference.PreferenceFragment
import androidx.preference.PreferenceManager
import androidx.preference.ListPreference
import androidx.preference.SwitchPreference
import com.android.settings.widget.LabeledSeekBarPreference

import org.protonaosp.elmyra.R
import org.protonaosp.elmyra.getDePrefs
import org.protonaosp.elmyra.PREFS_NAME
import org.protonaosp.elmyra.getEnabled
import org.protonaosp.elmyra.getSensitivity
import org.protonaosp.elmyra.getAction
import org.protonaosp.elmyra.getActionName
import org.protonaosp.elmyra.getAllowScreenOff

// We need to use the "deprecated" PreferenceFragment to match Settings UI
// AppCompat won't fully match the native device default settings theme
@Suppress("DEPRECATION")
class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var prefs: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.setStorageDeviceProtected()
        preferenceManager.sharedPreferencesName = PREFS_NAME

        prefs = context.getDePrefs()
        prefs.registerOnSharedPreferenceChangeListener(this)
        updateUi()
    }

    override fun onDestroy() {
        super.onDestroy()
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences, key: String) {
        updateUi()
    }

    private fun updateUi() {
        // Enabled
        findPreference<SwitchPreference>(getString(R.string.pref_key_enabled))?.apply {
            setChecked(prefs.getEnabled(context))
        }

        // Sensitivity value
        findPreference<LabeledSeekBarPreference>(getString(R.string.pref_key_sensitivity))?.apply {
            progress = prefs.getSensitivity(context)
        }

        // Action value and summary
        findPreference<ListPreference>(getString(R.string.pref_key_action))?.apply {
            value = prefs.getAction(context)
            summary = prefs.getActionName(context)
        }

        // Screen state based on action
        findPreference<SwitchPreference>(getString(R.string.pref_key_allow_screen_off))?.apply {
            val screenForced = prefs.getBoolean(getString(R.string.pref_key_allow_screen_off_action_forced), false)
            setEnabled(!screenForced)
            if (screenForced) {
                setSummary(getString(R.string.setting_screen_off_blocked_summary))
                setPersistent(false)
                setChecked(false)
            } else {
                setSummary(getString(R.string.setting_screen_off_summary))
                setPersistent(true)
                setChecked(prefs.getAllowScreenOff(context))
            }
        }
    }
}

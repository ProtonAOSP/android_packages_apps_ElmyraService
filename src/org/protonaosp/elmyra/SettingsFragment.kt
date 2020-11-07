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

package org.protonaosp.elmyra

import android.os.Bundle
import android.content.SharedPreferences
import androidx.preference.PreferenceFragment
import androidx.preference.PreferenceManager
import androidx.preference.ListPreference
import androidx.preference.SwitchPreference

import org.protonaosp.elmyra.getDePrefs

// We need to use the "deprecated" PreferenceFragment to match Settings UI
@Suppress("DEPRECATION")
class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var prefs: SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
        val actionNames = resources.getStringArray(R.array.action_names)
        val actionValues = resources.getStringArray(R.array.action_values)

        val actionValue = prefs.getString(getString(R.string.pref_key_action),
                getString(R.string.default_action))
        val actionName = actionNames[actionValues.indexOf(actionValue)]
        findPreference<ListPreference>(getString(R.string.pref_key_action))?.setSummary(actionName)

        val screenPref = findPreference<SwitchPreference>(getString(R.string.pref_key_allow_screen_off))
        val screenForced = prefs.getBoolean(getString(R.string.pref_key_allow_screen_off_action_forced), false)
        screenPref?.setEnabled(!screenForced)
        if (screenForced) {
            screenPref?.setPersistent(false)
            screenPref?.setChecked(false)
        } else {
            val prefChecked = prefs.getBoolean(getString(R.string.pref_key_allow_screen_off),
                    resources.getBoolean(R.bool.default_allow_screen_off))
            screenPref?.setChecked(prefChecked)
            screenPref?.setPersistent(true)
        }
    }
}

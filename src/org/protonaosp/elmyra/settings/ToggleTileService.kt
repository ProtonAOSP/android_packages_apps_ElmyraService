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

import android.content.Context
import android.content.SharedPreferences
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.preference.PreferenceManager

import org.protonaosp.elmyra.R
import org.protonaosp.elmyra.getDePrefs
import org.protonaosp.elmyra.getEnabled

class ToggleTileService : TileService(), SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var prefs: SharedPreferences

    override fun onStartListening() {
        prefs = getDePrefs()
        prefs.registerOnSharedPreferenceChangeListener(this)
        update()
    }

    override fun onStopListening() {
        prefs.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun update(state: Boolean? = null) {
        qsTile.state = if (state ?: prefs.getEnabled(this)) Tile.STATE_ACTIVE else Tile.STATE_INACTIVE
        qsTile.updateTile()
    }

    override fun onClick() {
        val newState = !prefs.getEnabled(this)
        prefs.edit().putBoolean(getString(R.string.pref_key_enabled), newState).commit()
        update(newState)
    }

    override fun onSharedPreferenceChanged(prefs: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_key_enabled)) {
            update()
        }
    }
}

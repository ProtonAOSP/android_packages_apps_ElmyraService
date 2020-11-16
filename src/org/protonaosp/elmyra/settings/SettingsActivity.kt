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

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem

import org.protonaosp.elmyra.R

// Ugly, but there doesn't seem to be a better way
private const val SEARCH_REQUEST_CODE = 501
private const val INTELLIGENCE_PKG_NAME = "com.android.settings.intelligence"
private const val MENU_SEARCH = Menu.FIRST + 10

class SettingsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onNavigateUp(): Boolean {
        super.onNavigateUp()
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menu.add(Menu.NONE, MENU_SEARCH, 0, R.string.search_menu).apply {
            setIcon(R.drawable.ic_search_24dp)
            setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
            setOnMenuItemClickListener {
                val intent = Intent(Settings.ACTION_APP_SEARCH_SETTINGS).apply {
                    setPackage(INTELLIGENCE_PKG_NAME)
                }

                startActivityForResult(intent, SEARCH_REQUEST_CODE)
                return@setOnMenuItemClickListener true
            }
        }

        return true
    }
}

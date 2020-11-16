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

import android.content.ContentProvider
import android.content.ContentValues
import android.content.SharedPreferences
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import com.android.settingslib.drawer.TileUtils.META_DATA_PREFERENCE_SUMMARY

import org.protonaosp.elmyra.R
import org.protonaosp.elmyra.getDePrefs
import org.protonaosp.elmyra.getEnabled
import org.protonaosp.elmyra.getActionName

class SummaryProvider : ContentProvider() {
    private lateinit var prefs: SharedPreferences

    override fun onCreate(): Boolean {
        prefs = context.getDePrefs()
        return true
    }

    override fun call(method: String, uri: String, extras: Bundle?): Bundle {
        val bundle = Bundle()
        val summary = when (method) {
            "entry" -> if (prefs.getEnabled(context)) {
                context.getString(R.string.settings_entry_summary_on, prefs.getActionName(context))
            } else {
                context.getString(R.string.settings_entry_summary_off)
            }
            else -> throw IllegalArgumentException("Unknown method: $method")
        }

        bundle.putString(META_DATA_PREFERENCE_SUMMARY, summary)
        return bundle
    }

    override fun query(uri: Uri, projection: Array<String>, selection: String,
            selectionArgs: Array<String>, sortOrder: String): Cursor {
        throw UnsupportedOperationException()
    }

    override fun getType(uri: Uri): String {
        throw UnsupportedOperationException()
    }

    override fun insert(uri: Uri, values: ContentValues): Uri {
        throw UnsupportedOperationException()
    }

    override fun delete(uri: Uri, selection: String, selectionArgs: Array<String>): Int {
        throw UnsupportedOperationException()
    }

    override fun update(uri: Uri, values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        throw UnsupportedOperationException()
    }
}

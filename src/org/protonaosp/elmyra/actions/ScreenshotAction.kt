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

package org.protonaosp.elmyra.actions

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.PowerManager
import android.view.WindowManager

import com.android.internal.util.ScreenshotHelper

class ScreenshotAction(context: Context) : Action(context) {
    val helper = ScreenshotHelper(context)
    private val handler = Handler(Looper.getMainLooper())
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    override fun canRun() = pm.isInteractive()
    override fun canRunWhenScreenOff() = false

    override fun run() {
        helper.takeScreenshot(WindowManager.TAKE_SCREENSHOT_FULLSCREEN,
                true, true, WindowManager.ScreenshotSource.SCREENSHOT_OTHER,
                handler, null)
    }
}

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
import android.os.PowerManager
import android.os.SystemClock

class ScreenAction(context: Context) : Action(context) {
    val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager

    override fun run() {
        if (pm.isInteractive()) {
            pm.goToSleep(SystemClock.uptimeMillis(), PowerManager.GO_TO_SLEEP_REASON_POWER_BUTTON, 0)
        } else {
            pm.wakeUp(SystemClock.uptimeMillis(), PowerManager.WAKE_REASON_GESTURE, "org.protonaosp.elmyra:GESTURE")
        }
    }
}

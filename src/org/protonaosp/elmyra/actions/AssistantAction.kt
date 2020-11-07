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

import android.app.StatusBarManager
import android.content.Context
import android.os.Bundle
import android.os.ServiceManager

import com.android.internal.statusbar.IStatusBarService

class AssistantAction(context: Context) : Action(context) {
    val service = IStatusBarService.Stub.asInterface(ServiceManager.getService(Context.STATUS_BAR_SERVICE))

    override fun run() {
        service.startAssist(Bundle())
    }
}

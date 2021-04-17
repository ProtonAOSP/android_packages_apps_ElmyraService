/*
 * Copyright (C) 2020 The Proton AOSP Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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
import android.media.IAudioService
import android.media.AudioManager
import android.os.ServiceManager
import android.provider.Settings
import android.widget.Toast

class MuteAction(context: Context) : Action(context) {
    val service = IAudioService.Stub.asInterface(ServiceManager.getService(Context.AUDIO_SERVICE))

    override fun canRun() = context.resources.getBoolean(com.android.internal.R.bool.config_volumeHushGestureEnabled)

    override fun run() {
        // We can't call AudioService#silenceRingerModeInternal from here, so this is a partial copy of it
        var silenceRingerSetting = Settings.Secure.getInt(context.contentResolver,
                Settings.Secure.VOLUME_HUSH_GESTURE, Settings.Secure.VOLUME_HUSH_OFF)

        var ringerMode: Int
        var toastText: Int
        when (silenceRingerSetting) {
            Settings.Secure.VOLUME_HUSH_VIBRATE -> {
                ringerMode = AudioManager.RINGER_MODE_VIBRATE
                toastText = com.android.internal.R.string.volume_dialog_ringer_guidance_vibrate
            }
            // VOLUME_HUSH_MUTE and VOLUME_HUSH_OFF
            else -> {
                ringerMode = AudioManager.RINGER_MODE_SILENT
                toastText = com.android.internal.R.string.volume_dialog_ringer_guidance_silent
            }
        }

        service.setRingerModeInternal(ringerMode, "elmyra_hush")
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
    }
}

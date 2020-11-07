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
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Log

import org.protonaosp.elmyra.TAG

class FlashlightAction(context: Context) : Action(context) {
    private val handler = Handler(Looper.getMainLooper())
    private val cm = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val torchCamId = findCamera()
    private var available = true
    private var enabled = false

    private val torchCallback = object : CameraManager.TorchCallback() {
        override fun onTorchModeUnavailable(cameraId: String) {
            if (cameraId == torchCamId) {
                available = false
            }
        }

        override fun onTorchModeChanged(cameraId: String, newEnabled: Boolean) {
            if (cameraId == torchCamId) {
                available = true
                enabled = newEnabled
            }
        }
    }

    init {
        if (torchCamId != null) {
            cm.registerTorchCallback(torchCallback, handler)
        }
    }

    override fun canRun() = torchCamId != null && available

    override fun run() {
        try {
            cm.setTorchMode(torchCamId, !enabled)
        } catch (e: CameraAccessException) {
            Log.e(TAG, "Failed to set torch mode to $enabled", e)
            return
        }

        enabled = !enabled
    }

    override fun destroy() {
        if (torchCamId != null) {
            cm.unregisterTorchCallback(torchCallback)
        }
    }

    private fun findCamera(): String? {
        for (id in cm.cameraIdList) {
            val characteristics = cm.getCameraCharacteristics(id)
            val flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)
            val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)

            if (flashAvailable != null && flashAvailable && lensFacing != null &&
                    lensFacing == CameraCharacteristics.LENS_FACING_BACK) {
                return id
            }
        }

        return null
    }
}

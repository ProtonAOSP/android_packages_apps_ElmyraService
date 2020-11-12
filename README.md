# Active Edge Service

This is an open-source implementation of the Pixel Active Edge gesture, written from scratch for portability and customizability.

Active Edge, codename Elmyra, is a gesture powered by side grip sensors on the Pixel 2/XL, 3/XL, 3a/XL, and 4/XL that is used to activate the Google Assistant on stock. The gesture is handled by a proprietary nanoapp running under CHRE (ContextHub Runtime Environment) on the sensor DSP.

This app is a reverse-engineered Android client that runs as a standalone service and talks to the CHRE nanoapp for gesture functionality. No decompiled code has been used.

## Features

- Seamless integration in Settings → System → Gestures → Active Edge with no extra changes
- Integration with Settings search
- Many actions to perform on gesture trigger
  - Take screenshot
  - Open assistant
  - Open camera
  - Toggle flashlight
  - Mute calls & notifications (replicates default power + volume-up "prevent ringing" gesture)
  - Toggle power menu
  - Toggle screen
- 10 sensitivity levels (more granular than stock)
- Setting to control whether gesture is enabled when the screen is off
- Contextually-appropriate haptic feedback with modern effects
  - Heavy click for squeeze
  - Click for release
  - Reject for unavailable action (e.g. if flashlight can't turn on because camera is in use)

## Integration

Sync this repo to packages/apps/ElmyraService.

Add the following to your device tree **only for devices with this feature**:
```make
# Active Edge
PRODUCT_PACKAGES += \
    ElmyraService
```

While this service is designed to be as portable and self-contained as possible, Android does not provide the necessary APIs to implement all gesture actions out-of-the-box. This means that some commits must be added to frameworks/base to expose the APIs for full functionality:

- For screenshot action: [SystemUI: Allow privileged system apps to access screenshot service](https://github.com/ProtonAOSP/android_frameworks_base/commit/013c590411435569077228aacf1e246678c366ab)
- For assistant action: [core: Expose method to start assistant through Binder](https://github.com/ProtonAOSP/android_frameworks_base/commit/2b950e103e865aa6a1fe8a917964e0069d4c4037)

Default settings can be changed by overlaying [res/values/config.xml](https://github.com/ProtonAOSP/android_packages_apps_ElmyraService/blob/rvc/res/values/config.xml).

## License

All code in this repository is licensed under the Apache 2.0 License.

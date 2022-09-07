package com.github.botn365.wootingmovment;

import lombok.val;

import static com.github.botn365.main.WootingAnalogWrapper.*;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceEventType.WootingAnalog_DeviceEventType_Connected;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceEventType.WootingAnalog_DeviceEventType_Disconnected;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceType.WootingAnalog_DeviceType_Keyboard;

public class WootingInit {
    private static long deviceID;
    private static boolean initialised = false;

    public static void init() {
        if (wootingAnalogInitialise() > 0) {
            initialised = wootingAnalogIsInitialised();
            wootingAnalogSetKeycode(1);
            wootingAnalogSetDeviceEventCb((WootingAnalogDeviceEventType type, WootingAnalogDeviceInfoFFI ffi) -> {
                if (ffi.deviceId == deviceID) {
                    if (type.equals(WootingAnalog_DeviceEventType_Disconnected)) {
                        deviceID = -1;
                        initialised = pullForDevice();
                    }
                }
                if (ffi.deviceId == deviceID && type == WootingAnalog_DeviceEventType_Disconnected) {
                    deviceID = -1;
                    initialised = pullForDevice();
                } else if (deviceID == -1 && type.equals(WootingAnalog_DeviceEventType_Connected)) {
                    if (ffi.deviceType.equals(WootingAnalog_DeviceType_Keyboard)) {
                        deviceID = ffi.deviceId;
                        initialised = true;
                    }
                }
            });
            initialised = pullForDevice();
        }
    }

    private static boolean pullForDevice() {
        if (wootingAnalogIsInitialised()) {
            WootingAnalogDeviceInfoFFI[] devices = new WootingAnalogDeviceInfoFFI[4];
            val deviceCount = wootingAnalogGetConnectedDevicesInfo(devices);
            deviceID = -1;
            for (int i = 0; i < deviceCount; i++) {
                val device = devices[i];
                if (device.deviceType.equals(WootingAnalog_DeviceType_Keyboard)) {
                    deviceID = device.deviceId;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isInit() {
        return initialised;
    }

    public static long getDeviceID() {
        return deviceID;
    }
}

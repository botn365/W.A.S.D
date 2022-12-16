package com.github.botn365.wootingmovment;

import lombok.val;

import static com.github.botn365.main.WootingAnalogWrapper.*;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceEventType.WootingAnalog_DeviceEventType_Connected;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceEventType.WootingAnalog_DeviceEventType_Disconnected;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceType.WootingAnalog_DeviceType_Keyboard;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogResult.*;

public class WootingInit {
    private static long deviceID = -1;
    private static boolean initialised = false;

    public static WootingAnalogDeviceInfoFFI[] devices = new WootingAnalogDeviceInfoFFI[0];

    public static WootingAnalogResult error = WootingAnalogResult_DLLNotFound;

    public static void init() {
        val error = wootingAnalogInitialise();
        if (error > 0) {
            setCallBack();
            initialised = pullForDevice();
        }
        if (error == 0) {
            setCallBack();
            WootingInit.error = WootingAnalogResult_NoDevices;
        } else {
            WootingInit.error = WootingAnalogResult_Ok.fromInt(error);
        }
    }

    private static void setCallBack() {
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
                    WootingInit.error = WootingAnalogResult_Ok;
                }
            }
        });
    }

    private static boolean pullForDevice() {
        deviceID = -1;
        if (wootingAnalogIsInitialised()) {
            devices = new WootingAnalogDeviceInfoFFI[4];
            val deviceCount = wootingAnalogGetConnectedDevicesInfo(devices);
            for (int i = 0; i < deviceCount; i++) {
                val device = devices[i];
                if (device.deviceType.equals(WootingAnalog_DeviceType_Keyboard)) {
                    deviceID = device.deviceId;
                    error = WootingAnalogResult_Ok;
                    return true;
                }
            }
            error = WootingAnalogResult_NoDevices;
            return false;
        }
        error = WootingAnalogResult_UnInitialized;
        return false;
    }

    public static float[] oldValues = new float[]{0,0,0,0,0,0,0,0,0};

    public static boolean isInit() {
        return wootingAnalogIsInitialised() && initialised;
    }

    public static long getDeviceID() {
        return deviceID;
    }
}

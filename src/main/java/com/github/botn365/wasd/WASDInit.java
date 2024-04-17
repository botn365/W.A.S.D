package com.github.botn365.wasd;

import cpw.mods.fml.common.Loader;
import lombok.val;

import static com.github.botn365.main.WootingAnalogWrapper.*;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceEventType.WootingAnalog_DeviceEventType_Connected;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceEventType.WootingAnalog_DeviceEventType_Disconnected;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogDeviceType.WootingAnalog_DeviceType_Keyboard;
import static com.github.botn365.main.WootingAnalogWrapper.WootingAnalogResult.*;

public class WASDInit {
    private static long deviceID = -1;
    private static boolean initialised = false;

    public static WootingAnalogDeviceInfoFFI[] devices = new WootingAnalogDeviceInfoFFI[0];

    public static WootingAnalogResult error = WootingAnalogResult_Ok;

    public static void init() {
        Settings.IS_DWS_LOADED = Loader.isModLoaded("dws");
        int error = wootingAnalogIsInitialised() ? WootingAnalogResult_Ok.value : wootingAnalogInitialise();
        if (error > 0) {
            setCallBack();
            initialised = pullForDevice();
            return;
        }
        if (error == 0) {
            setCallBack();
            WASDInit.error = WootingAnalogResult_NoDevices;
        } else {
            WASDInit.error = fromInt(error);
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
                return;
            }
            if (ffi.deviceId == deviceID && type == WootingAnalog_DeviceEventType_Disconnected) {
                deviceID = -1;
                initialised = pullForDevice();
            } else if (deviceID == -1 && type.equals(WootingAnalog_DeviceEventType_Connected)) {
                if (ffi.deviceType.equals(WootingAnalog_DeviceType_Keyboard)) {
                    deviceID = ffi.deviceId;
                    initialised = true;
                    WASDInit.error = WootingAnalogResult_Ok;
                }
            } else {
                devices = new WootingAnalogDeviceInfoFFI[4];
                wootingAnalogGetConnectedDevicesInfo(devices);
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

    public static void setDeviceID(long id) {
        deviceID = id;
    }

    public static boolean setDeviceIDSave(long id) {
        for (val dev : devices) {
            if (dev != null && dev.deviceId == id) {
                deviceID = id;
                return true;
            }
        }
        return false;
    }
}

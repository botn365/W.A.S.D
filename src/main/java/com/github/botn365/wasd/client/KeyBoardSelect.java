package com.github.botn365.wasd.client;

import com.github.botn365.main.WootingAnalogWrapper;
import com.github.botn365.wasd.WASDInit;

public class KeyBoardSelect implements Selection {

    WootingAnalogWrapper.WootingAnalogDeviceInfoFFI keyBoard;

    public KeyBoardSelect(WootingAnalogWrapper.WootingAnalogDeviceInfoFFI keyBoard) {
        this.keyBoard = keyBoard;
    }

    @Override
    public String[] getText() {
        if (keyBoard == null) {
            return new String[]{"should not happen"};
        }
        return new String[] {
                keyBoard.deviceName,
                "ID:"+keyBoard.deviceId
        };
    }

    @Override
    public int length() {
        return 2;
    }

    @Override
    public boolean select() {
        return WASDInit.setDeviceIDSave(keyBoard.deviceId);
    }
}

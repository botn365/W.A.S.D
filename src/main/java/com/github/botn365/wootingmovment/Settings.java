package com.github.botn365.wootingmovment;

import com.github.botn365.wootingmovment.client.ResponseCurve;
import com.github.botn365.wootingmovment.client.ResponseGraphGui;
import lombok.val;

import java.util.ArrayList;

public class Settings {
    public final static ResponseCurve defaultCurve = new ResponseCurve();
    public final static ArrayList<ResponseCurve> movementCurves = new ArrayList<>();
    public final static ArrayList<ResponseCurve> flightCurves = new ArrayList<>();

    public static boolean movementEnabled = true;
    public static boolean fleightEnabled = true;
    public static boolean hotBarEnabled = true;

    public static State stateGlobal = State.Unified;
    public static State stateMovement = State.Unified;
    public static State stateFlight = State.Unified;

    public static int FORWARD = 0;
    public static int BACKWARD = 1;
    public static int LEFT = 2;
    public static int RIGHT = 3;
    public static int UP = 4;
    public static int DOWN = 5;

    static {
        addDefaultPoints(defaultCurve);
        for (int i = 0; i < 10; i++) {
            val c = new ResponseCurve();
            addDefaultPoints(c);
            if (i<7)
                movementCurves.add(c);
            else
                flightCurves.add(c);
        }
    }

    public static void addDefaultPoints(ResponseCurve curve) {
        curve.addPoint(0,0);
        curve.addPoint(0.33f,0.33f);
        curve.addPoint(0.66f,0.66f);
        curve.addPoint(1,1);
    }

    public static void resetPointsToDefault(ResponseCurve curve) {
        curve.getPoints().clear();
        addDefaultPoints(curve);
    }

    public static ResponseCurve getResponseCurve(int code) {
        if (stateGlobal == State.Unified) {
            return defaultCurve;
        } else {
            if (code < 4) {
                if (stateMovement == State.Unified) {
                    return movementCurves.get(0);
                } else if (stateMovement == State.XY) {
                    return movementCurves.get(1+(code/2));
                } else {
                    return movementCurves.get(3+code);
                }
            } else {
                if (stateFlight == State.Unified) {
                    return flightCurves.get(0);
                } else {
                    return flightCurves.get(code-3);
                }
            }
        }
    }



    public static enum State {
        Unified,
        Split,
        XY,
        Different,
        UpDown
        ;
    }

}

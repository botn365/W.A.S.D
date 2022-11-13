package com.github.botn365.wootingmovment;

import com.github.botn365.wootingmovment.client.ResponseCurve;
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

    public static enum State {
        Unified,
        Split,
        XY,
        Different,
        UpDown
        ;
    }

}

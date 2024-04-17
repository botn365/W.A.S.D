package com.github.botn365.wasd;

import lombok.val;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class Config {
    
    private static class Defaults {
        public static final String greeting = "Hello World";
    }

    private static class Categories {
        public static final String general = "general";
    }
    
    public static final String greeting = Defaults.greeting;

    public static final String defaultResponseCurve = "default_response_curve";

    public static final String defaultMovementResponseCurve = "default_movement_response_curve";
    public static final String xMovementResponseCurve = "x_movement_response_curve";
    public static final String yMovementResponseCurve = "y_movement_response_curve";
    public static final String forwardMovementResponseCurve = "forward_movement_response_curve";
    public static final String backwardMovementResponseCurve = "backward_movement_response_curve";
    public static final String leftMovementResponseCurve = "left_movement_response_curve";
    public static final String rightMovementResponseCurve = "right_movement_response_curve";

    public static final String defaultFlightResponseCurve = "default_flight_response_curve";

    public static final String upFlightResponseCurve = "up_flight_response_curve";
    public static final String downFlightResponseCurve = "down_flight_response_curve";


    public static final String curveSelection = "curve_selection";
    public static final String stateGlobal = "state_global";
    public static final String stateMovement = "state_movement";
    public static final String stateFlight = "state_flight";

    public static final String enableMovement = "enable_movement";
    public static final String enableFlight = "enable_flight";
    public static final String hotBarEnabled = "enable_hot_bar";

    public static final String lastDeviceID = "keyboard_device_id";

    public static final String upperThreshold = "upper_threshold";
    public static final String lowerThreshold = "lower_threshold";

    public static Configuration configuration;

    public static final String DEFAULT_CURVE = "[(0:0)(0.33:0.33)(0.66:0.66)(1:1)]";

    public static void syncronizeConfiguration(File configFile) {
        configuration = new Configuration(configFile);
        configuration.load();

        Settings.defaultCurve.fromString(configuration.get("responseCurves",defaultResponseCurve, DEFAULT_CURVE).getString());
        Settings.movementCurves.get(0).fromString(configuration.get("responseCurves",defaultMovementResponseCurve, DEFAULT_CURVE).getString());
        Settings.movementCurves.get(1).fromString(configuration.get("responseCurves",xMovementResponseCurve, DEFAULT_CURVE).getString());
        Settings.movementCurves.get(2).fromString(configuration.get("responseCurves",yMovementResponseCurve, DEFAULT_CURVE).getString());
        Settings.movementCurves.get(3).fromString(configuration.get("responseCurves",forwardMovementResponseCurve, DEFAULT_CURVE).getString());
        Settings.movementCurves.get(4).fromString(configuration.get("responseCurves",backwardMovementResponseCurve, DEFAULT_CURVE).getString());
        Settings.movementCurves.get(5).fromString(configuration.get("responseCurves",leftMovementResponseCurve, DEFAULT_CURVE).getString());
        Settings.movementCurves.get(6).fromString(configuration.get("responseCurves",rightMovementResponseCurve, DEFAULT_CURVE).getString());
        Settings.flightCurves.get(0).fromString(configuration.get("responseCurves",upFlightResponseCurve, DEFAULT_CURVE).getString());
        Settings.flightCurves.get(1).fromString(configuration.get("responseCurves",downFlightResponseCurve, DEFAULT_CURVE).getString());
        Settings.flightCurves.get(2).fromString(configuration.get("responseCurves", defaultFlightResponseCurve, DEFAULT_CURVE).getString());
        Settings.upperValueThreshold = (float) configuration.get("threshold",upperThreshold,Settings.upperValueThreshold).getDouble();
        Settings.lowerValueThreshold = (float) configuration.get("threshold",lowerThreshold,Settings.lowerValueThreshold).getDouble();

        Settings.stateGlobal = Settings.State.valueOf(configuration.get("curveSelection",stateGlobal, Settings.State.Unified.name()).getString());
        Settings.stateMovement = Settings.State.valueOf(configuration.get("curveSelection",stateMovement, Settings.State.Unified.name()).getString());
        Settings.stateFlight = Settings.State.valueOf(configuration.get("curveSelection",stateFlight, Settings.State.Unified.name()).getString());

        Settings.movementEnabled = configuration.get("enable/disable",enableMovement,true).getBoolean();
        Settings.fleightEnabled = configuration.get("enable/disable",enableFlight,true).getBoolean();
        Settings.hotBarEnabled = configuration.get("enable/disable",hotBarEnabled,true).getBoolean();

        try {
            val st = configuration.get("device", lastDeviceID,"-1").getString();
            Long devId = new Long(st);
            WASDInit.setDeviceIDSave(devId);
        } catch (NumberFormatException ignored) {}

        if(configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static void save() {

        configuration.get("responseCurves",defaultResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.defaultCurve.toString());
        configuration.get("responseCurves",defaultMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.movementCurves.get(0).toString());
        configuration.get("responseCurves",xMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.movementCurves.get(1).toString());
        configuration.get("responseCurves",yMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.movementCurves.get(2).toString());
        configuration.get("responseCurves",forwardMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.movementCurves.get(3).toString());
        configuration.get("responseCurves",backwardMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.movementCurves.get(4).toString());
        configuration.get("responseCurves",leftMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.movementCurves.get(5).toString());
        configuration.get("responseCurves",rightMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.movementCurves.get(6).toString());
        configuration.get("responseCurves",upFlightResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.flightCurves.get(0).toString());
        configuration.get("responseCurves",downFlightResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.flightCurves.get(1).toString());
        configuration.get("responseCurves", defaultFlightResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").set(Settings.flightCurves.get(2).toString());

        configuration.get("curveSelection",stateGlobal, Settings.State.Unified.toString()).set(Settings.stateGlobal.name());
        configuration.get("curveSelection",stateMovement, Settings.State.Unified.toString()).set(Settings.stateMovement.name());
        configuration.get("curveSelection",stateFlight, Settings.State.Unified.toString()).set(Settings.stateFlight.name());

        configuration.get("enable/disable",enableMovement,true).set(Settings.movementEnabled);
        configuration.get("enable/disable",enableFlight,true).set(Settings.fleightEnabled);
        configuration.get("enable/disable",hotBarEnabled,true).set(Settings.hotBarEnabled);

        configuration.get("device", lastDeviceID,"-1").set(Long.toString(WASDInit.getDeviceID()));

        configuration.get("threshold",upperThreshold,0.95f).set(Settings.upperValueThreshold);
        configuration.get("threshold",lowerThreshold,0.05f).set(Settings.lowerValueThreshold);

        configuration.save();

    }
}
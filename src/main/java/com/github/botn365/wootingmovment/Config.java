package com.github.botn365.wootingmovment;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

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

    public static Configuration configuration;

    public static void syncronizeConfiguration(File configFile) {
        configuration = new Configuration(configFile);
        configuration.load();

        Settings.defaultCurve.fromString(configuration.get("responseCurves",defaultResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.movementCurves.get(0).fromString(configuration.get("responseCurves",defaultMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.movementCurves.get(1).fromString(configuration.get("responseCurves",xMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.movementCurves.get(2).fromString(configuration.get("responseCurves",yMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.movementCurves.get(3).fromString(configuration.get("responseCurves",forwardMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.movementCurves.get(4).fromString(configuration.get("responseCurves",backwardMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.movementCurves.get(5).fromString(configuration.get("responseCurves",leftMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.movementCurves.get(6).fromString(configuration.get("responseCurves",rightMovementResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.flightCurves.get(0).fromString(configuration.get("responseCurves",upFlightResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.flightCurves.get(1).fromString(configuration.get("responseCurves",downFlightResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());
        Settings.flightCurves.get(2).fromString(configuration.get("responseCurves", defaultFlightResponseCurve,"[(0,0)(0.33:0.33)(0.66:0.66)(1,1)]").getString());

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

        configuration.save();

    }
}
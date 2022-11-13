package com.github.botn365.wootingmovment.client;

import lombok.AllArgsConstructor;
import lombok.val;

import java.util.ArrayList;

public class ResponseCurve {
    protected final ArrayList<Point> points = new ArrayList<>();

    public ArrayList<Point> getPoints() {
        return points;
    }

    public float translate(float input) {
        if (points.size() <= 1 || input < points.get(0).x) return 0;
        for (int i = 1; i < points.size(); i++) {
            val point = points.get(i);
            if (input <= point.x) {
                val prv = points.get(i-1);
                float diff = (input - prv.x)/(point.x - prv.x);
                return ((point.y - prv.y) * diff) + prv.y;
            }
        }
        return points.get(points.size()-1).y;
    }

    public void addPoint(float x, float y) {
        points.add(new Point(x,y));
    }

    @AllArgsConstructor
    public static class Point {
        public float x;
        public float y;
    }

}

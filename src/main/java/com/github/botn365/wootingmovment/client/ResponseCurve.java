package com.github.botn365.wootingmovment.client;

import lombok.AllArgsConstructor;
import lombok.val;
import lombok.var;

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

        @Override
        public String toString() {
            return "("+x+":"+y+")";
        }

        public int fromString(String input) {
            int pos1 = input.indexOf("(");
            int pos2 = input.indexOf(":");
            int pos3 = input.indexOf(")");
            if (pos1 >= pos2 || pos2 >= pos3) {
                return pos1+1;
            }
            try {
                var value = Float.valueOf(input.substring(pos1+1,pos2));
                x=value;
                value = Float.valueOf(input.substring(pos2+1,pos3));
                y=value;
            } catch (NumberFormatException e) {
                return pos3+1;
            }
            return pos3+1;
        }
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder("[");
        for (val point : points) {
            out.append(point.toString());
        }
        out.append("]");
        return out.toString();
    }

    public int fromString(String input) {
        int pos1 = input.indexOf("[");
        int pos2 = input.indexOf("]");
        if (pos1 >= pos2) {
            return pos1+1;
        }
        int index = 0;
        while (pos1 < pos2 && index < points.size()) {
            pos1 = points.get(index++).fromString(input.substring(pos1, pos2));
        }
        return pos2;
    }
}

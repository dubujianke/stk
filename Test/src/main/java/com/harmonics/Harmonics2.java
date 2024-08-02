package com.harmonics;

import java.util.ArrayList;
import java.util.List;

public class Harmonics2 {

    static double surfaceArea(double x, double y) {
        return Math.atan2(x * y, Math.sqrt(x * x + y * y + 1.0));
    }

    public static void main(String[] args) {
        double x = 4 * Math.PI;
        double a11 = surfaceArea(1, 1)/x;
        double a_1_1 = surfaceArea(-1, -1)/x;
        double a1_1 = surfaceArea(1, -1)/x;
        double a_11 = surfaceArea(-1, 1)/x;

        double a3 = surfaceArea(0, 0);

        System.out.println(a11*4*6);
    }
}

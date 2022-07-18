package com.util;

public class Time {
    public static double time_started = System.nanoTime();
    public static double get_time() {
        return (System.nanoTime() - time_started) * 1E-9;
    }

}

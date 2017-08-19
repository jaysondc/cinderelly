package com.shakeup.cinderelly;

/**
 * Created by Jayson on 8/18/2017.
 */

public class Utilities {

    /**
     * Converts a priority code to the appropriate string
     * @param priority is the integer representing priority
     * @return the string representation of the priority
     */
    public static String priorityToString(int priority) {
        switch (priority) {
            case 0: // Low
                return "LOW";
            case 1: // Medium
                return "MEDIUM";
            default: // High
                return "HIGH";

        }
    }
}

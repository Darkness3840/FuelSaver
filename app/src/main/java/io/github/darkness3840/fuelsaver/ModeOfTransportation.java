package io.github.darkness3840.fuelsaver;

public class ModeOfTransportation {
    public enum mode {
        none, gasCar, walk
    }
    private static mode currentMode;
    public static void setMode(mode m) {
        currentMode = m;
    }
    public static mode getMode() {
        return currentMode;
    }
}

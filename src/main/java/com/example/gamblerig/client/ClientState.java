package com.example.gamblerig.client;

public final class ClientState {
    public static volatile boolean enabled = false;
    public static volatile boolean hudVisible = true;   // R toggles this
    public static volatile String left = "none";
    public static volatile String right = "none";
    public static volatile String last = "-";
    public static volatile long lastStatusMs = 0;        // when the server last answered

    public static boolean connected() { return System.currentTimeMillis() - lastStatusMs < 3000; }

    private ClientState() {}
}

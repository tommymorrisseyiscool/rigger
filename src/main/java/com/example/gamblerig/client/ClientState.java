package com.example.gamblerig.client;

/** Local only: remembers the toggle so the menu button can show ON/OFF. */
public final class ClientState {
    public static volatile boolean enabled = false;
    private ClientState() {}
}

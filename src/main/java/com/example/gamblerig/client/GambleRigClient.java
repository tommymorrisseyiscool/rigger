package com.example.gamblerig.client;

import com.example.gamblerig.TogglePayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class GambleRigClient implements ClientModInitializer {
    // Faint green (ARGB). Lower the first two hex digits (0x50) to make it even harder to see.
    private static final int DOT_COLOR = 0x5000C800;

    @Override
    public void onInitializeClient() {
        KeyBinding.Category category = KeyBinding.Category.create(Identifier.of("gamblerig", "main"));
        KeyBinding menu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.gamblerig.menu", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, category));
        KeyBinding toggle = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.gamblerig.toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_X, category));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menu.wasPressed()) client.setScreen(new RigScreen());
            while (toggle.wasPressed() && client.player != null) {
                ClientState.enabled = !ClientState.enabled;
                ClientPlayNetworking.send(TogglePayload.INSTANCE);
            }
        });

        // Tiny faint dot in the bottom-right corner while enabled
        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.of("gamblerig", "dot"),
            (ctx, tickCounter) -> drawDot(ctx));
    }

    private static void drawDot(DrawContext ctx) {
        if (!ClientState.enabled) return;
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;
        int w = mc.getWindow().getScaledWidth();
        int h = mc.getWindow().getScaledHeight();
        ctx.fill(w - 6, h - 6, w - 3, h - 3, DOT_COLOR);   // 3x3 pixels
    }
}

package net.kyrptonaught.lceui.util;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.config.LCEConfigOptions;
import net.minecraft.server.MinecraftServer;

public class FadeOutTracker {
    private static final LCEConfigOptions config = LCEUIMod.getConfig();
    private static int timeToFadeOut;
    private static float fadeOutAmount;

    public static void init() {
        ServerTickEvents.END_SERVER_TICK.register(FadeOutTracker::tickFadeOut);
    }

    public float getFadeOutAmount() {
        return fadeOutAmount;
    }

    public static void resetTimeToFadeOut() {
        timeToFadeOut = config.idleInputTimeout;
        resetFadeOutAmount();
    }

    public static void resetFadeOutAmount() {
        fadeOutAmount = 1.0f;
    }

    public static void tickFadeOut(MinecraftServer server) {
        if (!config.fadeOut) {
            return;
        }
        if (timeToFadeOut > 0) {
            timeToFadeOut--;
            return;
        }
        if (timeToFadeOut == 0 && fadeOutAmount >= config.minFadeValue) {
            fadeOutAmount = fadeOutAmount - 0.05f;
        }
    }
}

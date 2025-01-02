package net.kyrptonaught.lceui.util;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.config.LCEConfigOptions;
import net.minecraft.client.MinecraftClient;

public class FadeOutTracker {
    private static final LCEConfigOptions config = LCEUIMod.getConfig();
    private static int timeToFadeOut;
    private static float fadeOutAmount;
    private static boolean fadeIn = false;

    public static void init() {
        ClientTickEvents.END_CLIENT_TICK.register(FadeOutTracker::tickFadeOut);
    }

    public static float getFadeOutAmount() {
        return fadeOutAmount;
    }

    public static void resetTimeToFadeOut() {
        timeToFadeOut = config.idleInputTimeout;
    }

    public static void resetFadeOutAmount() {
        fadeOutAmount = 1.0f; // Consider: Possibly allow user to set partially transparent reset value, why the user would do this, no idea.
    }

    public static void tryFadeIn() {
        if (fadeOutAmount < 1.0f) {
            fadeIn = true;
        }
    }

    public static void tickFadeOut(MinecraftClient client) {
        if (!config.fadeOut) {
            return;
        }
        if (!fadeIn) {
            if (timeToFadeOut > 0) {
                timeToFadeOut--;
                return;
            }
            if (timeToFadeOut == 0 && fadeOutAmount > config.minFadeValue) {
                fadeOutAmount = fadeOutAmount - 0.05f;
            }
        } else {
            resetFadeOutAmount();
            fadeIn = false;
            /*if (fadeOutAmount < 1.0f) { // TODO: Fade in too slow??
                fadeOutAmount = fadeOutAmount + 0.05f;
            } else { fadeIn = false;}*/
        }
    }
}

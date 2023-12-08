package net.kyrptonaught.lceui.compat;

import eu.midnightdust.midnightcontrols.client.MidnightControlsClient;
import eu.midnightdust.midnightcontrols.client.compat.CompatHandler;
import eu.midnightdust.midnightcontrols.client.compat.MidnightControlsCompat;
import eu.midnightdust.midnightcontrols.client.controller.ButtonBinding;
import eu.midnightdust.midnightcontrols.client.controller.PressAction;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.screens.LCECreativeInventoryScreen;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFW;

public class LCEMidnightControlsCompat implements CompatHandler {
    public static void register() {
        MidnightControlsCompat.registerCompatHandler(new LCEMidnightControlsCompat());
    }

    private static final PressAction CYCLE_LEFT = (client, button, value, action) -> {
        if (client.currentScreen instanceof LCECreativeInventoryScreen inventoryScreen) {
            inventoryScreen.incrementItemGroup(-1);
            return true;
        }
        return false;
    };

    private static final PressAction CYCLE_RIGHT = (client, button, value, action) -> {
        if (client.currentScreen instanceof LCECreativeInventoryScreen inventoryScreen) {
            inventoryScreen.incrementItemGroup(1);
            return true;
        }
        return false;
    };

    @Override
    public void handle(@NotNull MidnightControlsClient mod) {
        new ButtonBinding.Builder("key." + LCEUIMod.MOD_ID + ".controller.cycle_left")
                .buttons(GLFW.GLFW_KEY_LEFT_BRACKET)
                .action(CYCLE_LEFT)
                .cooldown()
                .category(ButtonBinding.INVENTORY_CATEGORY)
                .filter((client, buttonBinding) -> client.currentScreen instanceof LCECreativeInventoryScreen)
                .onlyInGame()
                .register();

        new ButtonBinding.Builder("key." + LCEUIMod.MOD_ID + ".controller.cycle_right")
                .buttons(GLFW.GLFW_KEY_RIGHT_BRACKET)
                .action(CYCLE_RIGHT)
                .cooldown()
                .category(ButtonBinding.INVENTORY_CATEGORY)
                .filter((client, buttonBinding) ->
                        client.currentScreen instanceof LCECreativeInventoryScreen
                )
                .register();
    }
}
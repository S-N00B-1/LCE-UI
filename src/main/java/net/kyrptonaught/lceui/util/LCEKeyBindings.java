package net.kyrptonaught.lceui.util;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class LCEKeyBindings {
    public static KeyBinding showDescription;
    public static KeyBinding openSecondaryInventory;

    public static void init() {
        showDescription = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lceui.whatsthis",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_N,
                "key.categories.lceui"
        ));
        openSecondaryInventory = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lceui.secondary_inventory",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_I,
                "key.categories.lceui"
        ));
    }
}

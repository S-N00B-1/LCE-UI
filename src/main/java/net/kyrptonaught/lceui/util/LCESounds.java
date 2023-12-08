package net.kyrptonaught.lceui.util;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LCESounds {
    public static final SoundEvent CLICK_STEREO = register("click_stereo");
    public static final SoundEvent UI_BACK = register("ui_back");

    private static SoundEvent register(String id) {
        return Registry.register(Registry.SOUND_EVENT, new Identifier(LCEUIMod.MOD_ID, id), new SoundEvent(new Identifier(LCEUIMod.MOD_ID, id)));
    }

    public static void init() {

    }
}

package net.kyrptonaught.lceui.util;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class LCESounds {
    public static final SoundEvent CLICK_STEREO = register("click_stereo");
    public static final SoundEvent UI_BACK = register("ui_back");
    public static final SoundEvent UI_MOVECURSOR = register("ui_movecursor");

    private static SoundEvent register(String id) {
        return Registry.register(Registries.SOUND_EVENT, new Identifier(LCEUIMod.MOD_ID, id), SoundEvent.of(new Identifier(LCEUIMod.MOD_ID, id)));
    }

    public static void init() {

    }
}

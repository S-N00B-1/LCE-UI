package net.kyrptonaught.lceui;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.fabricmc.api.ClientModInitializer;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.kyrptonaught.lceui.creativeinv.CustomItemGroup;
import net.kyrptonaught.lceui.whatsThis.WhatsThisInit;


public class LCEUIMod implements ClientModInitializer {
    public static final String MOD_ID = "lceui";

    public static ConfigManager configManager = new ConfigManager.MultiConfigManager(MOD_ID);

    public static ScalableSlot scalableSlotToDraw = null;

    @Override
    public void onInitializeClient() {
        MixinExtrasBootstrap.init();
        configManager.load();
        WhatsThisInit.init();
        CustomItemGroup.init();
    }
}
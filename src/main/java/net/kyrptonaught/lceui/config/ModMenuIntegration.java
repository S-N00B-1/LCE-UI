package net.kyrptonaught.lceui.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.BooleanItem;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return (screen) -> {
            LCEConfigOptions configOptions = LCEUIMod.getConfig();

            ConfigScreen configScreen = new ConfigScreen(screen, Text.translatable("options.lceui"));
            configScreen.setSavingEvent(() -> {
                LCEUIMod.configManager.save();
                if (MinecraftClient.getInstance().player != null)
                    LCEUIMod.syncConfig();
            });

            ConfigSection displaySection = new ConfigSection(configScreen, Text.translatable("options.lceui.general"));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.general.textShadows"), configOptions.closerTextShadows, true).setSaveConsumer(val -> configOptions.closerTextShadows = val));

            ConfigSection screensSection = new ConfigSection(configScreen, Text.translatable("options.lceui.screens"));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.general.creativeInventory"), configOptions.creativeInventory, true).setSaveConsumer(val -> configOptions.creativeInventory = val));

            return configScreen;
        };
    }
}
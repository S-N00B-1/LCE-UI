package net.kyrptonaught.lceui.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.BooleanItem;
import net.kyrptonaught.kyrptconfig.config.screen.items.number.IntegerItem;
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
            displaySection.addConfigItem(new IntegerItem(Text.translatable("options.lceui.general.hotbarScale"), configOptions.hotbarScale, 2).setSaveConsumer(val -> configOptions.hotbarScale = val));

            ConfigSection screensSection = new ConfigSection(configScreen, Text.translatable("options.lceui.screens"));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.creativeInventory"), configOptions.creativeInventory, true).setSaveConsumer(val -> configOptions.creativeInventory = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.survivalInventory"), configOptions.survivalInventory, true).setSaveConsumer(val -> configOptions.survivalInventory = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.chatWidth"), configOptions.chatWidth, true).setSaveConsumer(val -> configOptions.chatWidth = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.chatYPos"), configOptions.chatYPos, true).setSaveConsumer(val -> configOptions.chatYPos = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.recolorChat"), configOptions.recolorChat, true).setSaveConsumer(val -> configOptions.recolorChat = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.rescaleChatText"), configOptions.rescaleChatText, true).setSaveConsumer(val -> configOptions.rescaleChatText = val));

            return configScreen;
        };
    }
}
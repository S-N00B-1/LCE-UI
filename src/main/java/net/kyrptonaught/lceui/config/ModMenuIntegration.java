package net.kyrptonaught.lceui.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigScreen;
import net.kyrptonaught.kyrptconfig.config.screen.ConfigSection;
import net.kyrptonaught.kyrptconfig.config.screen.items.BooleanItem;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.titlescreen.LegacyPanoramaRenderer;
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
                MinecraftClient client = MinecraftClient.getInstance();
                if (client.player != null)
                    LCEUIMod.syncConfig();
            });

            ConfigSection displaySection = new ConfigSection(configScreen, Text.translatable("options.lceui.general"));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.general.textShadows"), configOptions.closerTextShadows, true).setSaveConsumer(val -> configOptions.closerTextShadows = val)).setToolTip(Text.translatable("options.lceui.general.textShadows.tooltip"));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.general.whatsThis"), configOptions.whatsThis, true).setSaveConsumer(val -> configOptions.whatsThis = val)).setToolTip(Text.translatable("options.lceui.general.whatsThis.tooltip"));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.general.tooltips"), configOptions.tooltips, true).setSaveConsumer(val -> configOptions.tooltips = val)).setToolTip(Text.translatable("options.lceui.general.tooltips.tooltip"));


            ConfigSection screensSection = new ConfigSection(configScreen, Text.translatable("options.lceui.screens"));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.chatWidth"), configOptions.chatWidth, true).setSaveConsumer(val -> configOptions.chatWidth = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.chatYPos"), configOptions.chatYPos, true).setSaveConsumer(val -> configOptions.chatYPos = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.recolorChat"), configOptions.recolorChat, true).setSaveConsumer(val -> configOptions.recolorChat = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.rescaleChatText"), configOptions.rescaleChatText, false).setSaveConsumer(val -> configOptions.rescaleChatText = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.hideHudWhenInUI"), configOptions.hideHudWhenInUI, true).setSaveConsumer(val -> configOptions.hideHudWhenInUI = val)).setToolTip(Text.translatable("options.lceui.screens.hideHudWhenInUI.tooltip"));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.removeTransparentBG"), configOptions.removeTransparentBG, true).setSaveConsumer(val -> configOptions.removeTransparentBG = val)).setToolTip(Text.translatable("options.lceui.screens.removeTransparentBG.tooltip"));


            ConfigSection panoramaSection = new ConfigSection(configScreen, Text.translatable("options.lceui.panorama"));
            panoramaSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.panorama.legacyPanorama"), configOptions.lcePan, true).setSaveConsumer(val -> configOptions.lcePan = val)).setToolTip(Text.translatable("options.lceui.panorama.legacyPanorama.tooltip"));
            panoramaSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.panorama.l4jpansupport"), configOptions.l4jPanSupport, true).setSaveConsumer(val -> {configOptions.l4jPanSupport = val; LegacyPanoramaRenderer.resetPanDimensions();})).setToolTip(Text.translatable("options.lceui.panorama.l4jpansupport.tooltip"));
            panoramaSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.panorama.renderPanoramaEverywhere"), configOptions.renderPanoramaEverywhere, false).setSaveConsumer(val -> configOptions.renderPanoramaEverywhere = val));

            return configScreen;
        };
    }
}
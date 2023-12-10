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
            displaySection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.general.renamedItemsHaveGoldName"), configOptions.renamedItemsHaveGoldName, true).setSaveConsumer(val -> configOptions.renamedItemsHaveGoldName = val));
            displaySection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.general.smallerItemsOutsideOfScalableSlots"), configOptions.smallerItemsOutsideOfScalableSlots, true).setSaveConsumer(val -> configOptions.smallerItemsOutsideOfScalableSlots = val));

            ConfigSection screensSection = new ConfigSection(configScreen, Text.translatable("options.lceui.screens"));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.creativeInventory"), configOptions.creativeInventory, true).setSaveConsumer(val -> configOptions.creativeInventory = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.survivalInventory"), configOptions.survivalInventory, true).setSaveConsumer(val -> configOptions.survivalInventory = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.classicCrafting"), configOptions.classicCrafting, true).setSaveConsumer(val -> configOptions.classicCrafting = val).setToolTip(Text.translatable("options.lceui.requires", Text.translatable("options.lceui.screens.survivalInventory"))));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.containerInventory"), configOptions.containerInventory, true).setSaveConsumer(val -> configOptions.containerInventory = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.container3x3Inventory"), configOptions.container3x3Inventory, true).setSaveConsumer(val -> configOptions.container3x3Inventory = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.chatWidth"), configOptions.chatWidth, true).setSaveConsumer(val -> configOptions.chatWidth = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.chatYPos"), configOptions.chatYPos, true).setSaveConsumer(val -> configOptions.chatYPos = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.recolorChat"), configOptions.recolorChat, true).setSaveConsumer(val -> configOptions.recolorChat = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.rescaleChatText"), configOptions.rescaleChatText, true).setSaveConsumer(val -> configOptions.rescaleChatText = val));
            screensSection.addConfigItem(new BooleanItem(Text.translatable("options.lceui.screens.sign"), configOptions.sign, true).setSaveConsumer(val -> configOptions.sign = val));

            return configScreen;
        };
    }
}
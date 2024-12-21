package net.kyrptonaught.lceui;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.kyrptonaught.kyrptconfig.config.ConfigManager;
import net.kyrptonaught.lceui.config.LCEConfigOptions;
import net.kyrptonaught.lceui.resourceloaders.TagResourceLoader;
import net.kyrptonaught.lceui.titlescreen.PanResourceReloader;
import net.kyrptonaught.lceui.util.LCEKeyBindings;
import net.kyrptonaught.lceui.whatsThis.WhatsThisInit;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;


// Hello.
// Before you look through rendering code,
// be warned that you may find some things weird.
// That's because Legacy Console Edition is
// inconsistent. To match its inconsistency, I
// have to use weirdly-sized textures and
// random divisions.
public class LCEUIMod implements ClientModInitializer {
    public static final String MOD_ID = "lceui";

    public static ConfigManager configManager = new ConfigManager.MultiConfigManager(MOD_ID);

    @Override
    public void onInitializeClient() {
        MixinExtrasBootstrap.init();
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new TagResourceLoader());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new PanResourceReloader());
        configManager.registerFile("config.json5", new LCEConfigOptions());
        configManager.load();
        LCEKeyBindings.init();
        WhatsThisInit.init();
    }


    public static void syncConfig() {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBoolean(getConfig().closerTextShadows);
        buf.writeBoolean(getConfig().whatsThis);
        buf.writeBoolean(getConfig().tooltips);

        buf.writeBoolean(getConfig().chatWidth);
        buf.writeBoolean(getConfig().chatYPos);
        buf.writeBoolean(getConfig().recolorChat);
        buf.writeBoolean(getConfig().rescaleChatText);

        buf.writeBoolean(getConfig().removeTransparentBG);
        buf.writeBoolean(getConfig().hideHudWhenInUI);

        buf.writeBoolean(getConfig().lceCrosshair);
        buf.writeFloat(getConfig().lceCrosshairOpacity);

        buf.writeBoolean(getConfig().lcePan);
        buf.writeBoolean(getConfig().renderPanoramaEverywhere);
        buf.writeBoolean(getConfig().l4jPanSupport);
        ClientPlayNetworking.send(new Identifier(LCEUIMod.MOD_ID, "sync_config_packet"), buf);
    }

    public static LCEConfigOptions getConfig() {
        return (LCEConfigOptions) configManager.getConfig("config.json5");
    }
}
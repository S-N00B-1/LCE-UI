package net.kyrptonaught.lceui.mixin.gui;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.titlescreen.LegacyPanoramaRenderer;
import net.kyrptonaught.lceui.titlescreen.PanoramaHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.kyrptonaught.lceui.mixin.titlescreen.TitleScreenMixin.*;

@Mixin(Screen.class)
public class BackgroundRendererMixin {
    @Shadow protected MinecraftClient client;

    private static final Identifier lceuiPan = PanoramaHolder.lceuiPan;
    private static final LegacyPanoramaRenderer PANORAMA_CUBE_MAP = PanoramaHolder.PANORAMA_CUBE_MAP;
    private static final Identifier l4jPan = PanoramaHolder.l4jPan;
    private static final LegacyPanoramaRenderer L4J_SUPPORT_PANORAMA_CUBE_MAP = PanoramaHolder.L4J_SUPPORT_PANORAMA_CUBE_MAP;


    @Inject(method = "renderInGameBackground", at=@At("HEAD"), cancellable = true)
    public void renderInGameBackground(DrawContext context, CallbackInfo ci) {
        if (this.client.currentScreen != null && LCEUIMod.getConfig().removeTransparentBG) {ci.cancel();}
    }

    @Inject(method = "renderBackgroundTexture", at=@At("HEAD"), cancellable = true)
    public void renderBackgroundTexture(DrawContext context, CallbackInfo ci) {
        if (this.client.currentScreen != null && LCEUIMod.getConfig().renderPanoramaEverywhere) {
            if (LCEUIMod.getConfig().l4jPanSupport && this.client.getResourceManager().getResource(l4jPan).isPresent()) {
                L4J_SUPPORT_PANORAMA_CUBE_MAP.render(context, context.getMatrices(), client.getTickDelta());
            } else if (this.client.getResourceManager().getResource(lceuiPan).isPresent()) {
                PANORAMA_CUBE_MAP.render(context, context.getMatrices(), client.getTickDelta());
            }
            ci.cancel();
        }
    }
}

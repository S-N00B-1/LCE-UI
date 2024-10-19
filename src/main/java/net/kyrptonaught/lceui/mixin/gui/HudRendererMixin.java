package net.kyrptonaught.lceui.mixin.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class HudRendererMixin {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "render", at=@At("HEAD"), cancellable = true)
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (client.currentScreen != null && !(client.currentScreen instanceof ChatScreen) && LCEUIMod.getConfig().hideHudWhenInUI) {
            RenderSystem.enableBlend();
            if (MinecraftClient.isFancyGraphicsOrBetter()) {
               renderVignetteOverlay(context, this.client.getCameraEntity());
            } else {
                RenderSystem.enableDepthTest();
            }
            ci.cancel();
        }
    }

    @Shadow private void renderVignetteOverlay(DrawContext context, Entity cameraEntity) {};
}

package net.kyrptonaught.lceui.mixin.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.entity.Entity;

import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudRendererMixin {
    @Shadow @Final private MinecraftClient client;
    @Shadow private void renderVignetteOverlay(DrawContext context, Entity cameraEntity) {}
    @Shadow @Final private BossBarHud bossBarHud;
    @Shadow private int ticks;

    @Shadow @Final private ChatHud chatHud;

    @Inject(method = "render", at=@At("HEAD"), cancellable = true)
    public void render(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (client.currentScreen != null && !(client.currentScreen instanceof ChatScreen) && LCEUIMod.getConfig().hideHudWhenInUI) {
            RenderSystem.enableBlend();
            if (MinecraftClient.isFancyGraphicsOrBetter()) {
               renderVignetteOverlay(context, this.client.getCameraEntity());
            } else {
                RenderSystem.enableDepthTest();
            }

            // While yes chat and bossbars should *technically* be hidden, actually, lem menu.
            this.client.getProfiler().push("bossHealth");
            this.bossBarHud.render(context);

            int n = MathHelper.floor(this.client.mouse.getX() * client.getWindow().getScaledWidth() / client.getWindow().getWidth());
            int p = MathHelper.floor(this.client.mouse.getY() * client.getWindow().getScaledHeight() / client.getWindow().getHeight());
            this.client.getProfiler().push("chat");
            this.chatHud.render(context, this.ticks, n, p);
            this.client.getProfiler().pop();

            ci.cancel();
        }
    }

}

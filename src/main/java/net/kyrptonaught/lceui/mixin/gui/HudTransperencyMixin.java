package net.kyrptonaught.lceui.mixin.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.util.FadeOutTracker;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudTransperencyMixin {

    /* TODO: Hud Transparency,this is going to be one messy mixin.
        //RenderSystem.setShaderColor(1.0f,1.0f,1.0f, FadeOutTracker.getFadeOutAmount());
         */
    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/gui/DrawContext;)V"))
    public void hotbarTransSet(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().fadeOut) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, FadeOutTracker.getFadeOutAmount());
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbar(FLnet/minecraft/client/gui/DrawContext;)V", shift = At.Shift.AFTER))
    public void hotbarTransReset(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().fadeOut) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasStatusBars()Z", shift = At.Shift.BEFORE))
    public void statusTransSet(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().fadeOut) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, FadeOutTracker.getFadeOutAmount());
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderMountHealth(Lnet/minecraft/client/gui/DrawContext;)V", shift = At.Shift.AFTER))
    public void statusTransReset(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().fadeOut) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getJumpingMount()Lnet/minecraft/entity/JumpingMount;", shift = At.Shift.BEFORE))
    public void expTransSet(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().fadeOut) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, FadeOutTracker.getFadeOutAmount());
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;getSleepTimer()I", shift = At.Shift.BEFORE))
    public void expTransReset(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().fadeOut) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/SubtitlesHud;render(Lnet/minecraft/client/gui/DrawContext;)V"))
    public void subTransSet(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().fadeOut) {
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, FadeOutTracker.getFadeOutAmount());
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderAutosaveIndicator(Lnet/minecraft/client/gui/DrawContext;)V", shift = At.Shift.AFTER))
    public void subTransReset(DrawContext context, float tickDelta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().fadeOut) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
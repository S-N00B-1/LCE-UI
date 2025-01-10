package net.kyrptonaught.lceui.mixin.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.util.FadeOutTracker;
import net.kyrptonaught.lceui.util.TransItemBuffer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class HudTransperencyMixin {

    private MinecraftClient client = MinecraftClient.getInstance();

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

    @WrapOperation(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V"))
    private void itemBufferRender(InGameHud instance, DrawContext context, int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed, Operation<Void> original) {
        if (LCEUIMod.getConfig().fadeOut) {
            TransItemBuffer.startItemBuffer();
        }

        original.call(instance,context,x,y,tickDelta,player,stack,seed);

        if (LCEUIMod.getConfig().fadeOut) {
            TransItemBuffer.endItemBuffer();

            //RenderSystem.enableBlend();
            //RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, FadeOutTracker.getFadeOutAmount());

            TransItemBuffer.drawItemBuffer(context);

            //RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
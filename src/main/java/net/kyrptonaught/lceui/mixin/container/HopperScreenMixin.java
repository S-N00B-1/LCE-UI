package net.kyrptonaught.lceui.mixin.container;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.screens.LCE3x3ContainerScreen;
import net.kyrptonaught.lceui.screens.LCEHopperScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HopperScreen.class)
public abstract class HopperScreenMixin extends HandledScreen<HopperScreenHandler> {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void setWidthAndHeight(HopperScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        if (LCEUIMod.getConfig().hopperInventory) {
            this.backgroundWidth = 430/3;
            this.backgroundHeight = 321/3;
            this.playerInventoryTitleY = this.backgroundHeight - 76;
        }
    }

    public HopperScreenMixin(HopperScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HopperScreen;renderBackground(Lnet/minecraft/client/gui/DrawContext;)V"))
    private void renderBackground(HopperScreen instance, DrawContext context, Operation<Void> original) {
        if (!LCEUIMod.getConfig().hopperInventory) original.call(instance, context);
    }

    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0))
    private void drawBackground1(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (LCEUIMod.getConfig().hopperInventory) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            LCEDrawableHelper.drawTexture(LCEHopperScreen.TEXTURE, instance, x, y, 0, 0, 430.0f/3.0f, 321.0f/3.0f, 512.0f/3.0f, 512.0f/3.0f);
        } else {
            original.call(instance, texture, x, y, u, v, width, height);
        }
    }
}

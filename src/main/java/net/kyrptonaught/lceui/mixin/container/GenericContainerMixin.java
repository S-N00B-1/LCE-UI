package net.kyrptonaught.lceui.mixin.container;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GenericContainerScreen.class)
public abstract class GenericContainerMixin extends HandledScreen<GenericContainerScreenHandler> {
    @Shadow @Final private int rows;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setWidthAndHeight(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        if (LCEUIMod.getConfig().containerInventory) {
            this.backgroundWidth = 430 / 3;
            this.backgroundHeight = (289 + this.rows * 42) / 3;
            this.playerInventoryTitleY = this.backgroundHeight - 76;
        }
    }

    public GenericContainerMixin(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/GenericContainerScreen;renderBackground(Lnet/minecraft/client/gui/DrawContext;)V"))
    private void renderBackground(GenericContainerScreen instance, DrawContext context, Operation<Void> original) {
        if (!LCEUIMod.getConfig().containerInventory) original.call(instance, context);
    }

    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0))
    private void drawBackground1(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (LCEUIMod.getConfig().containerInventory) {
            this.backgroundWidth = 430/3;
            this.backgroundHeight = (289 + this.rows * 42)/3;
            Identifier newTexture = new Identifier(LCEUIMod.MOD_ID, "textures/gui/container/generic_54.png");
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            LCEDrawableHelper.drawTexture(newTexture, instance, x, y, 0, 0, 430.0f/3.0f, (this.rows * 42 + 50.0f)/3.0f, 1024.0f/3.0f, 1024.0f/3.0f);
        } else {
            original.call(instance, texture, x, y, u, v, width, height);
        }
    }

    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 1))
    private void drawBackground2(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (LCEUIMod.getConfig().containerInventory) {
            Identifier newTexture = new Identifier(LCEUIMod.MOD_ID, "textures/gui/container/generic_54.png");
            LCEDrawableHelper.drawTexture(newTexture, instance, x, y - this.rows * 18 - 17 + this.rows * 42.0f/3.0f + 16 + 2.0f/3.0f, 0, 442, 430.0f/3.0f, 239.0f/3.0f, 1024.0f/3.0f, 1024.0f/3.0f);
        } else {
            original.call(instance, texture, x, y, u, v, width, height);
        }
    }
}

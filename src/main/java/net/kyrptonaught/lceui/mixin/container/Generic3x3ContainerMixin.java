package net.kyrptonaught.lceui.mixin.container;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.HandledScreenAccessor;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.screens.LCE3x3ContainerScreen;
import net.kyrptonaught.lceui.util.LCESounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Generic3x3ContainerScreen.class)
public abstract class Generic3x3ContainerMixin extends HandledScreen<Generic3x3ContainerScreenHandler> {
    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        MinecraftClient client = ((ScreenAccessor) ((Generic3x3ContainerScreen) (Object) this)).client();
        if (client != null) {
            if (LCEUIMod.getConfig().container3x3Inventory) {
                if (LCEUIMod.getConfig().compatibilityMode) client.getSoundManager().play(PositionedSoundInstance.master(LCESounds.CLICK_STEREO, 1.0f, 3.0f));
                else client.setScreen(new LCE3x3ContainerScreen((Generic3x3ContainerScreenHandler) ((HandledScreenAccessor<?>) this).handler(), client.player, ((ScreenAccessor) this).title()));
            }
        }
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setWidthAndHeight(Generic3x3ContainerScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        if (LCEUIMod.getConfig().container3x3Inventory) {
            this.backgroundWidth = 430/3;
            this.backgroundHeight = 405/3;
            this.playerInventoryTitleY = this.backgroundHeight - 76;
        }
    }

    public Generic3x3ContainerMixin(Generic3x3ContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/Generic3x3ContainerScreen;renderBackground(Lnet/minecraft/client/gui/DrawContext;)V"))
    private void renderBackground(Generic3x3ContainerScreen instance, DrawContext context, Operation<Void> original) {
        if (!LCEUIMod.getConfig().container3x3Inventory) original.call(instance, context);
    }

    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V", ordinal = 0))
    private void drawBackground1(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        if (LCEUIMod.getConfig().container3x3Inventory) {
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            LCEDrawableHelper.drawTexture(LCE3x3ContainerScreen.TEXTURE, instance, x, y, 0, 0, 430.0f/3.0f, 405.0f/3.0f, 512.0f/3.0f, 512.0f/3.0f);
        } else {
            original.call(instance, texture, x, y, u, v, width, height);
        }
    }
}

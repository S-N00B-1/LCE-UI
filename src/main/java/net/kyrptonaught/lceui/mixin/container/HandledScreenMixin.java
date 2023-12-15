package net.kyrptonaught.lceui.mixin.container;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.HandledScreenAccessor;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.screens.LCEContainerScreen;
import net.kyrptonaught.lceui.screens.LCEHopperScreen;
import net.kyrptonaught.lceui.util.LCESounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("ConstantConditions")
@Mixin(HandledScreen.class)
public class HandledScreenMixin {
    @Shadow protected int playerInventoryTitleY;

    @Shadow protected int backgroundWidth;

    @WrapOperation(method = "drawForeground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 0))
    private int drawForeground1(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow, Operation<Integer> original) {
        HandledScreen<?> handledScreen = (HandledScreen<?>) (Object) this;
        if (handledScreen instanceof GenericContainerScreen && LCEUIMod.getConfig().containerInventory) {
            return (int) LCEDrawableHelper.drawText(instance, textRenderer, text, 9 + 1.0f/3.0f, 7 + 1.0f/3.0f, 2.0f/3.0f, 0x373737);
        } else if ((handledScreen instanceof Generic3x3ContainerScreen && LCEUIMod.getConfig().container3x3Inventory)
                || (handledScreen instanceof HopperScreen && LCEUIMod.getConfig().hopperInventory)) {
            return (int) LCEDrawableHelper.drawCenteredText(instance, textRenderer, text, this.backgroundWidth / 2, this.backgroundWidth / 2, 10 + 1.0f/3.0f, 10 + 1.0f/3.0f, 2.0f/3.0f, 0x373737);
        }
        return original.call(instance, textRenderer, text, x, y, color, shadow);
    }

    @WrapOperation(method = "drawForeground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 1))
    private int drawForeground2(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow, Operation<Integer> original) {
        HandledScreen<?> handledScreen = (HandledScreen<?>) (Object) this;
        if (handledScreen instanceof GenericContainerScreen && LCEUIMod.getConfig().containerInventory) {
            return (int) LCEDrawableHelper.drawText(instance, textRenderer, text, 9 + 1.0f/3.0f, this.playerInventoryTitleY, 2.0f/3.0f, 0x373737);
        } else if ((handledScreen instanceof Generic3x3ContainerScreen && LCEUIMod.getConfig().container3x3Inventory)
                || (handledScreen instanceof HopperScreen && LCEUIMod.getConfig().hopperInventory)) {
            return (int) LCEDrawableHelper.drawText(instance, textRenderer, text, 9 + 1.0f/3.0f, this.playerInventoryTitleY + 2 + 1.0f/3.0f, 2.0f/3.0f, 0x373737);
        }
        return original.call(instance, textRenderer, text, x, y, color, shadow);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        MinecraftClient client = ((ScreenAccessor) this).client();
        if (client != null) {
            HandledScreen<?> handledScreen = (HandledScreen<?>) (Object) this;
            if (LCEUIMod.getConfig().compatibilityMode && isReplacedInCompatibilityMode(handledScreen)) {
                client.getSoundManager().play(PositionedSoundInstance.master(LCESounds.CLICK_STEREO, 1.0f, 3.0f));
            } else if (handledScreen instanceof GenericContainerScreen && LCEUIMod.getConfig().containerInventory) {
                client.setScreen(new LCEContainerScreen((GenericContainerScreenHandler) ((HandledScreenAccessor<?>) this).handler(), ((ScreenAccessor) this).client().player, ((ScreenAccessor) this).title()));
            } else if (handledScreen instanceof HopperScreen && LCEUIMod.getConfig().hopperInventory) {
                client.setScreen(new LCEHopperScreen((HopperScreenHandler) ((HandledScreenAccessor<?>) this).handler(), ((ScreenAccessor) this).client().player, ((ScreenAccessor) this).title()));
            }
        }
    }

    @Inject(method = "removed", at = @At("TAIL"))
    private void removed(CallbackInfo ci) {
        MinecraftClient client = ((ScreenAccessor) this).client();
        if (client != null && LCEUIMod.getConfig().compatibilityMode && isReplacedInCompatibilityMode((HandledScreen<?>) (Object) this)) {
            client.getSoundManager().play(PositionedSoundInstance.master(LCESounds.UI_BACK, 1.0f, 3.0f));
        }
    }

    @Unique
    private static boolean isReplacedInCompatibilityMode(HandledScreen<?> handledScreen) {
        return handledScreen instanceof GenericContainerScreen ||
                handledScreen instanceof Generic3x3ContainerScreen ||
                handledScreen instanceof InventoryScreen ||
                handledScreen instanceof HopperScreen;
    }
}

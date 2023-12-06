package net.kyrptonaught.lceui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.ScalableSlot;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class ScalableSlotMixin {
    @Mixin(HandledScreen.class)
    private interface HandledScreenAccessor {
        @Accessor("x")
        int x();

        @Accessor("y")
        int y();
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/util/math/MatrixStack;III)V"))
    private void drawSlotHighlightRedirect(MatrixStack matrices, int x, int y, int z, @Local(name = "slot") Slot slot) {
        if (slot instanceof ScalableSlot scalableSlot) {
            matrices.translate(scalableSlot.floatX, scalableSlot.floatY, z);
            matrices.scale(scalableSlot.scale, scalableSlot.scale, scalableSlot.scale);
            HandledScreen.drawSlotHighlight(matrices, 0, 0, 0);
            matrices.scale(1.0f/scalableSlot.scale, 1.0f/scalableSlot.scale, 1.0f/scalableSlot.scale);
            matrices.translate(-scalableSlot.floatX, -scalableSlot.floatY, -z);
        } else {
            HandledScreen.drawSlotHighlight(matrices, x, y, z);
        }
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V"))
    private void fill(MatrixStack matrixStack, int x, int y, int maxX, int maxY, int color, @Local(name = "slot") Slot slot) {
        if (slot instanceof ScalableSlot scalableSlot) {
            matrixStack.translate(x, y, 0);
            matrixStack.scale(scalableSlot.scale, scalableSlot.scale, 0);
            HandledScreen.fill(matrixStack, 0, 0, 16, 16, color);
            matrixStack.scale(1.0f/scalableSlot.scale, 1.0f/scalableSlot.scale, 0);
            matrixStack.translate(-x, -y, 0);
        } else {
            HandledScreen.fill(matrixStack, x, y, maxX, maxY, color);
        }
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderInGuiWithOverrides(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V"))
    private void setScalableSlotToDraw(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        if (slot instanceof ScalableSlot scalableSlot) {
            ScalableSlot.scalableSlotToDraw = scalableSlot;
        }
    }

    @Inject(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", shift = At.Shift.AFTER))
    private void unsetScalableSlotToDraw(MatrixStack matrices, Slot slot, CallbackInfo ci) {
        ScalableSlot.scalableSlotToDraw = null;
    }

    @Inject(method = "isPointOverSlot", at = @At("RETURN"), cancellable = true)
    private void isPointOverSlot(Slot slot, double pointX, double pointY, CallbackInfoReturnable<Boolean> cir) {
        if (slot instanceof ScalableSlot scalableSlot) {
            cir.setReturnValue(isPointWithinBounds(((HandledScreenAccessor)((HandledScreen)(Object)this)).x(), ((HandledScreenAccessor)((HandledScreen)(Object)this)).y(), scalableSlot.floatX, scalableSlot.floatY, 16 * scalableSlot.scale, 16 * scalableSlot.scale, pointX, pointY));
        }
    }

    @Unique
    private static boolean isPointWithinBounds(int guiX, int guiY, float x, float y, float width, float height, double pointX, double pointY) {
        return (pointX -= (double) guiX) >= (double)(x - 1) && pointX < (double)(x + width + 1) && (pointY -= (double) guiY) >= (double)(y - 1) && pointY < (double)(y + height + 1);
    }
}

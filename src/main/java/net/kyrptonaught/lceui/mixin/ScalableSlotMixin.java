package net.kyrptonaught.lceui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(HandledScreen.class)
public class ScalableSlotMixin {
    @Shadow protected int x;

    @Shadow protected int y;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;drawSlotHighlight(Lnet/minecraft/client/gui/DrawContext;III)V"))
    private void drawSlotHighlightRedirect(DrawContext context, int x, int y, int z, @Local(name = "slot") Slot slot) {
        MatrixStack matrices = context.getMatrices();
        if (slot instanceof ScalableSlot scalableSlot) {
            matrices.translate(scalableSlot.floatX, scalableSlot.floatY, z);
            matrices.scale(scalableSlot.scale, scalableSlot.scale, scalableSlot.scale);
            HandledScreen.drawSlotHighlight(context, 0, 0, 0);
            matrices.scale(1.0f/scalableSlot.scale, 1.0f/scalableSlot.scale, 1.0f/scalableSlot.scale);
            matrices.translate(-scalableSlot.floatX, -scalableSlot.floatY, -z);
        } else {
            HandledScreen.drawSlotHighlight(context, x, y, z);
        }
    }

    @Redirect(method = "drawSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void fill(DrawContext context, int x, int y, int maxX, int maxY, int color, @Local(name = "slot") Slot slot) {
        MatrixStack matrices = context.getMatrices();
        if (slot instanceof ScalableSlot scalableSlot) {
            matrices.translate(x, y, 0);
            matrices.scale(scalableSlot.scale, scalableSlot.scale, 0);
            context.fill(0, 0, 16, 16, color);
            matrices.scale(1.0f/scalableSlot.scale, 1.0f/scalableSlot.scale, 0);
            matrices.translate(-x, -y, 0);
        } else {
            context.fill(x, y, maxX, maxY, color);
        }
    }

    @Inject(method = "drawSlot", at = @At("HEAD"))
    private void setScalableSlotToDraw(DrawContext context, Slot slot, CallbackInfo ci) {
        if (slot instanceof ScalableSlot scalableSlot) {
            ScalableSlot.scalableSlotToDraw = scalableSlot;
        }
    }

    @Inject(method = "drawSlot", at = @At("TAIL"))
    private void unsetScalableSlotToDraw(DrawContext context, Slot slot, CallbackInfo ci) {
        ScalableSlot.scalableSlotToDraw = null;
    }

    @Inject(method = "isPointOverSlot", at = @At("RETURN"), cancellable = true)
    private void isPointOverSlot(Slot slot, double pointX, double pointY, CallbackInfoReturnable<Boolean> cir) {
        if (slot instanceof ScalableSlot scalableSlot) {
            cir.setReturnValue(isPointWithinBounds(this.x, this.y, scalableSlot.floatX, scalableSlot.floatY, 16 * scalableSlot.scale, 16 * scalableSlot.scale, pointX, pointY));
        }
    }

    @Unique
    private static boolean isPointWithinBounds(int guiX, int guiY, float x, float y, float width, float height, double pointX, double pointY) {
        return (pointX -= guiX) >= (x - 1.0) && pointX < (x + width + 1.0) && (pointY -= guiY) >= (y - 1.0) && pointY < (y + height + 1.0);
    }
}

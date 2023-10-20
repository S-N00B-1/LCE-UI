package net.kyrptonaught.lceui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemRenderer.class)
public class ScalableSlotItemRendererMixin {
    @ModifyArg(method = "renderGuiItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V"), index = 3)
    private MatrixStack scaleMatrixBasedOnSlot1(MatrixStack matrixStack, @Local(name = "x") int x, @Local(name = "y") int y) {
        if (LCEUIMod.scalableSlotToDraw != null) {
            matrixStack.translate(LCEUIMod.scalableSlotToDraw.scale - 1, 1 - LCEUIMod.scalableSlotToDraw.scale, 0);
            matrixStack.scale(LCEUIMod.scalableSlotToDraw.itemScale, LCEUIMod.scalableSlotToDraw.itemScale, 1.0f);
        }
        return matrixStack;
    }
}

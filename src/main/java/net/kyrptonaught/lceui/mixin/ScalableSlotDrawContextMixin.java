package net.kyrptonaught.lceui.mixin;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(DrawContext.class)
public abstract class ScalableSlotDrawContextMixin {
    @Shadow public abstract MatrixStack getMatrices();

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V"))
    private void scaleMatrixBasedOnSlot(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo ci) {
        MatrixStack matrices = this.getMatrices();
        if (ScalableSlot.scalableSlotToDraw != null) {
            matrices.translate(ScalableSlot.scalableSlotToDraw.scale / 2 - 0.5f,  0.5f - ScalableSlot.scalableSlotToDraw.scale / 2, 0.0f);
            matrices.scale(ScalableSlot.scalableSlotToDraw.itemScale, ScalableSlot.scalableSlotToDraw.itemScale, 1.0f);
        } else if (LCEUIMod.getConfig().smallerItemsOutsideOfScalableSlots) {
            float itemScale = 7.0f/8.0f;
            matrices.scale(itemScale, itemScale, 1.0f);
        }
    }

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/world/World;Lnet/minecraft/item/ItemStack;IIII)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", shift = At.Shift.AFTER))
    private void unscaleMatrixBasedOnSlot(LivingEntity entity, World world, ItemStack stack, int x, int y, int seed, int z, CallbackInfo ci) {
        MatrixStack matrices = this.getMatrices();
        if (ScalableSlot.scalableSlotToDraw != null) {
            matrices.scale(1.0f/ScalableSlot.scalableSlotToDraw.itemScale, 1.0f/ScalableSlot.scalableSlotToDraw.itemScale, 1.0f);
            matrices.translate(0.5f - ScalableSlot.scalableSlotToDraw.scale / 2, ScalableSlot.scalableSlotToDraw.scale / 2 - 0.5f, 0);
        } else if (LCEUIMod.getConfig().smallerItemsOutsideOfScalableSlots) {
            float itemScale = 7.0f/8.0f;
            matrices.scale(1.0f/itemScale, 1.0f/itemScale, 1.0f);
        }
    }

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void scaleCountBasedOnSlot(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        MatrixStack matrices = this.getMatrices();
        if (ScalableSlot.scalableSlotToDraw != null) {
            matrices.translate(x, y, 0.0f);
            matrices.scale(ScalableSlot.scalableSlotToDraw.scale, ScalableSlot.scalableSlotToDraw.scale, 1.0f);
            matrices.translate(-x, -y, 0.0f);
        }
    }

    @Inject(method = "drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At("TAIL"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void unscaleCountBasedOnSlot(TextRenderer textRenderer, ItemStack stack, int x, int y, String countOverride, CallbackInfo ci) {
        MatrixStack matrices = this.getMatrices();
        if (ScalableSlot.scalableSlotToDraw != null) {
            matrices.translate(x, y, 0.0f);
            matrices.scale(1.0f/ScalableSlot.scalableSlotToDraw.scale, 1.0f/ScalableSlot.scalableSlotToDraw.scale, 1.0f);
            matrices.translate(-x, -y, 0.0f);
        }
    }
}

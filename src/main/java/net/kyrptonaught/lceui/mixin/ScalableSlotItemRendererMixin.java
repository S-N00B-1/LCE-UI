package net.kyrptonaught.lceui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public abstract class ScalableSlotItemRendererMixin {
    @ModifyArg(method = "renderGuiItemModel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformation$Mode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V"), index = 3)
    private MatrixStack scaleMatrixBasedOnSlot(MatrixStack matrixStack, @Local(name = "x") int x, @Local(name = "y") int y) {
        if (ScalableSlot.scalableSlotToDraw != null) {
            var variable = ScalableSlot.scalableSlotToDraw.scale >= 1 ? 2.0f/3.0f : (ScalableSlot.scalableSlotToDraw.itemScale * ScalableSlot.scalableSlotToDraw.scale) * 3.0f/2.0f + 1.0f/15.0f;
            matrixStack.translate((ScalableSlot.scalableSlotToDraw.itemScale * ScalableSlot.scalableSlotToDraw.scale) - variable, variable - (ScalableSlot.scalableSlotToDraw.itemScale * ScalableSlot.scalableSlotToDraw.scale), 0);
            matrixStack.scale(ScalableSlot.scalableSlotToDraw.itemScale, ScalableSlot.scalableSlotToDraw.itemScale, 1.0f);
            matrixStack.translate(variable - (ScalableSlot.scalableSlotToDraw.itemScale * ScalableSlot.scalableSlotToDraw.scale), (ScalableSlot.scalableSlotToDraw.itemScale * ScalableSlot.scalableSlotToDraw.scale) - variable, 0);
        }
        return matrixStack;
    }

    @Redirect(method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderGuiQuad(Lnet/minecraft/client/render/BufferBuilder;IIIIIIII)V"))
    private void renderGuiQuadRedirect(ItemRenderer instance, BufferBuilder buffer, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
        if (ScalableSlot.scalableSlotToDraw != null) {
            this.renderGuiQuad(buffer, x - width * (ScalableSlot.scalableSlotToDraw.scale - 1) + (ScalableSlot.scalableSlotToDraw.scale - 1) * 16 + (1.0/3.0), y - height * (ScalableSlot.scalableSlotToDraw.scale - 1) + (ScalableSlot.scalableSlotToDraw.scale - 1) * 16 + (1.0/3.0), width * ScalableSlot.scalableSlotToDraw.scale, height * ScalableSlot.scalableSlotToDraw.scale, red, green, blue, alpha); // I don't know why this works, but it seems to work
        } else {
            this.renderGuiQuad(buffer, x, y, width, height, red, green, blue, alpha);
        }
    }

    @Unique
    private void renderGuiQuad(BufferBuilder buffer, double x, double y, double width, double height, int red, int green, int blue, int alpha) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        buffer.vertex((x + 0), y + 0, 0.0).color(red, green, blue, alpha).next();
        buffer.vertex((x + 0), y + height, 0.0).color(red, green, blue, alpha).next();
        buffer.vertex((x + width), y + height, 0.0).color(red, green, blue, alpha).next();
        buffer.vertex(x + width, y + 0, 0.0).color(red, green, blue, alpha).next();
        BufferRenderer.drawWithShader(buffer.end());
    }
}

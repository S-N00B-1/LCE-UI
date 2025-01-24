package net.kyrptonaught.lceui.mixin.gui;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public class ItemRendererTransMixin {
    @ModifyVariable(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V", at = @At("STORE"), ordinal = 0)
    private RenderLayer transparentLayer(RenderLayer original) {
        if (LCEUIMod.getConfig().fadeOut) {
            return TexturedRenderLayers.getEntityTranslucentCull();
        } else { return original; }
    }
    //TODO: Block entity rendering.
}
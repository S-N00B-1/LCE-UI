package net.kyrptonaught.lceui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.text.Style;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TextRenderer.Drawer.class)
public class TextShadowMixin {
    @Shadow @Final private boolean shadow;

    @ModifyArg(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawGlyph(Lnet/minecraft/client/font/GlyphRenderer;ZZFFFLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFFFI)V"), index = 4)
    private float modifyX(float original, @Local(name = "n") float n, @Local(name = "style") Style style) {
        return original - (((style.getColor() == TextColor.fromFormatting(Formatting.YELLOW) ? 1.0f : 2.0f) * n)/3.0f);
    }

    @ModifyArg(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawGlyph(Lnet/minecraft/client/font/GlyphRenderer;ZZFFFLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFFFI)V"), index = 5)
    private float modifyY(float original, @Local(name = "n") float n) {
        return original - ((2.0f * n)/3.0f);
    }

    @ModifyArg(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawGlyph(Lnet/minecraft/client/font/GlyphRenderer;ZZFFFLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFFFI)V"), index = 8)
    private float modifyRed(float original) {
        return this.shadow ? 0.0f : original;
    }

    @ModifyArg(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawGlyph(Lnet/minecraft/client/font/GlyphRenderer;ZZFFFLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFFFI)V"), index = 9)
    private float modifyGreen(float original) {
        return this.shadow ? 0.0f : original;
    }

    @ModifyArg(method = "accept", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;drawGlyph(Lnet/minecraft/client/font/GlyphRenderer;ZZFFFLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumer;FFFFI)V"), index = 10)
    private float modifyBlue(float original) {
        return this.shadow ? 0.0f : original;
    }
}

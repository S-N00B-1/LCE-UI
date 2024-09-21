package net.kyrptonaught.lceui.mixin.chat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.font.Glyph;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Glyph.class)
public interface GlyphMixin {
    @ModifyReturnValue(method = "getShadowOffset", at = @At("RETURN"))
    private float shadowOffset(float original) {
        if (LCEUIMod.getConfig().closerTextShadows) {
            return 1.0f / 3.0f;
        }
        return original;
    }

}

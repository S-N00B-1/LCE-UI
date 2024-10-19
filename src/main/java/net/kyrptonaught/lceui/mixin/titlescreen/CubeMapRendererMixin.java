package net.kyrptonaught.lceui.mixin.titlescreen;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.gui.CubeMapRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CubeMapRenderer.class)
public class CubeMapRendererMixin {

    @Inject(method = "draw", at = @At("HEAD"), cancellable = true)
    public void draw(CallbackInfo ci) {
        // The original panorama renderer will attempt to draw over the new one, thus, this one must be stopped at all costs.
        // All you need to do is give the order.
        if (LCEUIMod.getConfig().lcePan) {
            ci.cancel();
        }
    }
}
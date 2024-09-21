package net.kyrptonaught.lceui.mixin.chat;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.SplashTextRenderer;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SplashTextRenderer.class)
public class SplashMixin {

    @Shadow private final String text;

    public SplashMixin(String text) {
        this.text = text;
    }

    // This method has the same functionality as the original,
    // But the text shadow is drawn manually, because when closerTextShadows
    // is enabled, the splash has to keep the original shadow offset.
    @Inject(method = "render", at=@At("HEAD"), cancellable = true)
    public void render(DrawContext context, int screenWidth, TextRenderer textRenderer, int alpha, CallbackInfo ci) {
        context.getMatrices().push();
        context.getMatrices().translate((float)screenWidth / 2.0F + 123.0F, 69.0F, 0.0F);
        context.getMatrices().multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-20.0F));
        float f = 1.8F - MathHelper.abs(MathHelper.sin((float)(Util.getMeasuringTimeMs() % 1000L) / 1000.0F * 6.2831855F) * 0.1F);
        f = f * 100.0F / (float)(textRenderer.getWidth(this.text) + 32);
        context.getMatrices().scale(f, f, f);
        context.getMatrices().translate(1f, 1f, 0);
        context.drawText(textRenderer,this.text,(-((textRenderer.getWidth(this.text) / 2))),-8,4144917 | alpha, false);
        context.getMatrices().translate(-1f, -1f, 0);
        context.drawText(textRenderer,this.text,(-((textRenderer.getWidth(this.text) / 2))),-8,16776960 | alpha, false);
        context.getMatrices().pop();

        ci.cancel();
    }

}

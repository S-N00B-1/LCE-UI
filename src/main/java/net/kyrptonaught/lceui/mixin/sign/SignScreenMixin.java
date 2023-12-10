package net.kyrptonaught.lceui.mixin.sign;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.util.LCESounds;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.SignEditScreen;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

// TODO: fix selection (cursor in the middle of text and stuff like that)
@Mixin(SignEditScreen.class)
public abstract class SignScreenMixin {
    @Shadow private int currentRow;

    @Shadow private int ticksSinceOpened;

    @Inject(method = "init", at = @At("HEAD"))
    private void playSoundOnInit(CallbackInfo ci) {
        if (LCEUIMod.getConfig().sign) ((ScreenAccessor)this).client().getSoundManager().play(PositionedSoundInstance.master(LCESounds.CLICK_STEREO, 1.0f, 5.0f));
    }

    @Inject(method = "removed", at = @At("HEAD"))
    private void playSoundOnRemoval(CallbackInfo ci) {
        if (LCEUIMod.getConfig().sign) ((ScreenAccessor)this).client().getSoundManager().play(PositionedSoundInstance.master(LCESounds.UI_BACK, 1.0f, 5.0f));
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/SignEditScreen;drawCenteredText(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", ordinal = 0))
    private void drawCenteredTextRedirect(MatrixStack matrixStack, TextRenderer textRenderer, Text text, int x, int y, int color) {
        if (LCEUIMod.getConfig().sign)
            LCEDrawableHelper.drawTextWithShadow(matrixStack, textRenderer, text, x - (float) textRenderer.getWidth(text) / 2.0f, y + 18 - 1.0f/3.0f, 1.0f, color);
        else
            DrawableHelper.drawCenteredText(matrixStack, textRenderer, text, x, y, color);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER))
    private boolean isInstance(Object signBlock, Operation<Boolean> original) {
        return original.call(signBlock) || LCEUIMod.getConfig().sign;
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZIIZ)I", ordinal = 0))
    private int redirectDraw(TextRenderer instance, String text, float x, float y, int color, boolean shadow, Matrix4f matrix, VertexConsumerProvider vertexConsumers, boolean seeThrough, int backgroundColor, int light, boolean rightToLeft, @Local(name = "matrices") MatrixStack matrices) {
        if (LCEUIMod.getConfig().sign) {
            matrices.scale(1.0f/0.010416667F, 1.0f/-0.010416667F, 1.0f/0.010416667F);
            matrices.scale(1.0f/93.75f, 1.0f/-93.75F, 1.0f/93.75F);
            LCEDrawableHelper.drawCenteredText(matrices, instance, Text.literal(text), 0.0f, 0.0f, y + 6, y + 6, 2.0f/3.0f, color);
            matrices.scale(93.75f, -93.75F, 93.75F);
            matrices.scale(0.010416667F, -0.010416667F, 0.010416667F);
            return 0;
        } else
            return instance.draw(text, x, y, color, shadow, matrix, vertexConsumers, seeThrough, backgroundColor, light, rightToLeft);
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZIIZ)I", ordinal = 1), index = 0)
    private String doNotDrawCursorInCustomUI(String original) {
        return LCEUIMod.getConfig().sign ? "" : original;
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), index = 8)
    private boolean shouldRenderArrows(boolean original) {
        return LCEUIMod.getConfig().sign ? this.ticksSinceOpened / 4 % 2 == 0 : original;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/font/TextRenderer;draw(Ljava/lang/String;FFIZLnet/minecraft/util/math/Matrix4f;Lnet/minecraft/client/render/VertexConsumerProvider;ZIIZ)I", ordinal = 1, shift = At.Shift.BY, by = 2), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void renderArrows(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci, @Local(ordinal = 3) int p, @Local(ordinal = 6) int l) {
        if (LCEUIMod.getConfig().sign) {
            matrices.scale(1.0f / 0.010416667F, 1.0f / -0.010416667F, 1.0f / 0.010416667F);
            matrices.scale(1.0f / 93.75f, 1.0f / -93.75F, 1.0f / 93.75F);
            LCEDrawableHelper.drawText(matrices, ((ScreenAccessor) this).client().textRenderer, Text.literal(">"), -43, this.currentRow * 10 - 17 + 2.0f / 3.0f, 2.0f / 3.0f, 0);
            LCEDrawableHelper.drawText(matrices, ((ScreenAccessor) this).client().textRenderer, Text.literal("<"), 41 + 2.0f/3.0f, this.currentRow * 10 - 17 + 2.0f / 3.0f, 2.0f / 3.0f, 0);
            matrices.scale(93.75f, -93.75F, 93.75F);
            matrices.scale(0.010416667F, -0.010416667F, 0.010416667F);
        }
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V"))
    private void renderLargerSign(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrices.scale(288.0f/282.0f, 288.0f/282.0f, 288.0f/282.0f);
        matrices.translate(0.0f, 2.0f/192.0f + 2.0f/384.0f, 0.0f);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", shift = At.Shift.AFTER))
    private void reverseRenderLargerSign(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        matrices.translate(0.0f, 192.0f/2.0f + 384.0f/2.0f, 0.0f);
        matrices.scale(282.0f/288.0f, 282.0f/288.0f, 282.0f/288.0f);
    }
}
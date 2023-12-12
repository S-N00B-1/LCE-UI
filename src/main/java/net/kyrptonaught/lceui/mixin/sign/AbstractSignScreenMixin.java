package net.kyrptonaught.lceui.mixin.sign;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.util.LCESounds;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractSignEditScreen;
import net.minecraft.client.gui.screen.ingame.HangingSignEditScreen;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// TODO: fix selection (cursor in the middle of text and stuff like that)
@Mixin(AbstractSignEditScreen.class)
public abstract class AbstractSignScreenMixin {
    @Shadow protected abstract Vector3f getTextScale();

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

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawCenteredTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)V", ordinal = 0))
    private void drawCenteredTextRedirect(DrawContext instance, TextRenderer textRenderer, Text text, int centerX, int y, int color, Operation<Void> original) {
        if (LCEUIMod.getConfig().sign)
            LCEDrawableHelper.drawTextWithShadow(instance, textRenderer, text, centerX - (float) textRenderer.getWidth(text) / 2.0f, y + 18 - 1.0f/3.0f, 1.0f, color);
        else
            original.call(instance, textRenderer, text, centerX, y, color);
    }

    @WrapOperation(method = "renderSignText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I"))
    private int redirectDraw(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow, Operation<Integer> original) {
        if (LCEUIMod.getConfig().sign) {
            MatrixStack matrices = instance.getMatrices();
            matrices.scale(1.0f/this.getTextScale().x, 1.0f/this.getTextScale().y, 1.0f/this.getTextScale().z);
            float width = LCEDrawableHelper.drawCenteredText(instance, textRenderer, Text.literal(text), 0.0f, 0.0f, y + 6, y + 6, 2.0f/3.0f, color);
            matrices.scale(this.getTextScale().x, this.getTextScale().y, this.getTextScale().z);
            instance.drawText(textRenderer, "", 0, 0, color, shadow);
            return (int) width;
        } else
            return original.call(instance, textRenderer, text, x, y, color, shadow);
    }

    @WrapOperation(method = "renderSignText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I", ordinal = 1))
    private int doNotDrawCursorInCustomUI(DrawContext instance, TextRenderer textRenderer, String text, int x, int y, int color, boolean shadow, Operation<Integer> original) {
        return LCEUIMod.getConfig().sign ? 0 : original.call(instance, textRenderer, text, x, y, color, shadow);
    }

    @ModifyVariable(method = "renderSignText", at = @At(value = "STORE"), index = 4)
    private boolean shouldRenderArrows(boolean original) {
        return LCEUIMod.getConfig().sign ? this.ticksSinceOpened / 4 % 2 == 0 : original;
    }

    @Inject(method = "renderSignText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Ljava/lang/String;IIIZ)I", ordinal = 1, shift = At.Shift.BY, by = 2))
    private void renderArrows(DrawContext context, CallbackInfo ci) {
        if (LCEUIMod.getConfig().sign) {
            MatrixStack matrices = context.getMatrices();
            matrices.scale(1.0f / 0.010416667F, 1.0f / -0.010416667F, 1.0f / 0.010416667F);
            matrices.scale(1.0f / 93.75f, 1.0f / -93.75F, 1.0f / 93.75F);
            LCEDrawableHelper.drawText(context, ((ScreenAccessor) this).client().textRenderer, Text.literal(">"), (((AbstractSignEditScreen)(Object)this) instanceof HangingSignEditScreen ? -26 : -43), this.currentRow * 10 - 17 + 2.0f / 3.0f + (((AbstractSignEditScreen)(Object)this) instanceof HangingSignEditScreen ? 2 + 1.0f/3.0f : 0), 2.0f / 3.0f, 0);
            LCEDrawableHelper.drawText(context, ((ScreenAccessor) this).client().textRenderer, Text.literal("<"), (((AbstractSignEditScreen)(Object)this) instanceof HangingSignEditScreen ? 22 + 2.0f/3.0f : 41 + 2.0f/3.0f), this.currentRow * 10 - 17 + 2.0f / 3.0f + (((AbstractSignEditScreen)(Object)this) instanceof HangingSignEditScreen ? 2 + 1.0f/3.0f : 0), 2.0f / 3.0f, 0);
            matrices.scale(93.75f, -93.75F, 93.75F);
            matrices.scale(0.010416667F, -0.010416667F, 0.010416667F);
        }
    }

    // TODO: make new mixins for SignEditScreen and HangingSignEditScreen
//    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getBlock()Lnet/minecraft/block/Block;", shift = At.Shift.AFTER))
//    private boolean isInstance(Object signBlock, Operation<Boolean> original) {
//        return original.call(signBlock) || LCEUIMod.getConfig().sign;
//    }
//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V"))
//    private void renderLargerSign(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//        matrices.scale(288.0f/282.0f, 288.0f/282.0f, 288.0f/282.0f);
//        matrices.translate(0.0f, 2.0f/192.0f + 2.0f/384.0f, 0.0f);
//    }
//
//    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelPart;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;II)V", shift = At.Shift.AFTER))
//    private void reverseRenderLargerSign(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
//        matrices.translate(0.0f, 192.0f/2.0f + 384.0f/2.0f, 0.0f);
//        matrices.scale(282.0f/288.0f, 282.0f/288.0f, 282.0f/288.0f);
//    }
}
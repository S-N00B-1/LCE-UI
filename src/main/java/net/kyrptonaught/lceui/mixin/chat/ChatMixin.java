package net.kyrptonaught.lceui.mixin.chat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO: make line height 35px instead of 34px or 36px
@Mixin(ChatHud.class)
public abstract class ChatMixin {
    @Shadow @Final private MinecraftClient client;

    @Shadow
    public abstract double getChatScale();

    @Unique
    private static final int left = 37;

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private int renderWithLCEShadow1(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, Operation<Integer> original) {
        return LCEUIMod.getConfig().closerTextShadows ? (int)LCEDrawableHelper.drawTextWithShadow(instance, textRenderer, text, x, y, 1.0f, color) : original.call(instance, textRenderer, text, x, y, color);
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    private int renderWithLCEShadow2(DrawContext instance, TextRenderer textRenderer, OrderedText text, int x, int y, int color, Operation<Integer> original) {
        return LCEUIMod.getConfig().closerTextShadows ? (int)LCEDrawableHelper.drawTextWithShadow(instance, textRenderer, text, x, y, 1.0f, color) : original.call(instance, textRenderer, text, x, y, color);
    }

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V", ordinal = 0))
    private void fillRedirect1(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
        instance.fill(x1, LCEUIMod.getConfig().chatYPos ? y1 - 26 : y1, x2 + (LCEUIMod.getConfig().chatWidth ? left + 18 : 0), LCEUIMod.getConfig().chatYPos ? y2 - 26 : y2, LCEUIMod.getConfig().recolorChat ? (int)(this.client.options.getChatOpacity().getValue() * 95) << 24 : color);
    }

    @Redirect(method = "render", slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/message/MessageHandler;getUnprocessedMessageCount()J", ordinal = 0)), at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;fill(IIIII)V"))
    private void fillRedirect2(DrawContext instance, int x1, int y1, int x2, int y2, int color) {
        instance.fill(x1, y1, x2, y2, color);
    }

    @Inject(method = "getWidth()I", at = @At("RETURN"), cancellable = true)
    private void getWidth(CallbackInfoReturnable<Integer> cir) {
        if (LCEUIMod.getConfig().chatWidth) {
            cir.setReturnValue(this.client.getWindow().getScaledWidth() - 5 - left);
        }
    }

    @ModifyReturnValue(method = "toChatLineX", at = @At("RETURN"))
    private double toChatLineX(double original) {
        if (LCEUIMod.getConfig().chatWidth)
            return original - ((left / 2.0) / this.getChatScale());
        return original;
    }

    @ModifyReturnValue(method = "toChatLineY", at = @At("RETURN"))
    private double toChatLineY(double original) {
        if (LCEUIMod.getConfig().chatYPos)
            return original - (2.0f/3.0f + 4.9f * this.getChatScale()) / (this.getChatScale() * (this.client.options.getChatLineSpacing().getValue() + 1.0));
        return original;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0), index = 0)
    private float translateModifyX1(float original) {
        if (LCEUIMod.getConfig().chatWidth)
            return original - 4.0f;
        return original;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 2), index = 0)
    private float translateModifyX2(float original) {
        if (LCEUIMod.getConfig().chatWidth)
            return original + left - 4.0f;
        return original;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0), index = 1)
    private float translateModifyY1(float original) {
        if (LCEUIMod.getConfig().chatYPos)
            return original - 26;
        return original;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 2), index = 1)
    private float translateModifyY2(float original) {
        if (LCEUIMod.getConfig().chatYPos)
            return original - 26;
        return original;
    }

    @ModifyReturnValue(method = "getChatScale", at = @At("RETURN"))
    private double getChatScale(double original) {
        if (LCEUIMod.getConfig().rescaleChatText)
            return original * 2.0 / 3.0;
        return original;
    }

    @Inject(method = "isXInsideIndicatorIcon", at = @At("RETURN"), cancellable = true)
    private void getIndicatorX(double x, ChatHudLine.Visible line, MessageIndicator indicator, CallbackInfoReturnable<Boolean> cir) {
        if (LCEUIMod.getConfig().chatWidth) cir.setReturnValue(false);
    }
}

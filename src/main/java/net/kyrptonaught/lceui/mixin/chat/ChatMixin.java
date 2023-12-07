package net.kyrptonaught.lceui.mixin.chat;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.math.MatrixStack;
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

    @Shadow public abstract int getHeight();

    @Unique
    private static final int left = 37;

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;fill(Lnet/minecraft/client/util/math/MatrixStack;IIIII)V", ordinal = 0))
    private void fillRedirect(MatrixStack matrixStack, int x1, int y1, int x2, int y2, int color) {
        DrawableHelper.fill(matrixStack, x1, y1, x2 + (LCEUIMod.getConfig().chatWidth ? left + 18 : 0), y2, LCEUIMod.getConfig().recolorChat ? (int)(this.client.options.getChatOpacity().getValue() * 95) << 24 : color);
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
            return original + (4.0 - left) / this.getChatScale();
        return original;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V", ordinal = 0), index = 0)
    private double translateModifyX1(double original) {
        if (LCEUIMod.getConfig().chatWidth)
            return original - 4.0;
        return original;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(DDD)V", ordinal = 2), index = 0)
    private double translateModifyX2(double original) {
        if (LCEUIMod.getConfig().chatWidth)
            return original + left - 4.0;
        return original;
    }

    @ModifyReturnValue(method = "getChatScale", at = @At("RETURN"))
    private double getChatScale(double original) {
        if (LCEUIMod.getConfig().rescaleChatText)
            return original * 2.0 / 3.0;
        return original;
    }

//    @ModifyReturnValue(method = "getVisibleLineCount", at = @At("RETURN"))
//    private int getVisibleLineCount(int original) {
//        if (LCEUIMod.getConfig().chatHeight)
//            return (int)(this.getHeight() / (35.0 / 3.0));
//        return original;
//    }
}

package net.kyrptonaught.lceui.mixin.hotbar;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public class HotbarMixin {
    @Shadow private int scaledWidth;

    @ModifyArg(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", ordinal = 1), index = 0)
    private Formatting changeItalicToGold(Formatting formatting) {
        if (LCEUIMod.getConfig().renamedItemsHaveGoldName) return Formatting.GOLD;
        return formatting;
    }

    @WrapOperation(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;III)I"))
    private int changeTextRenderer(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, Operation<Integer> original) {
        if (LCEUIMod.getConfig().hotbarText) {
            return (int) LCEDrawableHelper.drawTextWithShadow(instance, textRenderer, text, (this.scaledWidth - (textRenderer.getWidth(text) * 2.0f/3.0f)) / 2.0f, y - textRenderer.fontHeight / 3.0f, 2.0f/3.0f, color);
        }
        return original.call(instance, textRenderer, text, x, y, color);
    }
}

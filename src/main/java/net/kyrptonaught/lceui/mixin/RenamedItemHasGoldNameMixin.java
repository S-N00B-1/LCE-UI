package net.kyrptonaught.lceui.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(ItemStack.class)
public class RenamedItemHasGoldNameMixin {
    @Redirect(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", ordinal = 1))
    private MutableText changeItalicToGold(MutableText instance, Formatting formatting) {
        if (LCEUIMod.getConfig().renamedItemsHaveGoldName) return instance.formatted(Formatting.GOLD);
        return instance.formatted(formatting);
    }
}

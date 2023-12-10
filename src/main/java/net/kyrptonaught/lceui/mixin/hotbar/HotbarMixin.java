package net.kyrptonaught.lceui.mixin.hotbar;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(InGameHud.class)
public class HotbarMixin {
    @ModifyArg(method = "renderHeldItemTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/text/MutableText;formatted(Lnet/minecraft/util/Formatting;)Lnet/minecraft/text/MutableText;", ordinal = 1), index = 0)
    private Formatting changeItalicToGold(Formatting formatting) {
        if (LCEUIMod.getConfig().renamedItemsHaveGoldName) return Formatting.GOLD;
        return formatting;
    }
}

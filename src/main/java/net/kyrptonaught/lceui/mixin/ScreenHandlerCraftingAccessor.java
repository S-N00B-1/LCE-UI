package net.kyrptonaught.lceui.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(PlayerScreenHandler.class)
public interface ScreenHandlerCraftingAccessor {
    @Accessor("craftingResult")
    CraftingResultInventory craftingResult();
}
package net.kyrptonaught.lceui.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.screen.PlayerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerScreenHandler.class)
public interface ScreenHandlerCraftingAccessor {
    @Accessor("craftingInput")
    CraftingInventory craftingInput();

    @Accessor("craftingResult")
    CraftingResultInventory craftingResult();
}
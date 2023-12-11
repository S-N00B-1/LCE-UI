package net.kyrptonaught.lceui.mixin.container;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(value = { Generic3x3ContainerScreenHandler.class, ShulkerBoxScreenHandler.class, HopperScreenHandler.class })
public interface ContainerInventoryAccessor {
    @Accessor("inventory")
    Inventory inventory();
}

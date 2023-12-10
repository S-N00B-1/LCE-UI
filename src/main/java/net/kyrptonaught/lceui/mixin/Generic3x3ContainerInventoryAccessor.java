package net.kyrptonaught.lceui.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(Generic3x3ContainerScreenHandler.class)
public interface Generic3x3ContainerInventoryAccessor {
    @Accessor("inventory")
    Inventory inventory();
}

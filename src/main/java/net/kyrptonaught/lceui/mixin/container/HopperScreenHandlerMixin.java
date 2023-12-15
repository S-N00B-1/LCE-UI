package net.kyrptonaught.lceui.mixin.container;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(HopperScreenHandler.class)
public class HopperScreenHandlerMixin {
    @WrapOperation(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/HopperScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 0))
    private Slot replaceSlotWithScalableSlot1(HopperScreenHandler instance, Slot slot, Operation<Slot> original) {
        if (LCEUIMod.getConfig().hopperInventory) {
            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;
            float j = (slot.x - 44.0f) / 18.0f;
            return original.call(instance, new ScalableSlot(slot.inventory, slot.getIndex(), 37 + 1.0f/3.0f + j * (18 * slotScale - 1.0f/4.0f), 16, slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/HopperScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 1))
    private Slot replaceSlotWithScalableSlot2(HopperScreenHandler instance, Slot slot, Operation<Slot> original) {
        if (LCEUIMod.getConfig().hopperInventory) {
            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;
            float k = (slot.x - 8.0f) / 18.0f;
            float j = (slot.y - 51.0f) / 18.0f;
            float i = -(18 * slotScale - 1.0f/4.0f);
            return original.call(instance, new ScalableSlot(slot.inventory, slot.getIndex(), 9 + k * (18 * slotScale - 1.0f/4.0f), 56 + j * (18 * slotScale - 1.0f/4.0f) + i, slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/HopperScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 2))
    private Slot replaceSlotWithScalableSlot3(HopperScreenHandler instance, Slot slot, Operation<Slot> original) {
        if (LCEUIMod.getConfig().hopperInventory) {
            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;
            float j = (slot.x - 8.0f) / 18.0f;
            float i = -(18 * slotScale - 1.0f/4.0f);
            return original.call(instance, new ScalableSlot(slot.inventory, slot.getIndex(), 9 + j * (18 * slotScale - 1.0f/4.0f), 102 + i, slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }
}

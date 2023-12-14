package net.kyrptonaught.lceui.mixin.container;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(GenericContainerScreenHandler.class)
public class GenericContainerScreenHandlerMixin {
    @Shadow @Final private int rows;

    @WrapOperation(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GenericContainerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 0))
    private Slot replaceSlotWithScalableSlot1(GenericContainerScreenHandler instance, Slot slot, Operation<Slot> original) {
        if (LCEUIMod.getConfig().containerInventory) {
            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;
            float k = (slot.x - 8.0f) / 18.0f;
            float j = (slot.y - 18.0f) / 18.0f;
            return original.call(instance, new ScalableSlot(slot.inventory, slot.getIndex(), 9 + k * (18 * slotScale - 1.0f/4.0f), 17 + 1.0f/3.0f + j * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GenericContainerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 1))
    private Slot replaceSlotWithScalableSlot2(GenericContainerScreenHandler instance, Slot slot, Operation<Slot> original) {
        if (LCEUIMod.getConfig().containerInventory) {
            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;
            float iOriginal = (this.rows - 4) * 18;
            float i = (this.rows - 4) * (18 * slotScale - 1.0f/4.0f);
            float k = (slot.x - 8.0f) / 18.0f;
            float j = (slot.y - iOriginal - 103.0f) / 18.0f;
            return original.call(instance, new ScalableSlot(slot.inventory, slot.getIndex(), 9 + k * (18 * slotScale - 1.0f/4.0f), 85 + 1.0f/3.0f + j * (18 * slotScale - 1.0f/4.0f) + i, slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/inventory/Inventory;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/GenericContainerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 2))
    private Slot replaceSlotWithScalableSlot3(GenericContainerScreenHandler instance, Slot slot, Operation<Slot> original) {
        if (LCEUIMod.getConfig().containerInventory) {
            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;
            float i = (this.rows - 4) * (18 * slotScale - 1.0f/4.0f);
            float j = (slot.x - 8.0f) / 18.0f;
            return original.call(instance, new ScalableSlot(slot.inventory, slot.getIndex(), 9 + j * (18 * slotScale - 1.0f/4.0f), 132 + i, slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }
}

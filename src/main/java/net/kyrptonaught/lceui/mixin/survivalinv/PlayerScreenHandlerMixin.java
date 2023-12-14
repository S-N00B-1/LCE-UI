package net.kyrptonaught.lceui.mixin.survivalinv;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.screens.LCESurvivalInventoryScreen;
import net.kyrptonaught.lceui.util.ScalableCraftingResultSlot;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin(PlayerScreenHandler.class)
public abstract class PlayerScreenHandlerMixin {
    @Shadow @Final private RecipeInputInventory craftingInput;

    @Shadow @Final private CraftingResultInventory craftingResult;

    @Shadow @Final private PlayerEntity owner;

    @Shadow
    static void onEquipStack(PlayerEntity player, EquipmentSlot slot, ItemStack stack1, ItemStack stack2) {
        throw new IllegalStateException();
    }

    @Shadow @Final private static EquipmentSlot[] EQUIPMENT_SLOT_ORDER;

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 0))
    private Slot addSlot1(PlayerScreenHandler instance, Slot slot, Operation<Slot> original) {
        float slotScale = 19.0f/24.0f;
        float itemScale = 7.0f/8.0f;

        if (LCEUIMod.getConfig().survivalInventory) {
            return original.call(instance, new ScalableCraftingResultSlot(this.owner, this.craftingInput, this.craftingResult, slot.getIndex(), 121 + (LCEUIMod.getConfig().classicCrafting ? 0 : MinecraftClient.getInstance().getWindow().getScaledWidth()), 27 - 1.0f/3.0f, slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 1))
    private Slot addSlot2(PlayerScreenHandler instance, Slot slot, Operation<Slot> original) {
        float slotScale = 19.0f/24.0f;
        float itemScale = 7.0f/8.0f;

        if (LCEUIMod.getConfig().survivalInventory) {
            float x = (slot.x - 98) / 18.0f;
            float y = (slot.y - 18) / 18.0f;
            return original.call(instance, new ScalableSlot(this.craftingInput, slot.getIndex(), 74 + x * (18 * slotScale - 1.0f/4.0f) + (LCEUIMod.getConfig().classicCrafting ? 0 : MinecraftClient.getInstance().getWindow().getScaledWidth()), 20 - 1.0f/3.0f + y * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 2))
    private Slot addSlot3(PlayerScreenHandler instance, Slot slot, Operation<Slot> original) {
        float slotScale = 19.0f/24.0f;
        float itemScale = 7.0f/8.0f;

        if (LCEUIMod.getConfig().survivalInventory) {
            int yNew = (slot.y - 8) / 18;
            EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[yNew];
            return original.call(instance, new ScalableSlot(this.owner.getInventory(), slot.getIndex(), LCEUIMod.getConfig().classicCrafting ? 9 : 42, 9 + 1.0f/3.0f + yNew * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale) {
                public void setStack(ItemStack stack) {
                    onEquipStack(owner, equipmentSlot, stack, this.getStack());
                    super.setStack(stack);
                }

                public int getMaxItemCount() {
                    return 1;
                }

                public boolean canInsert(ItemStack stack) {
                    return equipmentSlot == MobEntity.getPreferredEquipmentSlot(stack);
                }

                public boolean canTakeItems(PlayerEntity playerEntity) {
                    ItemStack itemStack = this.getStack();
                    return (itemStack.isEmpty() || playerEntity.isCreative() || !EnchantmentHelper.hasBindingCurse(itemStack)) && super.canTakeItems(playerEntity);
                }

                @Nullable
                @Override
                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, LCEUIMod.getConfig().ps4BackgroundSprites ? LCESurvivalInventoryScreen.EMPTY_PS4_ARMOR_SLOT_TEXTURES[yNew] : LCESurvivalInventoryScreen.EMPTY_ARMOR_SLOT_TEXTURES[yNew]);
                }
            });
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 3))
    private Slot addSlot4(PlayerScreenHandler instance, Slot slot, Operation<Slot> original) {
        float slotScale = 19.0f/24.0f;
        float itemScale = 7.0f/8.0f;

        if (LCEUIMod.getConfig().survivalInventory) {
            int x = (slot.x - 8) / 18;
            int y = (slot.y - 84) / 18;
            return original.call(instance, new ScalableSlot(this.owner.getInventory(), slot.getIndex(), 9 + x * (18 * slotScale - 1.0f/4.0f), 77 + 1.0f/3.0f + y * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 4))
    private Slot addSlot5(PlayerScreenHandler instance, Slot slot, Operation<Slot> original) {
        float slotScale = 19.0f/24.0f;
        float itemScale = 7.0f/8.0f;

        if (LCEUIMod.getConfig().survivalInventory) {
            int x = (slot.x - 8) / 18;
            return original.call(instance, new ScalableSlot(this.owner.getInventory(), slot.getIndex(), 9 + x * (18 * slotScale - 1.0f/4.0f), 124, slotScale, slotScale * itemScale));
        }
        return original.call(instance, slot);
    }

    @WrapOperation(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addSlot(Lnet/minecraft/screen/slot/Slot;)Lnet/minecraft/screen/slot/Slot;", ordinal = 5))
    private Slot addSlot6(PlayerScreenHandler instance, Slot slot, Operation<Slot> original) {
        float slotScale = 19.0f/24.0f;
        float itemScale = 7.0f/8.0f;

        if (LCEUIMod.getConfig().survivalInventory) {
            return original.call(instance, new ScalableSlot(this.owner.getInventory(), slot.getIndex(), LCEUIMod.getConfig().classicCrafting ? 74 : 107, 51 + 1.0f/3.0f, slotScale, slotScale * itemScale) {
                public void setStack(ItemStack stack) {
                    onEquipStack(owner, EquipmentSlot.OFFHAND, stack, this.getStack());
                    super.setStack(stack);
                }

                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return LCEUIMod.getConfig().ps4BackgroundSprites ? Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(LCEUIMod.MOD_ID, "item/ps4_edition_empty_armor_slot_shield")) : null;
                }
            });
        }
        return original.call(instance, slot);
    }
}

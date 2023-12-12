package net.kyrptonaught.lceui.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.util.collection.DefaultedList;

public class ScalableCraftingResultSlot extends ScalableSlot {
    private final RecipeInputInventory input;
    private final PlayerEntity player;
    private int amount;

    public ScalableCraftingResultSlot(PlayerEntity player, RecipeInputInventory input, Inventory inventory, int index, float x, float y, float scale, float itemScale) {
        super(inventory, index, x, y, scale, itemScale);
        this.player = player;
        this.input = input;
    }
    public ScalableCraftingResultSlot(PlayerEntity player, RecipeInputInventory input, Inventory inventory, int index, float x, float y, float scale) {
        super(inventory, index, x, y, scale);
        this.player = player;
        this.input = input;
    }

    public boolean canInsert(ItemStack stack) {
        return false;
    }

    public ItemStack takeStack(int amount) {
        if (this.hasStack()) {
            this.amount += Math.min(amount, this.getStack().getCount());
        }

        return super.takeStack(amount);
    }

    protected void onCrafted(ItemStack stack, int amount) {
        this.amount += amount;
        this.onCrafted(stack);
    }

    protected void onTake(int amount) {
        this.amount += amount;
    }

    protected void onCrafted(ItemStack stack) {
        if (this.amount > 0) {
            stack.onCraft(this.player.getWorld(), this.player, this.amount);
        }

        if (this.inventory instanceof RecipeUnlocker recipeUnlocker) {
            recipeUnlocker.unlockLastRecipe(this.player, this.input.getInputStacks());
        }

        this.amount = 0;
    }

    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        this.onCrafted(stack);
        DefaultedList<ItemStack> defaultedList = player.getWorld().getRecipeManager().getRemainingStacks(RecipeType.CRAFTING, this.input, player.getWorld());

        for(int i = 0; i < defaultedList.size(); ++i) {
            ItemStack itemStack = this.input.getStack(i);
            ItemStack itemStack2 = defaultedList.get(i);
            if (!itemStack.isEmpty()) {
                this.input.removeStack(i, 1);
                itemStack = this.input.getStack(i);
            }

            if (!itemStack2.isEmpty()) {
                if (itemStack.isEmpty()) {
                    this.input.setStack(i, itemStack2);
                } else if (ItemStack.canCombine(itemStack, itemStack2)) {
                    itemStack2.increment(itemStack.getCount());
                    this.input.setStack(i, itemStack2);
                } else if (!this.player.getInventory().insertStack(itemStack2)) {
                    this.player.dropItem(itemStack2, false);
                }
            }
        }
    }
}
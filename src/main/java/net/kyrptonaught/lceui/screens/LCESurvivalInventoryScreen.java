package net.kyrptonaught.lceui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenHandlerCraftingAccessor;
import net.kyrptonaught.lceui.util.LCESounds;
import net.kyrptonaught.lceui.util.ScalableCraftingResultSlot;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;

public class LCESurvivalInventoryScreen extends AbstractInventoryScreen<LCESurvivalInventoryScreen.SurvivalScreenHandler> {
    private float mouseX;
    private float mouseY;
    private boolean mouseDown;

    public LCESurvivalInventoryScreen(PlayerEntity player) {
        super(new SurvivalScreenHandler(player), player.getInventory(), Text.translatable("container.inventory"));
        this.passEvents = true;
        this.backgroundHeight = 435/3;
        this.backgroundWidth = 431/3;
    }

    public void handledScreenTick() {

    }

    @Override
    protected void init() {
        super.init();
        this.client.keyboard.setRepeatEvents(true);
        this.client.player.playSound(LCESounds.CLICK_STEREO, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    @Override
    public void removed() {
        super.removed();
        this.client.keyboard.setRepeatEvents(false);
        this.client.player.playSound(LCESounds.UI_BACK, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        LCEDrawableHelper.drawText(matrices, this.textRenderer, this.title, 8 + 1.0f/3.0f, 69 - 1.0f/3.0f, 2.0f/3.0f, 4210752);
        if (LCEUIMod.getConfig().classicCrafting) {
            LCEDrawableHelper.drawText(matrices, this.textRenderer, Text.translatable("container.crafting"), 74, 10 + 2.0f/3.0f, 2.0f/3.0f, 4210752);
        }
    }

    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);

        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
        this.mouseX = (float)mouseX;
        this.mouseY = (float)mouseY;
    }

    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new Identifier(LCEUIMod.MOD_ID, "textures/gui/survival_inventory.png"));
        int i = this.x;
        int j = this.y;
        matrices.push();
        matrices.translate(this.x, this.y, 0);
        matrices.scale(1.0f/3.0f, 1.0f/3.0f, 1.0f);
        matrices.translate(-this.x, -this.y, 0);
        drawTexture(matrices, i, j, LCEUIMod.getConfig().classicCrafting ? 431 : 0, 0, 431, 435, 1024, 1024);
        matrices.pop();
        drawEntity(i + (LCEUIMod.getConfig().classicCrafting ? 48 : 81), j + 57, 22, (float)(i + (LCEUIMod.getConfig().classicCrafting ? 48 : 81)) - this.mouseX, (float)(j + 22) - this.mouseY, this.client.player);
    }

    public static void drawEntity(int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        float f = (float)Math.atan((double)(mouseX / 40.0F));
        float g = (float)Math.atan((double)(mouseY / 40.0F));
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate((double)x, (double)y, 1050.0);
        matrixStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0, 0.0, 1000.0);
        matrixStack2.scale((float)size, (float)size, (float)size);
        Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
        Quaternion quaternion2 = Vec3f.POSITIVE_X.getDegreesQuaternion(g * 20.0F);
        quaternion.hamiltonProduct(quaternion2);
        matrixStack2.multiply(quaternion);
        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0F + f * 20.0F;
        entity.setYaw(180.0F + f * 40.0F);
        entity.setPitch(-g * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        DiffuseLighting.method_34742();
        EntityRenderDispatcher entityRenderDispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
        quaternion2.conjugate();
        entityRenderDispatcher.setRotation(quaternion2);
        entityRenderDispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            entityRenderDispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack2, immediate, 15728880);
        });
        immediate.draw();
        entityRenderDispatcher.setRenderShadows(true);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }

    public boolean isPointWithinBounds(int x, int y, int width, int height, double pointX, double pointY) {
        return super.isPointWithinBounds(x, y, width, height, pointX, pointY);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.mouseDown) {
            this.mouseDown = false;
            return true;
        } else {
            return super.mouseReleased(mouseX, mouseY, button);
        }
    }

    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        return mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
    }

    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);
    }

    @Environment(EnvType.CLIENT)
    public static class SurvivalScreenHandler
            extends AbstractRecipeScreenHandler<CraftingInventory> {
        private static final EquipmentSlot[] EQUIPMENT_SLOT_ORDER;
        private final CraftingInventory craftingInput;
        private final CraftingResultInventory craftingResult = new CraftingResultInventory();
        private final PlayerScreenHandler parent;
        private final PlayerEntity player;

        public SurvivalScreenHandler(PlayerEntity player) {
            super(null, 0);
            this.player = player;
            this.parent = player.playerScreenHandler;
            this.craftingInput = new CraftingInventory(this.parent, 2, 2);
            
            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;

            int y;
            int x;
            this.addSlot(new ScalableCraftingResultSlot(player, this.parent.getCraftingInput(), ((ScreenHandlerCraftingAccessor)this.parent).craftingResult(), 0, 121 + (LCEUIMod.getConfig().classicCrafting ? 0 : MinecraftClient.getInstance().getWindow().getScaledWidth()), 27 - 1.0f/3.0f, slotScale, slotScale * itemScale));
            for (y = 0; y < 2; ++y) {
                for (x = 0; x < 2; ++x) {
                    this.addSlot(new ScalableSlot(this.parent.getCraftingInput(), x + y * 2, 74 + x * (18 * slotScale - 1.0f/4.0f) + (LCEUIMod.getConfig().classicCrafting ? 0 : MinecraftClient.getInstance().getWindow().getScaledWidth()), 20 - 1.0f/3.0f + y * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale));
                }
            }


            for(y = 0; y < 4; ++y) {
                final EquipmentSlot equipmentSlot = EQUIPMENT_SLOT_ORDER[y];
                this.addSlot(new ScalableSlot(player.getInventory(), 39 - y, LCEUIMod.getConfig().classicCrafting ? 9 : 42, 9 + 1.0f/3.0f + y * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale) {
                    public void setStack(ItemStack stack) {
                        ItemStack itemStack = this.getStack();
                        super.setStack(stack);
                        player.onEquipStack(equipmentSlot, itemStack, stack);
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
                });
            }

            for(y = 0; y < 3; ++y) {
                for(x = 0; x < 9; ++x) {
                    this.addSlot(new ScalableSlot(player.getInventory(), x + (y + 1) * 9, 9 + x * (18 * slotScale - 1.0f/4.0f), 77 + 1.0f/3.0f + y * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale));
                }
            }

            for(x = 0; x < 9; ++x) {
                this.addSlot(new ScalableSlot(player.getInventory(), x, 9 + x * (18 * slotScale - 1.0f/4.0f), 124, slotScale, slotScale * itemScale));
            }

            this.addSlot(new ScalableSlot(player.getInventory(), 40, LCEUIMod.getConfig().classicCrafting ? 74 : 107, 51 + 1.0f/3.0f, slotScale, slotScale * itemScale));
        }

        public void close(PlayerEntity player) {
            super.close(player);
            this.craftingResult.clear();
        }

        public boolean canUse(PlayerEntity player) {
            return true;
        }

        public ItemStack transferSlot(PlayerEntity player, int index) {
            ItemStack itemStack = ItemStack.EMPTY;
            Slot slot = this.slots.get(index);
            if (slot.hasStack()) {
                ItemStack itemStack2 = slot.getStack();
                itemStack = itemStack2.copy();
                EquipmentSlot equipmentSlot = MobEntity.getPreferredEquipmentSlot(itemStack);
                if (index == 0) {
                    if (!this.insertItem(itemStack2, 9, 45, true)) {
                        return ItemStack.EMPTY;
                    }

                    slot.onQuickTransfer(itemStack2, itemStack);
                } else if (index >= 1 && index < 5) {
                    if (!this.insertItem(itemStack2, 9, 45, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 5 && index < 9) {
                    if (!this.insertItem(itemStack2, 9, 45, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR && !this.slots.get(8 - equipmentSlot.getEntitySlotId()).hasStack()) {
                    int i = 8 - equipmentSlot.getEntitySlotId();
                    if (!this.insertItem(itemStack2, i, i + 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (equipmentSlot == EquipmentSlot.OFFHAND && !this.slots.get(45).hasStack()) {
                    if (!this.insertItem(itemStack2, 45, 46, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 9 && index < 36) {
                    if (!this.insertItem(itemStack2, 36, 45, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 36 && index < 45) {
                    if (!this.insertItem(itemStack2, 9, 36, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.insertItem(itemStack2, 9, 45, false)) {
                    return ItemStack.EMPTY;
                }

                if (itemStack2.isEmpty()) {
                    slot.setStack(ItemStack.EMPTY);
                } else {
                    slot.markDirty();
                }

                if (itemStack2.getCount() == itemStack.getCount()) {
                    return ItemStack.EMPTY;
                }

                slot.onTakeItem(player, itemStack2);
                if (index == 0) {
                    player.dropItem(itemStack2, false);
                }
            }

            return itemStack;
        }

        public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
            return false;
        }

        static {
            EQUIPMENT_SLOT_ORDER = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
        }

        @Override
        public ItemStack getCursorStack() {
            return this.parent.getCursorStack();
        }

        @Override
        public void setCursorStack(ItemStack stack) {
            this.parent.setCursorStack(stack);
        }

        @Override
        public void populateRecipeFinder(RecipeMatcher finder) {
            this.craftingInput.provideRecipeInputs(finder);
        }

        @Override
        public void clearCraftingSlots() {
            this.craftingResult.clear();
            this.craftingInput.clear();
        }

        @Override
        public boolean matches(Recipe<? super CraftingInventory> recipe) {
            return recipe.matches(this.craftingInput, this.player.world);
        }

        @Override
        public int getCraftingResultSlotIndex() {
            return 0;
        }

        @Override
        public int getCraftingWidth() {
            return this.craftingInput.getWidth();
        }

        @Override
        public int getCraftingHeight() {
            return this.craftingInput.getHeight();
        }

        @Override
        public int getCraftingSlotCount() {
            return 5;
        }

        @Override
        public RecipeBookCategory getCategory() {
            return RecipeBookCategory.CRAFTING;
        }

        @Override
        public boolean canInsertIntoSlot(int index) {
            return index != this.getCraftingResultSlotIndex();
        }
    }
}

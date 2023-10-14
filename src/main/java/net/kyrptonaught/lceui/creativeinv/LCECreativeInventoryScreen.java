package net.kyrptonaught.lceui.creativeinv;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryListener;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public class LCECreativeInventoryScreen extends AbstractInventoryScreen<LCECreativeInventoryScreen.CreativeScreenHandler> {
    static final SimpleInventory INVENTORY = new SimpleInventory(50);
    private static int selectedTab = 0;
    private float scrollPosition;
    private boolean scrolling;
    private CreativeInventoryListener listener;
    private boolean lastClickOutsideBounds;

    public LCECreativeInventoryScreen(PlayerEntity player) {
        super(new CreativeScreenHandler(player), player.getInventory(), ScreenTexts.EMPTY);
        player.currentScreenHandler = this.handler;
        this.passEvents = true;
        this.backgroundHeight = 193;
        this.backgroundWidth = 256;
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        if (!this.client.interactionManager.hasCreativeInventory()) {
            this.client.setScreen(new InventoryScreen(this.client.player));
        }
    }

    @Override
    protected void onMouseClick(@Nullable Slot slot, int slotId, int button, SlotActionType actionType) {
        boolean bl = actionType == SlotActionType.QUICK_MOVE;
        if (slot != null || actionType == SlotActionType.QUICK_CRAFT) {
            if (slot != null && !slot.canTakeItems(this.client.player)) {
                return;
            }
            if (actionType != SlotActionType.QUICK_CRAFT && slot.inventory == INVENTORY) {
                ItemStack itemStack = this.handler.getCursorStack();
                ItemStack itemStack2 = slot.getStack();
                if (actionType == SlotActionType.SWAP) {
                    if (!itemStack2.isEmpty()) {
                        ItemStack itemStack3 = itemStack2.copy();
                        itemStack3.setCount(itemStack3.getMaxCount());
                        this.client.player.getInventory().setStack(button, itemStack3);
                        this.client.player.playerScreenHandler.sendContentUpdates();
                    }
                    return;
                }
                if (actionType == SlotActionType.CLONE) {
                    if (this.handler.getCursorStack().isEmpty() && slot.hasStack()) {
                        ItemStack itemStack3 = slot.getStack().copy();
                        itemStack3.setCount(itemStack3.getMaxCount());
                        this.handler.setCursorStack(itemStack3);
                    }
                    return;
                }
                if (actionType == SlotActionType.THROW) {
                    if (!itemStack2.isEmpty()) {
                        ItemStack itemStack3 = itemStack2.copy();
                        itemStack3.setCount(button == 0 ? 1 : itemStack3.getMaxCount());
                        this.client.player.dropItem(itemStack3, true);
                        this.client.interactionManager.dropCreativeStack(itemStack3);
                    }
                    return;
                }
                if (!itemStack.isEmpty() && !itemStack2.isEmpty() && itemStack.isItemEqualIgnoreDamage(itemStack2) && ItemStack.areNbtEqual(itemStack, itemStack2)) {
                    if (button == 0) {
                        if (bl) {
                            itemStack.setCount(itemStack.getMaxCount());
                        } else if (itemStack.getCount() < itemStack.getMaxCount()) {
                            itemStack.increment(1);
                        }
                    } else {
                        itemStack.decrement(1);
                    }
                } else if (itemStack2.isEmpty() || !itemStack.isEmpty()) {
                    if (button == 0) {
                        this.handler.setCursorStack(ItemStack.EMPTY);
                    } else {
                        this.handler.getCursorStack().decrement(1);
                    }
                } else {
                    this.handler.setCursorStack(itemStack2.copy());
                    itemStack = this.handler.getCursorStack();
                    if (bl) {
                        itemStack.setCount(itemStack.getMaxCount());
                    }
                }
            } else if (this.handler != null) {
                ItemStack itemStack = slot == null ? ItemStack.EMPTY : this.handler.getSlot(slot.id).getStack();
                this.handler.onSlotClick(slot == null ? slotId : slot.id, button, actionType, this.client.player);
                if (ScreenHandler.unpackQuickCraftStage(button) == 2) {
                    for (int j = 0; j < 9; ++j) {
                        this.client.interactionManager.clickCreativeStack(this.handler.getSlot(INVENTORY.size() + j).getStack(), 36 + j);
                    }
                } else if (slot != null) {
                    ItemStack itemStack2 = this.handler.getSlot(slot.id).getStack();
                    this.client.interactionManager.clickCreativeStack(itemStack2, slot.id - this.handler.slots.size() + 9 + 36);
                    int k = INVENTORY.size() + button;
                    if (actionType == SlotActionType.SWAP) {
                        this.client.interactionManager.clickCreativeStack(itemStack, k - this.handler.slots.size() + 9 + 36);
                    } else if (actionType == SlotActionType.THROW && !itemStack.isEmpty()) {
                        ItemStack itemStack4 = itemStack.copy();
                        itemStack4.setCount(button == 0 ? 1 : itemStack4.getMaxCount());
                        this.client.player.dropItem(itemStack4, true);
                        this.client.interactionManager.dropCreativeStack(itemStack4);
                    }
                    this.client.player.playerScreenHandler.sendContentUpdates();
                }
            }
        } else if (!this.handler.getCursorStack().isEmpty() && this.lastClickOutsideBounds) {
            if (button == 0) {
                this.client.player.dropItem(this.handler.getCursorStack(), true);
                this.client.interactionManager.dropCreativeStack(this.handler.getCursorStack());
                this.handler.setCursorStack(ItemStack.EMPTY);
            }
            if (button == 1) {
                ItemStack itemStack = this.handler.getCursorStack().split(1);
                this.client.player.dropItem(itemStack, true);
                this.client.interactionManager.dropCreativeStack(itemStack);
            }
        }
    }

    @Override
    protected void init() {
        if (this.client.interactionManager.hasCreativeInventory()) {
            super.init();
            this.client.keyboard.setRepeatEvents(true);
            int i = selectedTab;
            selectedTab = -1;
            this.setSelectedTab(CustomItemGroup.ITEM_GROUPS.get(i));
            this.client.player.playerScreenHandler.removeListener(this.listener);
            this.listener = new CreativeInventoryListener(this.client);
            this.client.player.playerScreenHandler.addListener(this.listener);
        } else {
            this.client.setScreen(new InventoryScreen(this.client.player));
        }
    }

    @Override
    public void resize(MinecraftClient client, int width, int height) {
        this.init(client, width, height);
    }

    @Override
    public void removed() {
        super.removed();
        if (this.client.player != null && this.client.player.getInventory() != null) {
            this.client.player.playerScreenHandler.removeListener(this.listener);
        }
        this.client.keyboard.setRepeatEvents(false);
    }

    private void drawCreativeInventoryTexture(MatrixStack matrices, int u, int v, int width, int height, int x, int y) {
        if (this.client == null) return;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new Identifier(LCEUIMod.MOD_ID, "textures/gui/creativeinv/creative_inventory.png"));

        matrices.push();
        this.drawTexture(matrices, x, y, u, v, width, height);
        matrices.pop();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.renderTabs(matrices, this.x, this.y);
        this.drawCreativeInventoryTexture(matrices, 0, 20, 256, 193, this.x, this.y);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        if (this.client == null) return;
        Text text;
        if (selectedTab < CustomItemGroup.ITEM_GROUPS.size()) {
            text = CustomItemGroup.ITEM_GROUPS.get(selectedTab).getName();
        } else {
            text = Text.translatable("lceui.itemGroup.unknown");
        }
        LCEDrawableHelper.drawCenteredText(matrices, this.textRenderer, text, 0.25f, this.backgroundWidth + 0.25f, 39.25f, 39.25f, 0.75f, 0xFF3b3b3b);
        this.drawCreativeInventoryTexture(matrices, (selectedTab % 8 == 0 ? 31 : (selectedTab % 8 == 7 ? 105 : 68)), 220, 32, 30, 32 * (selectedTab % 8), 0);
        for (CustomItemGroup itemGroup : CustomItemGroup.ITEM_GROUPS) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, itemGroup.getResourceLocation());

            matrices.push();
            float widthAndHeight = 26.0f * (3.0f/4.0f);
            LCEDrawableHelper.drawTexture(matrices, 32 * itemGroup.getIndex() + 3 + ((26.0f - widthAndHeight) / 2.0f), 2 + ((26.0f - widthAndHeight) / 2.0f) + (itemGroup.getIndex() % 8 == selectedTab % 8 ? 0 : 1), widthAndHeight, widthAndHeight, 0, 0, 26, 26, 26, 26);
            matrices.pop();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double d = mouseX - (double)this.x;
            double e = mouseY - (double)this.y;
            for (CustomItemGroup customItemGroup : CustomItemGroup.ITEM_GROUPS) {
                if (!this.isClickInTab(customItemGroup, d, e)) continue;
                return true;
            }
            if (this.isClickInScrollbar(mouseX, mouseY)) {
                this.scrolling = this.hasScrollbar();
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double d = mouseX - (double)this.x;
            double e = mouseY - (double)this.y;
            this.scrolling = false;
            for (CustomItemGroup customItemGroup : CustomItemGroup.ITEM_GROUPS) {
                if (!this.isClickInTab(customItemGroup, d, e)) continue;
                this.setSelectedTab(customItemGroup);
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    private boolean hasScrollbar() {
        return this.handler.shouldShowScrollbar();
    }

    private void setSelectedTab(CustomItemGroup group) {
        selectedTab = group.getIndex();
        this.cursorDragSlots.clear();
        this.handler.itemList.clear();
        this.endTouchDrag();
        group.appendStacks(this.handler.itemList);
        this.scrollPosition = 0.0f;
        this.handler.scrollItems(0.0f);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (!this.hasScrollbar()) {
            return false;
        }
        int amountOfPages = MathHelper.ceil((float)this.handler.itemList.size() / (CreativeScreenHandler.INVENTORY_WIDTH * CreativeScreenHandler.INVENTORY_HEIGHT));
        double scroll = amount / (amountOfPages - 1);
        this.scrollPosition = MathHelper.clamp(this.scrollPosition - (float)scroll, 0.0f, 1.0f);
        this.handler.scrollItems(this.scrollPosition);
        return true;
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
        this.lastClickOutsideBounds = bl && !this.isClickInTab(CustomItemGroup.ITEM_GROUPS.get(selectedTab), mouseX, mouseY);
        return this.lastClickOutsideBounds;
    }

    protected boolean isClickInScrollbar(double mouseX, double mouseY) {
        int i = this.x;
        int j = this.y - 20;
        int minX = i + 221;
        int minY = j + 77;
        int maxX = i + 232;
        int maxY = j + 167;
        return mouseX >= (double)minX && mouseY >= (double)minY && mouseX < (double)maxX && mouseY < (double)maxY;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
//        if (this.scrolling) {
//            int i = this.y + 18;
//            int j = i + 112;
//            this.scrollPosition = ((float)mouseY - (float)i - 7.5f) / ((float)(j - i) - 15.0f);
//            this.scrollPosition = MathHelper.clamp(this.scrollPosition, 0.0f, 1.0f);
//            this.handler.scrollItems(this.scrollPosition);
//            return true;
//        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        for (CustomItemGroup customItemGroup : CustomItemGroup.ITEM_GROUPS) {
            if (this.renderTabTooltipIfHovered(matrices, customItemGroup, mouseX, mouseY)) break;
        }
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    protected boolean isClickInTab(CustomItemGroup group, double mouseX, double mouseY) {
        return mouseX >= group.getIndex() * 32 && mouseY >= 0 && mouseX < group.getIndex() * 32 + 32 && mouseY < 26;
    }

    protected boolean renderTabTooltipIfHovered(MatrixStack matrices, CustomItemGroup group, int mouseX, int mouseY) {
        if (this.isPointWithinBounds(group.getIndex() * 32, 0, 32, 26, mouseX, mouseY)) {
            this.renderTooltip(matrices, group.getName(), mouseX, mouseY);
            return true;
        }
        return false;
    }

    protected void renderTabs(MatrixStack matrices, int x, int y) {
        for (int i = 0; i < Math.min(8, CustomItemGroup.ITEM_GROUPS.size()); i++) {
            if (i != selectedTab) this.drawCreativeInventoryTexture(matrices, 142, 220, 32, 30, 32 * (i % 8) + x, y + 1);
        }
    }

    public int getSelectedTab() {
        return selectedTab;
    }

    @Environment(value=EnvType.CLIENT)
    public static class CreativeScreenHandler
            extends ScreenHandler {
        public final DefaultedList<ItemStack> itemList = DefaultedList.of();
        private final ScreenHandler parent;
        private static final int INVENTORY_WIDTH = 10;
        private static final int INVENTORY_HEIGHT = 5;

        public CreativeScreenHandler(PlayerEntity player) {
            super(null, 0);
            this.parent = player.playerScreenHandler;
            PlayerInventory playerInventory = player.getInventory();
            
            for (int x = 0; x < INVENTORY_WIDTH; x++) {
                for (int y = 0; y < INVENTORY_HEIGHT; y++) {
                    this.addSlot(new Slot(INVENTORY, x + y * INVENTORY_WIDTH, x * 18 + 31, y * 18 + 58));
                }
            }

            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, x, x * 18 + 40, 159));
            }
            this.scrollItems(0.0f);
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }

        public void scrollItems(float position) {
            int amountOfPages = MathHelper.ceil((float)this.itemList.size() / (INVENTORY_WIDTH * INVENTORY_HEIGHT));
            int currentTab = (int)(position * (amountOfPages - 1));
            for (int y = 0; y < INVENTORY_HEIGHT; ++y) {
                for (int x = 0; x < INVENTORY_WIDTH; ++x) {
                    int m = x + (y + currentTab * INVENTORY_HEIGHT) * INVENTORY_WIDTH;
                    if (m >= 0 && m < this.itemList.size()) {
                        INVENTORY.setStack(x + y * INVENTORY_WIDTH, this.itemList.get(m));
                        continue;
                    }
                    INVENTORY.setStack(x + y * INVENTORY_WIDTH, ItemStack.EMPTY);
                }
            }
        }

        public boolean shouldShowScrollbar() {
            return this.itemList.size() > INVENTORY.size();
        }

        @Override
        public ItemStack transferSlot(PlayerEntity player, int index) {
            Slot slot = this.slots.get(index);
            if (index >= this.slots.size() - 9 && index < this.slots.size() && slot.hasStack()) {
                slot.setStack(ItemStack.EMPTY);
            }
            return ItemStack.EMPTY;
        }

        @Override
        public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
            return slot.inventory != INVENTORY;
        }

        @Override
        public boolean canInsertIntoSlot(Slot slot) {
            return slot.inventory != INVENTORY;
        }

        @Override
        public ItemStack getCursorStack() {
            return this.parent.getCursorStack();
        }

        @Override
        public void setCursorStack(ItemStack stack) {
            this.parent.setCursorStack(stack);
        }
    }
}

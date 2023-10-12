package net.kyrptonaught.lceui.creativeinv;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryListener;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemGroup;
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
    static final SimpleInventory INVENTORY = new SimpleInventory(60);
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
                        this.client.interactionManager.clickCreativeStack(this.handler.getSlot(60 + j).getStack(), 51 + j);
                    }
                } else if (slot != null) {
                    ItemStack itemStack2 = this.handler.getSlot(slot.id).getStack();
                    this.client.interactionManager.clickCreativeStack(itemStack2, slot.id - this.handler.slots.size() + 9 + 51);
                    int k = 60 + button;
                    if (actionType == SlotActionType.SWAP) {
                        this.client.interactionManager.clickCreativeStack(itemStack, k - this.handler.slots.size() + 9 + 51);
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
            this.setSelectedTab(CustomItemGroup.LCE_ITEM_GROUPS.get(i));
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

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void drawCenteredText(MatrixStack matrices, Text text, float minX, float maxX, float minY, float maxY, float scale, int color) {
        float textX = ((minX + maxX) / scale - (this.textRenderer.getWidth(text))) / 2;
        float textY = ((minY + maxY) / scale - (this.textRenderer.fontHeight)) / 2;
        matrices.scale(scale, scale, scale);
        this.textRenderer.draw(matrices, text, textX, textY, color);
        matrices.scale(1/scale, 1/scale, 1/scale);
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
        this.renderTabs(matrices, MathHelper.floor(this.client.getWindow().getScaledWidth() / 2.0f - 128), MathHelper.ceil(this.client.getWindow().getScaledHeight() / 2.0f - 98));
        this.drawCreativeInventoryTexture(matrices, 0, 20, 256, 193, MathHelper.floor(this.client.getWindow().getScaledWidth() / 2.0f - 128), MathHelper.ceil(this.client.getWindow().getScaledHeight() / 2.0f - 98));
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        if (this.client == null) return;
        Text text;
        if (selectedTab < CustomItemGroup.LCE_ITEM_GROUPS.size()) {
            text = CustomItemGroup.LCE_ITEM_GROUPS.get(selectedTab).getName();
        } else {
            text = Text.translatable("lceui.itemGroup.unknown");
        }
        this.drawCenteredText(matrices, text, 0, this.backgroundWidth, 35, 35, 2.0f/3.0f, 0xFF000000);
        this.drawCreativeInventoryTexture(matrices, 31, 220, 32, 29, 32 * (selectedTab % 8), -1);
        for (CustomItemGroup itemGroup : CustomItemGroup.LCE_ITEM_GROUPS) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, itemGroup.getResourceLocation());

            matrices.push();
            DrawableHelper.drawTexture(matrices, 32 * itemGroup.getIndex() + 3, 1, 0, 0, 26, 26, 26, 26);
            matrices.pop();
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            double d = mouseX - (double)this.x;
            double e = mouseY - (double)this.y;
            for (CustomItemGroup customItemGroup : CustomItemGroup.LCE_ITEM_GROUPS) {
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
            for (CustomItemGroup customItemGroup : CustomItemGroup.LCE_ITEM_GROUPS) {
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
        int i = (this.handler.itemList.size() + 9 - 1) / 9 - 5;
        float f = (float)(amount / (double)i);
        this.scrollPosition = MathHelper.clamp(this.scrollPosition - f, 0.0f, 1.0f);
        this.handler.scrollItems(this.scrollPosition);
        return true;
    }

    @Override
    protected boolean isClickOutsideBounds(double mouseX, double mouseY, int left, int top, int button) {
        boolean bl = mouseX < (double)left || mouseY < (double)top || mouseX >= (double)(left + this.backgroundWidth) || mouseY >= (double)(top + this.backgroundHeight);
        this.lastClickOutsideBounds = bl && !this.isClickInTab(CustomItemGroup.LCE_ITEM_GROUPS.get(selectedTab), mouseX, mouseY);
        return this.lastClickOutsideBounds;
    }

    protected boolean isClickInScrollbar(double mouseX, double mouseY) {
        int i = this.x;
        int j = this.y;
        int k = i + 175;
        int l = j + 18;
        int m = k + 14;
        int n = l + 112;
        return mouseX >= (double)k && mouseY >= (double)l && mouseX < (double)m && mouseY < (double)n;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.scrolling) {
            int i = this.y + 18;
            int j = i + 112;
            this.scrollPosition = ((float)mouseY - (float)i - 7.5f) / ((float)(j - i) - 15.0f);
            this.scrollPosition = MathHelper.clamp(this.scrollPosition, 0.0f, 1.0f);
            this.handler.scrollItems(this.scrollPosition);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        for (CustomItemGroup customItemGroup : CustomItemGroup.LCE_ITEM_GROUPS) {
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
        for (int i = 0; i < Math.min(8, CustomItemGroup.LCE_ITEM_GROUPS.size()); i++) {
            this.drawCreativeInventoryTexture(matrices, 68, 220, 32, 29, 32 * (i % 8) + x, y);
        }
    }

    protected void renderTabIcon(MatrixStack matrices, ItemGroup group) {
        boolean bl = group.getIndex() == selectedTab;
        boolean bl2 = group.isTopRow();
        int i = group.getColumn();
        int j = i * 28;
        int k = 0;
        int l = this.x + 28 * i;
        int m = this.y;
        if (bl) {
            k += 32;
        }
        if (group.isSpecial()) {
            l = this.x + this.backgroundWidth - 28 * (6 - i);
        } else if (i > 0) {
            l += i;
        }
        if (bl2) {
            m -= 28;
        } else {
            k += 64;
            m += this.backgroundHeight - 4;
        }
        this.drawTexture(matrices, l, m, j, k, 28, 32);
        this.itemRenderer.zOffset = 100.0f;
        int n2 = bl2 ? 1 : -1;
        ItemStack itemStack = group.getIcon();
        this.itemRenderer.renderInGuiWithOverrides(itemStack, l += 6, m += 8 + n2);
        this.itemRenderer.renderGuiItemOverlay(this.textRenderer, itemStack, l, m);
        this.itemRenderer.zOffset = 0.0f;
    }

    public int getSelectedTab() {
        return selectedTab;
    }

    @Environment(value=EnvType.CLIENT)
    public static class CreativeScreenHandler
            extends ScreenHandler {
        public final DefaultedList<ItemStack> itemList = DefaultedList.of();
        private final ScreenHandler parent;

        public CreativeScreenHandler(PlayerEntity player) {
            super(null, 0);
            this.parent = player.playerScreenHandler;
            PlayerInventory playerInventory = player.getInventory();
            
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 6; y++) {
                    this.addSlot(new Slot(INVENTORY, x + y * 10, x * 18 + 31, y * 18 + 45));
                }
            }

            for (int x = 0; x < 9; x++) {
                this.addSlot(new Slot(playerInventory, x, x * 18 + 40, 158));
            }
            this.scrollItems(0.0f);
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }

        public void scrollItems(float position) {
            int i = (this.itemList.size() + 10 - 1) / 10 - 6;
            int j = (int)((double)(position * (float)i) + 0.5);
            if (j < 0) {
                j = 0;
            }
            for (int y = 0; y < 6; ++y) {
                for (int x = 0; x < 10; ++x) {
                    int m = x + (y + j) * 10;
                    if (m >= 0 && m < this.itemList.size()) {
                        INVENTORY.setStack(x + y * 10, this.itemList.get(m));
                        continue;
                    }
                    INVENTORY.setStack(x + y * 10, ItemStack.EMPTY);
                }
            }
        }

        public boolean shouldShowScrollbar() {
            return this.itemList.size() > 60;
        }

        @Override
        public ItemStack transferSlot(PlayerEntity player, int index) {
            Slot slot = this.slots.get(index);
            if (index >= this.slots.size() - 10 && index < this.slots.size() && slot.hasStack()) {
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

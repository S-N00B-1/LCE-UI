package net.kyrptonaught.lceui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.container.ContainerInventoryAccessor;
import net.kyrptonaught.lceui.util.LCESounds;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LCE3x3ContainerScreen extends HandledScreen<LCE3x3ContainerScreen.LCE3x3GenericContainerScreenHandler> implements ScreenHandlerProvider<LCE3x3ContainerScreen.LCE3x3GenericContainerScreenHandler> {
    public static final Identifier TEXTURE = new Identifier(LCEUIMod.MOD_ID, "textures/gui/container/generic_3x3.png");

    public LCE3x3ContainerScreen(Generic3x3ContainerScreenHandler handler, PlayerEntity player, Text title) {
        super(new LCE3x3GenericContainerScreenHandler(player, handler), player.getInventory(), title);
        this.backgroundWidth = 430/3;
        this.backgroundHeight = 405/3;
        this.playerInventoryTitleY = this.backgroundHeight - 76;
    }

    @Override
    protected void init() {
        super.init();
        this.client.getSoundManager().play(PositionedSoundInstance.master(LCESounds.CLICK_STEREO, 1.0f, 3.0f));
    }

    @Override
    public void removed() {
        super.removed();
        this.client.getSoundManager().play(PositionedSoundInstance.master(LCESounds.UI_BACK, 1.0f, 3.0f));
    }

    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        LCEDrawableHelper.drawCenteredText(context, this.textRenderer, this.title, this.backgroundWidth / 2, this.backgroundWidth / 2, 10 + 1.0f/3.0f, 10 + 1.0f/3.0f, 2.0f/3.0f, 0x373737);
        LCEDrawableHelper.drawText(context, this.textRenderer, Text.translatable("container.inventory"), 9 + 1.0f/3.0f, this.playerInventoryTitleY + 2 + 1.0f/3.0f, 2.0f/3.0f, 0x373737);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        LCEDrawableHelper.drawTexture(TEXTURE, context, i, j, 0, 0, 430.0f/3.0f, 405.0f/3.0f, 512.0f/3.0f, 512.0f/3.0f);
    }

    @Environment(EnvType.CLIENT)
    public static class LCE3x3GenericContainerScreenHandler extends ScreenHandler {
        private final Generic3x3ContainerScreenHandler parent;

        public LCE3x3GenericContainerScreenHandler(PlayerEntity player, Generic3x3ContainerScreenHandler parent) {
            super(parent.getType(), parent.syncId);
            this.parent = parent;
            Inventory inventory = ((ContainerInventoryAccessor)parent).inventory();

            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;

            float i = -(18 * slotScale - 1.0f/4.0f);


            int j;
            int k;
            for(j = 0; j < 3; ++j) {
                for(k = 0; k < 3; ++k) {
                    this.addSlot(new ScalableSlot(inventory, k + j * 3, 51 + k * (18 * slotScale - 1.0f/4.0f), 16 + j * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale));
                }
            }

            for(j = 0; j < 3; ++j) {
                for(k = 0; k < 9; ++k) {
                    this.addSlot(new ScalableSlot(player.getInventory(), k + j * 9 + 9, 9 + k * (18 * slotScale - 1.0f/4.0f), 84 + j * (18 * slotScale - 1.0f/4.0f) + i, slotScale, slotScale * itemScale));
                }
            }

            for(j = 0; j < 9; ++j) {
                this.addSlot(new ScalableSlot(player.getInventory(), j, 9 + j * (18 * slotScale - 1.0f/4.0f), 130 + i, slotScale, slotScale * itemScale));
            }
        }

        @Override
        public ItemStack quickMove(PlayerEntity player, int index) {
            return this.parent.quickMove(player, index);
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return this.parent.canUse(player);
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

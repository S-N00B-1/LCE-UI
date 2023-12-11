package net.kyrptonaught.lceui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.container.ContainerInventoryAccessor;
import net.kyrptonaught.lceui.util.LCESounds;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LCEShulkerBoxScreen extends HandledScreen<LCEShulkerBoxScreen.LCEShulkerBoxScreenHandler> implements ScreenHandlerProvider<LCEShulkerBoxScreen.LCEShulkerBoxScreenHandler> {
    public LCEShulkerBoxScreen(ShulkerBoxScreenHandler handler, PlayerEntity player, Text title) {
        super(new LCEShulkerBoxScreenHandler(player, handler), player.getInventory(), title);
        this.passEvents = false;
        this.backgroundWidth = 430/3;
        this.backgroundHeight = 415/3;
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

    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        LCEDrawableHelper.drawText(matrices, this.textRenderer, this.title, 9 + 1.0f/3.0f, 7 + 2.0f/3.0f, 2.0f/3.0f, 0x373737);
        LCEDrawableHelper.drawText(matrices, this.textRenderer, Text.translatable("container.inventory"), 9 + 1.0f/3.0f, this.playerInventoryTitleY + 2.0f/3.0f, 2.0f/3.0f, 0x373737);    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(matrices, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, new Identifier(LCEUIMod.MOD_ID, "textures/gui/container/shulker_box.png"));
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        LCEDrawableHelper.drawTexture(matrices, i, j, 0, 0, 430.0f/3.0f, 415.0f/3.0f, 512.0f/3.0f, 512.0f/3.0f);
    }

    @Environment(EnvType.CLIENT)
    public static class LCEShulkerBoxScreenHandler extends ScreenHandler {
        private final ShulkerBoxScreenHandler parent;

        public LCEShulkerBoxScreenHandler(PlayerEntity player, ShulkerBoxScreenHandler parent) {
            super(parent.getType(), parent.syncId);
            this.parent = parent;
            Inventory inventory = ((ContainerInventoryAccessor)parent).inventory();

            float slotScale = 19.0f/24.0f;
            float itemScale = 7.0f/8.0f;

            float i = -(18 * slotScale - 1.0f/4.0f);


            int j;
            int k;
            for(j = 0; j < 3; ++j) {
                for(k = 0; k < 9; ++k) {
                    this.addSlot(new ScalableSlot(inventory, k + j * 9, 9 + k * (18 * slotScale - 1.0f/4.0f), 17 + 1.0f/3.0f + j * (18 * slotScale - 1.0f/4.0f), slotScale, slotScale * itemScale));
                }
            }

            for(j = 0; j < 3; ++j) {
                for(k = 0; k < 9; ++k) {
                    this.addSlot(new ScalableSlot(player.getInventory(), k + j * 9 + 9, 9 + 1.0f/3.0f + k * (18 * slotScale - 1.0f/4.0f), 85 + 1.0f/3.0f + j * (18 * slotScale - 1.0f/4.0f) + i, slotScale, slotScale * itemScale));
                }
            }

            for(j = 0; j < 9; ++j) {
                this.addSlot(new ScalableSlot(player.getInventory(), j, 9 + j * (18 * slotScale - 1.0f/4.0f), 132 + i, slotScale, slotScale * itemScale));
            }
        }

        @Override
        public ItemStack transferSlot(PlayerEntity player, int index) {
            return this.parent.transferSlot(player, index);
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

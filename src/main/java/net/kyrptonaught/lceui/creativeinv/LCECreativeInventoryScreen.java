package net.kyrptonaught.lceui.creativeinv;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class LCECreativeInventoryScreen extends AbstractInventoryScreen<LCECreativeInventoryScreen.LCECreativeScreenHandler> {
    static final SimpleInventory INVENTORY = new SimpleInventory(50);
    static int tabIndex = 1;

    public LCECreativeInventoryScreen(PlayerEntity player) {
        super(new LCECreativeScreenHandler(player), player.getInventory(), ScreenTexts.EMPTY);
    }

    private void drawCreativeInventoryTexture(MatrixStack matrices, int u, int v, int width, int height, int x, int y) {
        if (this.client == null) return;
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, new Identifier(LCEUIMod.MOD_ID, "textures/gui/creativeinv/creative_inventory.png"));

        matrices.push();
        int i;
        int j;
        if (this.client.getWindow().calculateScaleFactor(this.client.options.getGuiScale().getValue(),
                this.client.options.getForceUnicodeFont().getValue()) % 2 == 0) {
            i = this.client.getWindow().getScaledWidth() / 3 - 128;
            j = this.client.getWindow().getScaledHeight() / 3 - 97;
            matrices.scale(1.5f, 1.5f, 1.5f);
        } else {
            i = this.client.getWindow().getScaledWidth() / 2 - 128;
            j = this.client.getWindow().getScaledHeight() / 2 - 97;
        }
        this.drawTexture(matrices, i + x, j + y, u, v, width, height);
        matrices.pop();
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        this.drawCreativeInventoryTexture(matrices, 0, 20, 256, 193, 0, 0);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        if (this.client == null) return;
        Text text = Text.translatable("lceui.itemGroup.building_blocks");
        int textWidth = (int)((this.client.getWindow().getScaledWidth() - this.client.textRenderer.getWidth(text)) / 2.0f);
        int textHeight;
        int tabX;
        int tabY;
        int tabSize;
        if (this.client.getWindow().calculateScaleFactor(this.client.options.getGuiScale().getValue(),
                this.client.options.getForceUnicodeFont().getValue()) % 2 == 0) {
            textHeight = (int)((this.client.getWindow()
                    .getScaledHeight() - this.client.textRenderer.fontHeight) / 2.0f - 90.0f);
            tabX = this.client.getWindow().getScaledWidth() / 2 - 192 + 11;
            tabY = this.client.getWindow().getScaledHeight() / 2 - 145 + 9;
            tabSize = 24;
        } else {
            textHeight = (int)((this.client.getWindow()
                    .getScaledHeight() - this.client.textRenderer.fontHeight) / 2.0f - 60.0f);
            tabX = this.client.getWindow().getScaledWidth() / 2 - 128 + 3;
            tabY = this.client.getWindow().getScaledHeight() / 2 - 97 + 1;
            tabSize = 16;
        }
        this.client.textRenderer.draw(matrices, text, textWidth, textHeight, 0xFF000000);
        this.drawCreativeInventoryTexture(matrices, 31, 220, 32, 29, 32 * tabIndex, 0);
        for (CustomItemGroup itemGroup : CustomItemGroup.LCE_ITEM_GROUPS) {
            RenderSystem.setShaderTexture(0, itemGroup.resourceLocation());
            this.drawTexture(matrices, tabX + (tabSize * itemGroup.index()), tabY, 0, 0, 26, 26);
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.drawBackground(matrices, delta, mouseX, mouseY);
        this.drawForeground(matrices, mouseX, mouseY);
    }

    @Override
    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);
    }

    @Environment(EnvType.CLIENT)
    public static class LCECreativeScreenHandler extends ScreenHandler {

        private final PlayerEntity player;

        protected LCECreativeScreenHandler(PlayerEntity player) {
            super(null, 0);
            this.player = player;

            for (int y = 0; y < 10; y++) {
                for (int x = 0; x < 6; x++) {
                    this.addSlot(new Slot(INVENTORY, x * x, x * 18, y * 18));
                }
            }
        }

        @Override
        public ItemStack transferSlot(PlayerEntity player, int index) {
            if (index >= this.slots.size() - 9 && index < this.slots.size()) {
                Slot slot = (Slot)this.slots.get(index);
                if (slot.hasStack()) {
                    slot.setStack(ItemStack.EMPTY);
                }
            }

            return ItemStack.EMPTY;
        }

        @Override
        public boolean canUse(PlayerEntity player) {
            return true;
        }
    }
}

package net.kyrptonaught.lceui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenHandlerCraftingAccessor;
import net.kyrptonaught.lceui.util.LCESounds;
import net.kyrptonaught.lceui.util.ScalableCraftingResultSlot;
import net.kyrptonaught.lceui.util.ScalableSlot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class LCESurvivalInventoryScreen extends AbstractInventoryScreen<LCESurvivalInventoryScreen.SurvivalScreenHandler> {
    private float mouseX;
    private float mouseY;
    private boolean mouseDown;

    public LCESurvivalInventoryScreen(PlayerEntity player) {
        super(new SurvivalScreenHandler(player), player.getInventory(), Text.translatable("container.inventory"));
        this.backgroundHeight = 435/3;
        this.backgroundWidth = 431/3;
    }

    @Override
    protected void init() {
        super.init();
        this.client.player.playSound(LCESounds.CLICK_STEREO, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    @Override
    public void removed() {
        super.removed();
        this.client.player.playSound(LCESounds.UI_BACK, SoundCategory.MASTER, 1.0f, 1.0f);
    }

    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        LCEDrawableHelper.drawText(context, this.textRenderer, this.title, 9 + 1.0f/3.0f, 69 - 1.0f/3.0f, 2.0f/3.0f, 0x373737);
        if (LCEUIMod.getConfig().classicCrafting) {
            LCEDrawableHelper.drawText(context, this.textRenderer, Text.translatable("container.crafting"), 74, 10 + 2.0f/3.0f, 2.0f/3.0f, 0x373737);
        }
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        this.drawMouseoverTooltip(context, mouseX, mouseY);
        this.mouseX = (float)mouseX;
        this.mouseY = (float)mouseY;
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        int i = this.x;
        int j = this.y;
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(this.x, this.y, 0);
        matrices.scale(1.0f/3.0f, 1.0f/3.0f, 1.0f);
        matrices.translate(-this.x, -this.y, 0);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        LCEDrawableHelper.drawTexture(new Identifier(LCEUIMod.MOD_ID, "textures/gui/survival_inventory.png"), context, i, j, LCEUIMod.getConfig().classicCrafting ? 431 : 0, 0, 431, 435, 1024, 1024);
        matrices.pop();
        InventoryScreen.drawEntity(context, i + (LCEUIMod.getConfig().classicCrafting ? 48 : 81), j + 57, 22, (float)(i + (LCEUIMod.getConfig().classicCrafting ? 48 : 81)) - this.mouseX, (float)(j + 22) - this.mouseY, this.client.player);
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.mouseDown) {
            this.mouseDown = false;
            return true;
        } else {
            return super.mouseReleased(mouseX, mouseY, button);
        }
    }

    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        super.onMouseClick(slot, slotId, button, actionType);
    }

    @Environment(EnvType.CLIENT)
    public static class SurvivalScreenHandler extends ScreenHandler {
        private static final EquipmentSlot[] EQUIPMENT_SLOT_ORDER;
        private final PlayerScreenHandler parent;

        public SurvivalScreenHandler(PlayerEntity player) {
            super(null, player.playerScreenHandler.syncId);
            this.parent = player.playerScreenHandler;
            
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
                        PlayerScreenHandler.onEquipStack(player, equipmentSlot, stack, this.getStack());
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

            this.addSlot(new ScalableSlot(player.getInventory(), 40, LCEUIMod.getConfig().classicCrafting ? 74 : 107, 51 + 1.0f/3.0f, slotScale, slotScale * itemScale) {
                public void setStack(ItemStack stack) {
                    PlayerScreenHandler.onEquipStack(player, EquipmentSlot.OFFHAND, stack, this.getStack());
                    super.setStack(stack);
                }

                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return LCEUIMod.getConfig().ps4BackgroundSprites ? Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(LCEUIMod.MOD_ID, "item/ps4_edition_empty_armor_slot_shield")) : null;
                }
            });
        }

        public boolean canUse(PlayerEntity player) {
            return this.parent.canUse(player);
        }

        public ItemStack quickMove(PlayerEntity player, int index) {
            return this.parent.quickMove(player, index);
        }

        public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
            return this.parent.canInsertIntoSlot(stack, slot);
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
    }
}

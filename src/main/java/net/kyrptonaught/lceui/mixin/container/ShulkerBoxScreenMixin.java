package net.kyrptonaught.lceui.mixin.container;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.screens.LCEContainerScreen;
import net.kyrptonaught.lceui.screens.LCEShulkerBoxScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ShulkerBoxScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ShulkerBoxScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ShulkerBoxScreen.class)
public abstract class ShulkerBoxScreenMixin extends HandledScreen<ShulkerBoxScreenHandler> {
    public ShulkerBoxScreenMixin(ShulkerBoxScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        if (LCEUIMod.getConfig().shulkerBoxInventory) {
            MinecraftClient client = ((ScreenAccessor) ((ShulkerBoxScreen) (Object) this)).client();
            if (client != null) {
                this.removed();
                client.setScreen(new LCEShulkerBoxScreen(this.handler, ((ScreenAccessor) this).client().player, this.title));
            }
        } else {
            super.init();
        }
    }
}

package net.kyrptonaught.lceui.mixin.container;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.screens.LCEContainerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(GenericContainerScreen.class)
public abstract class GenericContainerMixin extends HandledScreen<GenericContainerScreenHandler> {
    public GenericContainerMixin(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        if (LCEUIMod.getConfig().containerInventory) {
            MinecraftClient client = ((ScreenAccessor) ((GenericContainerScreen) (Object) this)).client();
            if (client != null) {
                this.removed();
                client.setScreen(new LCEContainerScreen(this.handler, ((ScreenAccessor) this).client().player, this.title));
            }
        } else {
            super.init();
        }
    }
}

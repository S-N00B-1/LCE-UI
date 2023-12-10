package net.kyrptonaught.lceui.mixin.container;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.HandledScreenAccessor;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.screens.LCE3x3ContainerScreen;
import net.kyrptonaught.lceui.screens.LCEContainerScreen;
import net.kyrptonaught.lceui.screens.LCEHopperScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.HopperScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(HopperScreen.class)
public abstract class HopperScreenMixin extends HandledScreen<HopperScreenHandler> {
    public HopperScreenMixin(HopperScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        if (LCEUIMod.getConfig().hopperInventory) {
            MinecraftClient client = ((ScreenAccessor) ((HopperScreen) (Object) this)).client();
            if (client != null) {
                this.removed();
                client.setScreen(new LCEHopperScreen(this.handler, ((ScreenAccessor) this).client().player, this.title));
            }
        } else {
            super.init();
        }
    }
}

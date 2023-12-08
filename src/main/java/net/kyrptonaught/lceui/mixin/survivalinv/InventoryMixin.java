package net.kyrptonaught.lceui.mixin.survivalinv;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryScreen.class)
public class InventoryMixin {
    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasCreativeInventory()Z"))
    private boolean doNotCloseInventoryInCreative1(ClientPlayerInteractionManager instance) {
        return !LCEUIMod.getConfig().creativeInventory && instance.hasCreativeInventory();
    }

    @Redirect(method = "handledScreenTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasCreativeInventory()Z"))
    private boolean doNotCloseInventoryInCreative2(ClientPlayerInteractionManager instance) {
        return !LCEUIMod.getConfig().creativeInventory && instance.hasCreativeInventory();
    }
}

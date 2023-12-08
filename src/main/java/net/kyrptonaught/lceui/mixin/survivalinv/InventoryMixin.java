package net.kyrptonaught.lceui.mixin.survivalinv;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenClientAccessor;
import net.kyrptonaught.lceui.screens.LCESurvivalInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryMixin {
    @Shadow public abstract void removed();

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    private void hijackInit(CallbackInfo ci) {
        MinecraftClient client = ((ScreenClientAccessor) ((InventoryScreen) (Object) this)).client();
        if (LCEUIMod.getConfig().survivalInventory && (LCEUIMod.getConfig().creativeInventory || !client.interactionManager.hasCreativeInventory())) {
            this.removed();
            client.setScreen(new LCESurvivalInventoryScreen(client.player));
            ci.cancel();
        }
    }

    @Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasCreativeInventory()Z"))
    private boolean doNotCloseInventoryInCreative1(ClientPlayerInteractionManager instance) {
        return !LCEUIMod.getConfig().creativeInventory && instance.hasCreativeInventory();
    }

    @Redirect(method = "handledScreenTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasCreativeInventory()Z"))
    private boolean doNotCloseInventoryInCreative2(ClientPlayerInteractionManager instance) {
        return !LCEUIMod.getConfig().creativeInventory && instance.hasCreativeInventory();
    }
}

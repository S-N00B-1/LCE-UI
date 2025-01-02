package net.kyrptonaught.lceui.mixin.gui;

import net.kyrptonaught.lceui.util.FadeOutTracker;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class syncSelectedSlotMixin {
    @Inject(method = "syncSelectedSlot", at= @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/packet/Packet;)V"))
    public void resetHudTrans(CallbackInfo ci) {
        FadeOutTracker.tryFadeIn();
        //FadeOutTracker.resetFadeOutAmount();
        FadeOutTracker.resetTimeToFadeOut();
    }
}

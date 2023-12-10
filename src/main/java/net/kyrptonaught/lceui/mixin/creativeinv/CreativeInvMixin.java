package net.kyrptonaught.lceui.mixin.creativeinv;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.screens.LCECreativeInventoryScreen;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInvMixin {
    @Shadow public abstract void removed();

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    public void hijackInit(CallbackInfo ci) {
        if (LCEUIMod.getConfig().creativeInventory) {
            MinecraftClient client = ((ScreenAccessor) ((CreativeInventoryScreen) (Object) this)).client();
            if (client != null) {
                this.removed();
                client.setScreen(new LCECreativeInventoryScreen(client.player));
                ci.cancel();
            }
        }
    }
}

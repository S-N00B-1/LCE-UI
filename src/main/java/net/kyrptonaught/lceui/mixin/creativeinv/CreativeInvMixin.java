package net.kyrptonaught.lceui.mixin.creativeinv;

import net.kyrptonaught.lceui.creativeinv.LCECreativeInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public class CreativeInvMixin {
    @Inject(method = "init", at = @At("HEAD"))
    public void hijackInit(CallbackInfo ci) {
        MinecraftClient.getInstance().setScreen(new LCECreativeInventoryScreen(MinecraftClient.getInstance().player));
    }
}

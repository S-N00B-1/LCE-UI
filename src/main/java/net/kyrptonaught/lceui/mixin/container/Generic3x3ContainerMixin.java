package net.kyrptonaught.lceui.mixin.container;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.HandledScreenAccessor;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.screens.LCE3x3ContainerScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.Generic3x3ContainerScreen;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Generic3x3ContainerScreen.class)
public abstract class Generic3x3ContainerMixin {
    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        if (LCEUIMod.getConfig().containerInventory) {
            MinecraftClient client = ((ScreenAccessor) ((Generic3x3ContainerScreen) (Object) this)).client();
            if (client != null) {
                client.setScreen(new LCE3x3ContainerScreen((Generic3x3ContainerScreenHandler) ((HandledScreenAccessor<?>)this).handler(), ((ScreenAccessor) this).client().player, ((ScreenAccessor)this).title()));
            }
        }
    }
}

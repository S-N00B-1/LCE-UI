package net.kyrptonaught.lceui.mixin.creativeinv;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.creativeinv.LCECreativeInventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInvMixin {
    @Shadow public abstract void removed();

    @Mixin(Screen.class)
    private interface ClientAccessor {
        @Accessor("client")
        MinecraftClient client();
    }

    @Inject(method = "init", at = @At("HEAD"), cancellable = true)
    public void hijackInit(CallbackInfo ci) {
        if (LCEUIMod.getConfig().creativeInventory) {
            this.removed();
            ((ClientAccessor) ((CreativeInventoryScreen) (Object) this)).client().setScreen(new LCECreativeInventoryScreen(((ClientAccessor) ((CreativeInventoryScreen) (Object) this)).client().player));
            ci.cancel();
        }
    }
}

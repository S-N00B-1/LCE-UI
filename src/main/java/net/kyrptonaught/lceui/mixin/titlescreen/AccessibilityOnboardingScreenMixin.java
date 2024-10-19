package net.kyrptonaught.lceui.mixin.titlescreen;

import net.kyrptonaught.lceui.titlescreen.LegacyPanoramaRenderer;
import net.kyrptonaught.lceui.titlescreen.PanoramaHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AccessibilityOnboardingScreen.class)
public class AccessibilityOnboardingScreenMixin {
    @Unique private static final LegacyPanoramaRenderer PANORAMA_CUBE_MAP = PanoramaHolder.PANORAMA_CUBE_MAP;
    private final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method="renderBackground",at=@At("HEAD"), cancellable = true)
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        PANORAMA_CUBE_MAP.render(context, context.getMatrices(),0.0F);
        context.fill(0, 0, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight(), -1877995504);
        ci.cancel();
    }

}

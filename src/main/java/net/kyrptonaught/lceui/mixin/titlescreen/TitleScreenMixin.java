package net.kyrptonaught.lceui.mixin.titlescreen;

import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.titlescreen.LegacyPanoramaRenderer;
import net.kyrptonaught.lceui.titlescreen.PanoramaHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    private static final Identifier lceuiPan = PanoramaHolder.lceuiPan;
    private static final LegacyPanoramaRenderer PANORAMA_CUBE_MAP = PanoramaHolder.PANORAMA_CUBE_MAP;
    private static final Identifier l4jPan = PanoramaHolder.l4jPan;
    private static final LegacyPanoramaRenderer L4J_SUPPORT_PANORAMA_CUBE_MAP = PanoramaHolder.L4J_SUPPORT_PANORAMA_CUBE_MAP;

    /* TODO
       Implement a method to convert the existing cubemap into a equirectangular panorama
       & Crop out the top/bottom portions (<- Common mistake people make when making new panoramas for rp's.)

       Make it bound to a config like 'convert vanilla panorama' or something.
    */

    private final MinecraftClient client = MinecraftClient.getInstance();

    @Inject(method = "render", at=@At("HEAD"))
    public void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (LCEUIMod.getConfig().l4jPanSupport && this.client.getResourceManager().getResource(l4jPan).isPresent()) {
            L4J_SUPPORT_PANORAMA_CUBE_MAP.render(context, context.getMatrices(), delta);
        } else if (this.client.getResourceManager().getResource(lceuiPan).isPresent()) {
            PANORAMA_CUBE_MAP.render(context, context.getMatrices(), delta);
        }
    }
}
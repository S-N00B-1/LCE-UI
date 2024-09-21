package net.kyrptonaught.lceui.mixin.tooltip;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipBackgroundRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TooltipBackgroundRenderer.class)
public class TooltipMixin {

    private static final Identifier TOP_LEFT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/top_left.png");
    private static final Identifier TOP_CENTER = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/top_center.png");
    private static final Identifier TOP_RIGHT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/top_right.png");

    private static final Identifier MIDDLE_LEFT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/middle_left.png");
    private static final Identifier MIDDLE_CENTER = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/middle_center.png");
    private static final Identifier MIDDLE_RIGHT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/middle_right.png");

    private static final Identifier BOTTOM_LEFT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/bottom_left.png");
    private static final Identifier BOTTOM_CENTER = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/bottom_center.png");
    private static final Identifier BOTTOM_RIGHT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/tooltip/bottom_right.png");


    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIIII)V", at = @At("HEAD"), cancellable = true)
    private static void render(DrawContext context, int x, int y, int width, int height, int z, CallbackInfo ci) {
        if (LCEUIMod.getConfig().tooltips && width < 1000) {
            x = x - 6;
            y = y - 6;
            width = width + 12;
            height = height + 12;
            drawTexture(context, x, y, z, 8, 8, TOP_LEFT);
            drawTexture(context, x + 8, y, z, width - 16, 8, TOP_CENTER);
            drawTexture(context, x + width - 8, y, z, 8, 8, TOP_RIGHT);

            y = y + 8;
            drawTexture(context, x, y, z, 8, height - 16, MIDDLE_LEFT);
            drawTexture(context, x + 8, y, z, width - 16, height - 16, MIDDLE_CENTER);
            drawTexture(context, x + width - 8, y, z, 8, height - 16, MIDDLE_RIGHT);

            y = y + height - 16;
            drawTexture(context, x, y, z, 8, 8, BOTTOM_LEFT);
            drawTexture(context, x + 8, y, z, width - 16, 8, BOTTOM_CENTER);
            drawTexture(context, x + width - 8, y, z, 8, 8, BOTTOM_RIGHT);

            ci.cancel();
        }
    }



    @Unique
    private static void drawTexture(DrawContext context, int x, int y, int z, int width, int height,  Identifier texture) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, texture);

        context.drawTexture(texture, x, y, z, 0, 0, width, height, 8, 8);

        RenderSystem.disableBlend();
    }

}

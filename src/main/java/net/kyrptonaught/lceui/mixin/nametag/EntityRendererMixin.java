/*package net.kyrptonaught.lceui.mixin.nametag;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.GlyphRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.client.font.TextRenderer;

import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    @Shadow @Final private TextRenderer textRenderer;
    @Shadow public abstract TextRenderer getTextRenderer();

    @Inject(method = "renderLabelIfPresent", at = @At(value = "RETURN", target = "Lnet/minecraft/client/util/math/MatrixStack;scale(FFF)V", shift = At.Shift.AFTER))
    protected void renderLabelIfPresent(Entity entity, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        float thickness = Math.max(0.1f,minecraft.player.distanceTo(entity) / 16f);

        int j =  "deadmau5".equals(text.getString()) ? -10 : 0;
        float h = (int) (-textRenderer.getWidth(text) / 2f);
        float[] color = new float[]{255f,255f,255f}; // Replace this with covertColorValueToRGB(entity.getTeamColorValue()) once implemented

        matrices.push();

        renderOutline(vertexConsumers.getBuffer(entity.isSneaky() ?  RenderLayer.getTextBackground() : RenderLayer.getTextBackgroundSeeThrough()), matrices, h - 1.1f, j - 1.1f, this.getTextRenderer().getWidth(text.getString()) + 2.1f,10.1f, thickness, color[0],color[1],color[2],255.0f);

        matrices.pop();
    }

    public void renderOutline(VertexConsumer vertexConsumer, MatrixStack matrices, float x, float y, float width, float height, float thickness, float r, float g, float b , float a) {
        this.fill(vertexConsumer,matrices,x, y, x + width, y + thickness, r,g,b,a);
        this.fill(vertexConsumer,matrices,x, y + height - thickness, x + width, y + height, r,g,b,a);
        this.fill(vertexConsumer,matrices,x, y + thickness, x + thickness, y + height - thickness, r,g,b,a);
        this.fill(vertexConsumer,matrices,x + width - thickness, y + thickness, x + width, y + height - thickness, r,g,b,a);
    }

    public void fill(VertexConsumer vertexConsumer, MatrixStack matrices, float i, float j, float k, float l, float r, float g, float b , float a) {
        float o;
        Matrix4f matrix4f = matrices.peek().getPositionMatrix();
        if (i < k) {
            o = i;
            i = k;
            k = o;
        }
        if (j < l) {
            o = j;
            j = l;
            l = o;
        }
        vertexConsumer.vertex(matrix4f, i, j, 0).color(r,g,b,a).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
        vertexConsumer.vertex(matrix4f, i, l, 0).color(r,g,b,a).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
        vertexConsumer.vertex(matrix4f, k, l, 0).color(r,g,b,a).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
        vertexConsumer.vertex(matrix4f, k, j, 0).color(r,g,b,a).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).next();
    }

    private float[] covertColorValueToRGB(int teamColorValue) {
        float r = 0.0f;
        float g = 255.0f;
        float b = 0.0f;
        return new float[] {r,g,b};
    }
}*/

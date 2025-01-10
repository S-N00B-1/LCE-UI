package net.kyrptonaught.lceui.util;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.gl.WindowFramebuffer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.Window;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL30;

public class TransItemBuffer {
    private static final Window window = MinecraftClient.getInstance().getWindow();
    private static Framebuffer itemBuffer;

    public static void startItemBuffer() {
        itemBuffer = new WindowFramebuffer(window.getWidth(), window.getHeight());
        itemBuffer.setClearColor(0,0,0,0);
        itemBuffer.clear(MinecraftClient.IS_SYSTEM_MAC);
        itemBuffer.beginWrite(true);
    }

    public static void endItemBuffer() {
        itemBuffer.endWrite();
        MinecraftClient.getInstance().getFramebuffer().beginWrite(true);
    }

    public static void drawItemBuffer(DrawContext context) {
        if (itemBuffer == null)  return;

        int width = window.getScaledWidth();
        int height = window.getScaledHeight();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, itemBuffer.getColorAttachment());

        if (itemBuffer.textureWidth != width || itemBuffer.textureHeight != height) {
            itemBuffer = null;
            return;
        }

        context.getMatrices().push();

        BufferBuilder itemBufferBuilder = Tessellator.getInstance().getBuffer();
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        itemBufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        itemBufferBuilder.vertex(matrix4f, 0, height, 0).texture(0,0).next();
        itemBufferBuilder.vertex(matrix4f, width, height, 0).texture(1,0).next();
        itemBufferBuilder.vertex(matrix4f, width, 0, 0).texture(1,1).next();
        itemBufferBuilder.vertex(matrix4f, 0, 0, 0).texture(0,1).next();
        BufferRenderer.drawWithGlobalProgram(itemBufferBuilder.end());
        RenderSystem.disableBlend();
        context.getMatrices().pop();
    }
}

package net.kyrptonaught.lceui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.math.Matrix4f;

public class LCEDrawableHelper {
    public static void drawCenteredText(MatrixStack matrices, TextRenderer textRenderer, Text text, float minX, float maxX, float minY, float maxY, float scale, int color) {
        float textX = ((minX + maxX) / scale - (textRenderer.getWidth(text))) / 3;
        float textY = ((minY + maxY) / scale - (textRenderer.fontHeight)) / 3;
        drawText(matrices, textRenderer, text, textX, textY, scale, color);
    }

    public static int drawTextWithShadow(MatrixStack matrices, TextRenderer textRenderer, OrderedText text, float x, float y, float scale, int color) {
        matrices.translate(x, y, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(-x, -y, 0.0f);
        int width;
        if (LCEUIMod.getConfig().closerTextShadows) {
            MutableText blackText = Text.literal("");
            text.accept(((index, style, codePoint) -> {
                blackText.append(Text.literal(String.valueOf((char)codePoint)).setStyle(style.withColor((color) & 0xFF000000)));
                return true;
            }));
            textRenderer.draw(matrices, blackText.asOrderedText(), x + 1.0f / 3.0f, y + 1.0f / 3.0f, (color) & 0xFF000000);
            width = textRenderer.draw(matrices, text, x, y, color);
        } else {
            width = textRenderer.drawWithShadow(matrices, text, x, y, color);
        }
        matrices.translate(x, y, 0.0f);
        matrices.scale(1/scale, 1/scale, 1.0f);
        matrices.translate(-x, -y, 0.0f);
        return width;
    }

    public static int drawTextWithShadow(MatrixStack matrices, TextRenderer textRenderer, Text text, float x, float y, float scale, int color) {
        return drawTextWithShadow(matrices, textRenderer, text.asOrderedText(), x, y, scale, color);
    }

    public static void drawText(MatrixStack matrices, TextRenderer textRenderer, OrderedText text, float x, float y, float scale, int color) {
        matrices.translate(x, y, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(-x, -y, 0.0f);
        textRenderer.draw(matrices, text, x, y, color);
        matrices.translate(x, y, 0.0f);
        matrices.scale(1/scale, 1/scale, 1.0f);
        matrices.translate(-x, -y, 0.0f);
    }

    public static void drawText(MatrixStack matrices, TextRenderer textRenderer, Text text, float x, float y, float scale, int color) {
        drawText(matrices, textRenderer, text.asOrderedText(), x, y, scale, color);
    }

    public static void drawTexture(MatrixStack matrices, float x, float y, float z, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        drawTexture(matrices, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(MatrixStack matrices, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight) {
        drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(MatrixStack matrices, float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }

    public static void fillGradient(MatrixStack matrices, float startX, float startY, float endX, float endY, int colorStart, int colorEnd, int z) {
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        fillGradient(matrices.peek().getPositionMatrix(), bufferBuilder, startX, startY, endX, endY, z, colorStart, colorEnd);
        tessellator.draw();
        RenderSystem.disableBlend();
        RenderSystem.enableTexture();
    }

    protected static void fillGradient(Matrix4f matrix, BufferBuilder builder, float startX, float startY, float endX, float endY, int colorStart, int colorEnd, int z) {
        float f = (float)(colorStart >> 24 & 0xFF) / 255.0f;
        float g = (float)(colorStart >> 16 & 0xFF) / 255.0f;
        float h = (float)(colorStart >> 8 & 0xFF) / 255.0f;
        float i = (float)(colorStart & 0xFF) / 255.0f;
        float j = (float)(colorEnd >> 24 & 0xFF) / 255.0f;
        float k = (float)(colorEnd >> 16 & 0xFF) / 255.0f;
        float l = (float)(colorEnd >> 8 & 0xFF) / 255.0f;
        float m = (float)(colorEnd & 0xFF) / 255.0f;
        builder.vertex(matrix, endX, startY, z).color(g, h, i, f).next();
        builder.vertex(matrix, startX, startY, z).color(g, h, i, f).next();
        builder.vertex(matrix, startX, endY, z).color(k, l, m, j).next();
        builder.vertex(matrix, endX, endY, z).color(k, l, m, j).next();
    }

    private static void drawTexture(MatrixStack matrices, float x0, float x1, float y0, float y1, float z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight) {
        drawTexturedQuad(matrices.peek().getPositionMatrix(), x0, x1, y0, y1, z, (u + 0.0f) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0f) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
    }

    private static void drawTexturedQuad(Matrix4f matrix, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix, x0, y1, z).texture(u0, v1).next();
        bufferBuilder.vertex(matrix, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix, x1, y0, z).texture(u1, v0).next();
        bufferBuilder.vertex(matrix, x0, y0, z).texture(u0, v0).next();
        BufferRenderer.drawWithShader(bufferBuilder.end());
    }
}

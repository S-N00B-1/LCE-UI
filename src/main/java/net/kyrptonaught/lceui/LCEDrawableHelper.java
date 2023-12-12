package net.kyrptonaught.lceui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

public class LCEDrawableHelper {
    public static float drawCenteredText(DrawContext context, TextRenderer textRenderer, Text text, float minX, float maxX, float minY, float maxY, float scale, int color) {
        float textX = ((minX + maxX) / scale - (textRenderer.getWidth(text))) / 3;
        float textY = ((minY + maxY) / scale - (textRenderer.fontHeight)) / 3;
        return drawText(context, textRenderer, text, textX, textY, scale, color);
    }

    public static float drawTextWithShadow(DrawContext context, TextRenderer textRenderer, OrderedText text, float x, float y, float scale, int color) {
        MatrixStack matrices = context.getMatrices();
        matrices.translate(x, y, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(-x, -y, 0.0f);
        float width;
        if (LCEUIMod.getConfig().closerTextShadows) {
            MutableText blackText = Text.literal("");
            text.accept(((index, style, codePoint) -> {
                blackText.append(Text.literal(String.valueOf((char)codePoint)).setStyle(style.withColor((color) & 0xFF000000)));
                return true;
            }));
            textRenderer.draw(blackText.asOrderedText(), x + 1.0f / 3.0f, y + 1.0f / 3.0f, (color) & 0xFF000000, false, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
            width = 1.0f/3.0f + textRenderer.draw(text, x, y, color, false, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        } else {
            width = textRenderer.draw(text, x, y, color, true, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        }
        matrices.translate(x, y, 0.0f);
        matrices.scale(1/scale, 1/scale, 1.0f);
        matrices.translate(-x, -y, 0.0f);
        return width * scale;
    }

    public static float drawTextWithShadow(DrawContext context, TextRenderer textRenderer, Text text, float x, float y, float scale, int color) {
        return drawTextWithShadow(context, textRenderer, text.asOrderedText(), x, y, scale, color);
    }

    public static float drawText(DrawContext context, TextRenderer textRenderer, OrderedText text, float x, float y, float scale, int color) {
        MatrixStack matrices = context.getMatrices();
        matrices.translate(x, y, 0.0f);
        matrices.scale(scale, scale, 1.0f);
        matrices.translate(-x, -y, 0.0f);
        int width = textRenderer.draw(text, x, y, color, false, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), TextRenderer.TextLayerType.NORMAL, 0, 15728880);
        matrices.translate(x, y, 0.0f);
        matrices.scale(1/scale, 1/scale, 1.0f);
        matrices.translate(-x, -y, 0.0f);
        return width * scale;
    }

    public static float drawText(DrawContext context, TextRenderer textRenderer, Text text, float x, float y, float scale, int color) {
        return drawText(context, textRenderer, text.asOrderedText(), x, y, scale, color);
    }

    public static void drawTexture(Identifier texture, DrawContext context, float x, float y, float z, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        drawTexture(texture, context, x, x + width, y, y + height, z, width, height, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(Identifier texture, DrawContext context, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, float textureWidth, float textureHeight) {
        drawTexture(texture, context, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
    }

    public static void drawTexture(Identifier texture, DrawContext context, float x, float y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        drawTexture(texture, context, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
    }

    private static void drawTexture(Identifier texture, DrawContext context, float x0, float x1, float y0, float y1, float z, float regionWidth, float regionHeight, float u, float v, float textureWidth, float textureHeight) {
        drawTexturedQuad(texture, context, x0, x1, y0, y1, z, (u + 0.0f) / textureWidth, (u + regionWidth) / textureWidth, (v + 0.0f) / textureHeight, (v + regionHeight) / textureHeight);
    }

    private static void drawTexturedQuad(Identifier texture, DrawContext context, float x1, float x2, float y1, float y2, float z, float u1, float u2, float v1, float v2) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        Matrix4f matrix4f = context.getMatrices().peek().getPositionMatrix();
        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(matrix4f, x1, y1, z).texture(u1, v1).next();
        bufferBuilder.vertex(matrix4f, x1, y2, z).texture(u1, v2).next();
        bufferBuilder.vertex(matrix4f, x2, y2, z).texture(u2, v2).next();
        bufferBuilder.vertex(matrix4f, x2, y1, z).texture(u2, v1).next();
        BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
    }
}

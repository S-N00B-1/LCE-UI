package net.kyrptonaught.lceui.whatsThis;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class DescriptionRenderer {
    private static final Identifier slot = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/slot.png");
    private static final Identifier TOP_LEFT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/top_left.png");
    private static final Identifier TOP_CENTER = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/top_center.png");
    private static final Identifier TOP_RIGHT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/top_right.png");

    private static final Identifier MIDDLE_LEFT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/middle_left.png");
    private static final Identifier MIDDLE_CENTER = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/middle_center.png");
    private static final Identifier MIDDLE_RIGHT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/middle_right.png");

    private static final Identifier BOTTOM_LEFT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/bottom_left.png");
    private static final Identifier BOTTOM_CENTER = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/bottom_center.png");
    private static final Identifier BOTTOM_RIGHT = new Identifier(LCEUIMod.MOD_ID, "textures/gui/whatsthis/popup/bottom_right.png");

    public static DescriptionInstance renderingDescription;

    public static boolean setToRender(DescriptionInstance descriptionInstance, boolean bypassViewedCheck) {
        if ((renderingDescription != null && !bypassViewedCheck) || descriptionInstance == null)
            return false;

        if (!bypassViewedCheck) {
            String key = descriptionInstance.getGroupKey();
            if (WhatsThisInit.descriptionManager.viewedDescriptions.contains(key))
                return false;
            WhatsThisInit.descriptionManager.viewedDescriptions.add(key);
        }

        renderingDescription = descriptionInstance;
        return true;
    }

    public static void renderDescription(MinecraftClient client, MatrixStack matrixStack, float delta) {
        if (renderingDescription == null) return;

        if (!client.isPaused())
            renderingDescription.tickOpen(delta);

        if (renderingDescription.shouldClose(client)) {
            renderingDescription = null;
            return;
        }

        if (renderingDescription.shouldHide(client)) return;

        int x = client.getWindow().getScaledWidth() - (500 / 3) - 20;

        BakedModel bakedModel = renderingDescription.getDisplayModel(client);

        render(client, matrixStack, x, 20, renderingDescription.getNameTranslation(), renderingDescription.getDescTranslation(), renderingDescription.getItemStack(), bakedModel, bakedModel != null);
    }

    private static void render(MinecraftClient client, MatrixStack matrices, int x, int y, Text titleText, Text descriptionText, ItemStack stack, BakedModel model, boolean displayModel) {
        TextRenderer textRenderer = client.textRenderer;

        List<OrderedText> description = textRenderer.wrapLines(descriptionText, 223);

        float lineHeight = 33 + (description.size() * 8.0f);

        float totalHeight = lineHeight - 31.0f / 3.0f;
        if (displayModel) totalHeight = lineHeight + 32.0f / 3.0f;

        renderBackground(matrices, x, y, (500.0f / 3.0f), totalHeight);

        LCEDrawableHelper.drawTextWithShadow(matrices, textRenderer, titleText, x + 9, y + 26.0f / 3.0f, 2.0f/3.0f, 0xFFFFFF);
        for (int i = 0; i < description.size(); i++)
            LCEDrawableHelper.drawText(matrices, textRenderer, description.get(i), x + 9, y + 52.0f / 3.0f + (i * 8.0f), 2.0f/3.0f, 0xFFFFFF);

        if (displayModel) {
            float scale = 4.0f / 3.0f;
            float slotX = x + 218.0f / 3.0f;
            float slotY = 9.0f / 3.0f + lineHeight;

            RenderSystem.setShaderTexture(0, slot);

            LCEDrawableHelper.drawTexture(matrices, slotX, slotY, 0, 0, 64.0f / 3.0f, 64.0f / 3.0f, 64.0f / 3.0f, 64.0f / 3.0f);
            renderGuiItemModel(client.getTextureManager(), client.getItemRenderer(), stack, slotX + 8.0f / 3.0f, slotY + 8.0f / 3.0f, scale * 54.0f / 64.0f, model);
        }
    }

    private static void renderBackground(MatrixStack matrices, float x, float y, float width, float height) {
        drawTexture(matrices, x, y, 8, 8, TOP_LEFT);
        drawTexture(matrices, x + 8, y, width - 16, 8, TOP_CENTER);
        drawTexture(matrices, x + width - 8, y, 8, 8, TOP_RIGHT);

        y = y + 8;
        drawTexture(matrices, x, y, 8, height - 16, MIDDLE_LEFT);
        drawTexture(matrices, x + 8, y, width - 16, height - 16, MIDDLE_CENTER);
        drawTexture(matrices, x + width - 8, y, 8, height - 16, MIDDLE_RIGHT);

        y = y + height - 16;
        drawTexture(matrices, x, y, 8, 8, BOTTOM_LEFT);
        drawTexture(matrices, x + 8, y, width - 16, 8, BOTTOM_CENTER);
        drawTexture(matrices, x + width - 8, y, 8, 8, BOTTOM_RIGHT);
    }

    private static void drawTexture(MatrixStack matrices, float x, float y, float width, float height, Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
        LCEDrawableHelper.drawTexture(matrices, x, y, 0, 0, width, height, 8, 8);
    }

    private static void renderGuiItemModel(TextureManager textureManager, ItemRenderer itemRenderer, ItemStack stack, float x, float y, float scale, BakedModel bakedModel) {
        itemRenderer.zOffset = bakedModel.hasDepth() ? itemRenderer.zOffset + 50.0f + (float) 0 : itemRenderer.zOffset + 50.0f;

        boolean sideLit = !bakedModel.isSideLit();
        textureManager.getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate(x, y, 100.0f + itemRenderer.zOffset);
        matrixStack.translate(8.0, 8.0, 0.0);
        matrixStack.scale(1.0f, -1.0f, 1.0f);
        matrixStack.scale(16.0f * scale, 16.0f * scale, 16.0f);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        if (sideLit) {
            DiffuseLighting.disableGuiDepthLighting();
        }
        itemRenderer.renderItem(stack, ModelTransformation.Mode.GUI, false, matrixStack2, immediate, LightmapTextureManager.MAX_LIGHT_COORDINATE, OverlayTexture.DEFAULT_UV, bakedModel);
        immediate.draw();
        RenderSystem.enableDepthTest();
        if (sideLit) {
            DiffuseLighting.enableGuiDepthLighting();
        }
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        itemRenderer.zOffset = bakedModel.hasDepth() ? itemRenderer.zOffset - 50.0f - (float) 0 : itemRenderer.zOffset - 50.0f;
    }
}
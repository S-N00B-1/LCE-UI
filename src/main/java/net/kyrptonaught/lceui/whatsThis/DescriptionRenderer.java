package net.kyrptonaught.lceui.whatsThis;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.crash.CrashException;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.joml.Matrix4f;

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

    public static void renderDescription(MinecraftClient client, DrawContext context, float delta) {
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

        render(client, context, x, 20, renderingDescription.getNameTranslation(), renderingDescription.getDescTranslation(), renderingDescription.getItemStack(), bakedModel, bakedModel != null);
    }

    private static void render(MinecraftClient client, DrawContext context, int x, int y, Text titleText, Text descriptionText, ItemStack stack, BakedModel model, boolean displayModel) {
        TextRenderer textRenderer = client.textRenderer;

        List<OrderedText> description = textRenderer.wrapLines(descriptionText, 223);

        float lineHeight = 33 + (description.size() * 8.0f);

        float totalHeight = lineHeight - 31.0f / 3.0f;
        if (displayModel) totalHeight = lineHeight + 32.0f / 3.0f;

        renderBackground(context, x, y, (500.0f / 3.0f), totalHeight);

        LCEDrawableHelper.drawTextWithShadow(context, textRenderer, titleText, x + 9, y + 26.0f / 3.0f, 2.0f/3.0f, 0xFFFFFF);
        for (int i = 0; i < description.size(); i++)
            LCEDrawableHelper.drawText(context, textRenderer, description.get(i), x + 9, y + 52.0f / 3.0f + (i * 8.0f), 2.0f/3.0f, 0xFFFFFF);

        if (displayModel) {
            float scale = 4.0f / 3.0f;
            float slotX = x + 218.0f / 3.0f;
            float slotY = 9.0f / 3.0f + lineHeight;

            LCEDrawableHelper.drawTexture(slot, context, slotX, slotY, 0, 0, 64.0f / 3.0f, 64.0f / 3.0f, 64.0f / 3.0f, 64.0f / 3.0f);
            renderGuiItemModel(context, client, stack, slotX + 8.0f / 3.0f, slotY + 8.0f / 3.0f, 0, scale * 54.0f / 64.0f, model);
        }
    }

    private static void renderBackground(DrawContext context, float x, float y, float width, float height) {
        drawTexture(context, x, y, 8, 8, TOP_LEFT);
        drawTexture(context, x + 8, y, width - 16, 8, TOP_CENTER);
        drawTexture(context, x + width - 8, y, 8, 8, TOP_RIGHT);

        y = y + 8;
        drawTexture(context, x, y, 8, height - 16, MIDDLE_LEFT);
        drawTexture(context, x + 8, y, width - 16, height - 16, MIDDLE_CENTER);
        drawTexture(context, x + width - 8, y, 8, height - 16, MIDDLE_RIGHT);

        y = y + height - 16;
        drawTexture(context, x, y, 8, 8, BOTTOM_LEFT);
        drawTexture(context, x + 8, y, width - 16, 8, BOTTOM_CENTER);
        drawTexture(context, x + width - 8, y, 8, 8, BOTTOM_RIGHT);
    }

    private static void drawTexture(DrawContext context, float x, float y, float width, float height, Identifier texture) {
        LCEDrawableHelper.drawTexture(texture, context, x, y, 0, 0, width, height, 8, 8);
    }

    private static void renderGuiItemModel(DrawContext context, MinecraftClient client, ItemStack stack, float x, float y, float z, float scale, BakedModel bakedModel) {
        MatrixStack matrices = context.getMatrices();
        if (!stack.isEmpty()) {
            matrices.push();
            matrices.translate(x + 8, y + 8, 150 + (bakedModel.hasDepth() ? z : 0));

            try {
                matrices.multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
                matrices.scale(16.0F * scale, 16.0F * scale, 16.0F * scale);
                boolean bl = !bakedModel.isSideLit();
                if (bl) {
                    DiffuseLighting.disableGuiDepthLighting();
                }

                client.getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, matrices, context.getVertexConsumers(), 15728880, OverlayTexture.DEFAULT_UV, bakedModel);
                context.draw();
                if (bl) {
                    DiffuseLighting.enableGuiDepthLighting();
                }
            } catch (Throwable var12) {
                CrashReport crashReport = CrashReport.create(var12, "Rendering item (LCE UI)");
                CrashReportSection crashReportSection = crashReport.addElement("Item being rendered");
                crashReportSection.add("Item Type", () -> String.valueOf(stack.getItem()));
                crashReportSection.add("Item Damage", () -> String.valueOf(stack.getDamage()));
                crashReportSection.add("Item NBT", () -> String.valueOf(stack.getNbt()));
                crashReportSection.add("Item Foil", () -> String.valueOf(stack.hasGlint()));
                throw new CrashException(crashReport);
            }

            matrices.pop();
        }
    }
}
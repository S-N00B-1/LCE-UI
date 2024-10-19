package net.kyrptonaught.lceui.titlescreen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.CubeMapRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class LegacyPanoramaRenderer extends CubeMapRenderer {
    private final Identifier texture;

    float time = 0;

    private static int width = 0;
    private static int height = 0;

    private static final MinecraftClient client = MinecraftClient.getInstance();

    public LegacyPanoramaRenderer(Identifier texture) {
        super(new Identifier(""));
        this.texture = texture;
    }

    //dummy to prevent drawing
    @Override
    public void draw(MinecraftClient client, float x, float y, float alpha) {
    }

    public static void resetPanDimensions() {
        width = 0;
        height = 0;
    }

    public void calcPanDimensions() {
        try {
            InputStream inputStream = client.getResourceManager().getResource(this.texture).get().getInputStream();
            width = NativeImage.read(inputStream).getWidth();

            inputStream = client.getResourceManager().getResource(this.texture).get().getInputStream();
            height = NativeImage.read(inputStream).getHeight();
        } catch (IOException e) {
            throw new RuntimeException(e); // Whoops, No assets!
        }
    }

    public void render(DrawContext context,MatrixStack matrices, float delta) {
        if (width == 0) { calcPanDimensions(); } // Width should NEVER be 0
        float speed = client.options.getPanoramaSpeed().getValue().floatValue();
        float deltaH = client.getWindow().getScaledHeight() * 1f / height;
        float loop = width * deltaH;
        time += (delta * speed);
        if (time >= loop) time -= loop;

        matrices.push();
        matrices.translate(-time, 0, 0);
        matrices.scale(deltaH, deltaH, 1);

        RenderSystem.setShaderTexture(0, texture);
        client.getTextureManager().getTexture(texture).setFilter(true,false); //Bi-linear Filtering, so we don't need to put a High Res Image in there.
        context.drawTexture(texture, 0, 0, 0, 0, width, height, width, height);
        context.drawTexture(texture, width, 0, 0, 0, width, height, width, height);
        context.drawTexture(texture, width * 2, 0, 0, 0, width, height, width, height);
        context.drawTexture(texture, width * 3, 0, 0, 0, width, height, width, height);
        matrices.pop();
    }

    public CompletableFuture<Void> loadTexturesAsync(TextureManager textureManager, Executor executor) {
        return CompletableFuture.allOf(textureManager.loadTextureAsync(texture, executor));
    }
}
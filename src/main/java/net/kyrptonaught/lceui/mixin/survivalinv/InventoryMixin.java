package net.kyrptonaught.lceui.mixin.survivalinv;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.kyrptonaught.lceui.LCEDrawableHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.mixin.ScreenAccessor;
import net.kyrptonaught.lceui.screens.LCESurvivalInventoryScreen;
import net.kyrptonaught.lceui.util.LCESounds;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryMixin extends AbstractInventoryScreen<LCESurvivalInventoryScreen.SurvivalScreenHandler> {
    @Shadow private float mouseX;

    @Shadow private float mouseY;

    public InventoryMixin(LCESurvivalInventoryScreen.SurvivalScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setWidthAndHeight(PlayerEntity player, CallbackInfo ci) {
        if (LCEUIMod.getConfig().survivalInventory) {
            this.backgroundHeight = 435/3;
            this.backgroundWidth = 431/3;
        }
    }

    @WrapOperation(method = "drawForeground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)I", ordinal = 0))
    private int drawForeground1(DrawContext instance, TextRenderer textRenderer, Text text, int x, int y, int color, boolean shadow, Operation<Integer> original) {
        if (LCEUIMod.getConfig().survivalInventory) {
            return LCEUIMod.getConfig().classicCrafting ? (int) LCEDrawableHelper.drawText(instance, textRenderer, text, 74, 10 + 2.0f/3.0f, 2.0f/3.0f, 0x373737) : 0;
        }
        return original.call(instance, textRenderer, text, x, y, color, shadow);
    }

    @Inject(method = "drawForeground", at = @At("TAIL"))
    private void drawForeground2(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        if (LCEUIMod.getConfig().survivalInventory) {
            LCEDrawableHelper.drawText(context, textRenderer, Text.translatable("container.inventory"), 9 + 1.0f/3.0f, 69 - 1.0f/3.0f, 2.0f/3.0f, 0x373737);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;renderBackground(Lnet/minecraft/client/gui/DrawContext;)V"))
    private void renderOnlyIfNotLCE1(InventoryScreen instance, DrawContext context, Operation<Void> original) {
        if (!LCEUIMod.getConfig().survivalInventory) {
            original.call(instance, context);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;render(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
    private void renderOnlyIfNotLCE2(RecipeBookWidget instance, DrawContext context, int mouseX, int mouseY, float delta, Operation<Void> original) {
        if (!LCEUIMod.getConfig().survivalInventory) {
            original.call(instance, context, mouseX, mouseY, delta);
        }
    }

    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;drawGhostSlots(Lnet/minecraft/client/gui/DrawContext;IIZF)V"))
    private void renderOnlyIfNotLCE3(RecipeBookWidget instance, DrawContext context, int x, int y, boolean notInventory, float delta, Operation<Void> original) {
        if (!LCEUIMod.getConfig().survivalInventory) {
            original.call(instance, context, x, y, notInventory, delta);
        }
    }
    @WrapOperation(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;drawTooltip(Lnet/minecraft/client/gui/DrawContext;IIII)V"))
    private void renderOnlyIfNotLCE4(RecipeBookWidget instance, DrawContext context, int x, int y, int mouseX, int mouseY, Operation<Void> original) {
        if (!LCEUIMod.getConfig().survivalInventory) {
            original.call(instance, context, x, y, mouseX, mouseY);
        }
    }

    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    private void drawLCEBackground(DrawContext instance, Identifier texture, int x, int y, int u, int v, int width, int height, Operation<Void> original) {
        MatrixStack matrices = instance.getMatrices();
        if (LCEUIMod.getConfig().survivalInventory) {
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            matrices.push();
            matrices.translate(this.x, this.y, 0);
            matrices.scale(1.0f/3.0f, 1.0f/3.0f, 1.0f);
            matrices.translate(-this.x, -this.y, 0);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            LCEDrawableHelper.drawTexture(new Identifier(LCEUIMod.MOD_ID, "textures/gui/survival_inventory.png"), instance, x, y, LCEUIMod.getConfig().classicCrafting ? 431 : 0, 0, 431, 435, 1024, 1024);
            matrices.pop();
        } else {
            original.call(instance, texture, x, y, u, v, width, height);
        }
    }

    @WrapOperation(method = "drawBackground", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIFFLnet/minecraft/entity/LivingEntity;)V"))
    private void drawEntity(DrawContext context, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity, Operation<Void> original) {
        if (LCEUIMod.getConfig().survivalInventory) {
            original.call(context, x - 51 + (LCEUIMod.getConfig().classicCrafting ? 48 : 81), y - 75 + 57, 22, (float)(x - 51 + (LCEUIMod.getConfig().classicCrafting ? 48 : 81)) - this.mouseX, (float)(y - 75 + 22) - this.mouseY, entity);
        } else {
            original.call(context, x, y, size, mouseX, mouseY, entity);
        }
    }

    @WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;mouseClicked(DDI)Z"))
    private boolean LCEhasNoRecipeBook1(RecipeBookWidget instance, double mouseX, double mouseY, int button, Operation<Boolean> original) {
        return !LCEUIMod.getConfig().survivalInventory && original.call(instance, mouseX, mouseY, button);
    }

    @WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;isOpen()Z"))
    private boolean LCEhasNoRecipeBook2(RecipeBookWidget instance, Operation<Boolean> original) {
        return !LCEUIMod.getConfig().survivalInventory && original.call(instance);
    }

    @WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;isClickOutsideBounds(DDIIIII)Z"))
    private boolean LCEhasNoRecipeBook3(RecipeBookWidget instance, double mouseX, double mouseY, int x, int y, int backgroundWidth, int backgroundHeight, int button, Operation<Boolean> original) {
        return !LCEUIMod.getConfig().survivalInventory || original.call(instance, mouseX, mouseY, x, y, backgroundWidth, backgroundHeight, button);
    }

    @WrapOperation(method = "*", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;slotClicked(Lnet/minecraft/screen/slot/Slot;)V"))
    private void LCEhasNoRecipeBook3(RecipeBookWidget instance, Slot slot, Operation<Void> original) {
        if (!LCEUIMod.getConfig().survivalInventory) {
            original.call(instance, slot);
        }
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;findLeftEdge(II)I"))
    private int LCEhasNoRecipeBook5(RecipeBookWidget instance, int width, int backgroundWidth, Operation<Integer> original) {
        if (LCEUIMod.getConfig().survivalInventory) return (width - backgroundWidth) / 2;
        return original.call(instance, width, backgroundWidth);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    private Element addDrawableChild(InventoryScreen instance, Element element, Operation<Element> original) {
        if (LCEUIMod.getConfig().survivalInventory) return element;
        return original.call(instance, element);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addSelectableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
    private Element addSelectableChild(InventoryScreen instance, Element element, Operation<Element> original) {
        if (LCEUIMod.getConfig().survivalInventory) return element;
        return original.call(instance, element);
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;setInitialFocus(Lnet/minecraft/client/gui/Element;)V"))
    private void setInitialFocus(InventoryScreen instance, Element element, Operation<Void> original) {
        if (!LCEUIMod.getConfig().survivalInventory) original.call(instance, element);
    }

    @Inject(method = "init", at = @At("HEAD"))
    private void hijackInit(CallbackInfo ci) {
        MinecraftClient client = ((ScreenAccessor) ((InventoryScreen) (Object) this)).client();
        if (!LCEUIMod.getConfig().compatibilityMode && LCEUIMod.getConfig().survivalInventory) {
            client.setScreen(new LCESurvivalInventoryScreen(client.player));
        } else if (client != null) {
            client.getSoundManager().play(PositionedSoundInstance.master(LCESounds.CLICK_STEREO, 1.0f, 3.0f));
        }
    }

    @WrapOperation(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasCreativeInventory()Z"))
    private boolean doNotCloseInventoryInCreative1(ClientPlayerInteractionManager instance, Operation<Boolean> original) {
        return !LCEUIMod.getConfig().creativeInventory && original.call(instance);
    }

    @WrapOperation(method = "handledScreenTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;hasCreativeInventory()Z"))
    private boolean doNotCloseInventoryInCreative2(ClientPlayerInteractionManager instance, Operation<Boolean> original) {
        return !LCEUIMod.getConfig().creativeInventory && original.call(instance);
    }
}

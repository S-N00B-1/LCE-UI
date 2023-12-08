package net.kyrptonaught.lceui.whatsThis;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.Command;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.kyrptonaught.lceui.LCEKeyBindings;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.tags.ClientTagHelper;
import net.kyrptonaught.lceui.whatsThis.resourceloaders.DescriptionResourceLoader;
import net.kyrptonaught.lceui.whatsThis.resourceloaders.ModelResourceLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.resource.ResourceType;

import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.registry.Registry;

import java.util.Collection;
import java.util.Map;

public class WhatsThisInit {
    public static DescriptionManager descriptionManager;

    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new DescriptionResourceLoader());
        ModelLoadingRegistry.INSTANCE.registerModelProvider(ModelResourceLoader::loadModels);

        descriptionManager = new DescriptionManager();

        ClientTickEvents.END_WORLD_TICK.register(world -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client != null && world != null && client.currentScreen == null) {
                HitResult hit = client.crosshairTarget;
                if (hit instanceof BlockHitResult blockHitResult) {
                    BlockState state = world.getBlockState(blockHitResult.getBlockPos());
                    if (!state.isAir()) {
                        DescriptionInstance descriptionInstance = DescriptionInstance.ofBlock(world, blockHitResult.getBlockPos(), state).bindToScreen(null);
                        DescriptionRenderer.setToRender(descriptionInstance, false);
                    }
                } else if (hit instanceof EntityHitResult entityHitResult) {
                    Entity entity = entityHitResult.getEntity();
                    if (entity.isAlive()) {
                        DescriptionInstance descriptionInstance = DescriptionInstance.ofEntity(entity).bindToScreen(null);
                        DescriptionRenderer.setToRender(descriptionInstance, false);
                    }
                }
            }
        });

        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {
            MinecraftClient client = MinecraftClient.getInstance();

            matrixStack.push();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            DescriptionRenderer.renderDescription(client, matrixStack, tickDelta);

            RenderSystem.disableBlend();
            matrixStack.pop();
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal(LCEUIMod.MOD_ID)
                        .then(ClientCommandManager.literal("descriptions")
                                .then(ClientCommandManager.literal("clear")
                                        .executes(context -> {
                                            int count = descriptionManager.viewedDescriptions.size();
                                            descriptionManager.viewedDescriptions.clear();
                                            if (count > 0) {
                                                context.getSource().sendFeedback(Text.translatable("command.lceui.descriptions.clear.all." + (count == 1 ? "single" : (count == 2 ? "dual" : "plural")), count));
                                                return Command.SINGLE_SUCCESS;
                                            }
                                            context.getSource().sendFeedback(Text.translatable("command.lceui.descriptions.notfound"));
                                            return 0;
                                        })
                                        .then(ClientCommandManager.argument("description", ViewableDescriptionArgumentType.viewedDescriptionArgumentType())
                                                .executes(context -> {
                                                    ViewableDescription viewableDescription = ViewableDescriptionArgumentType.getViewableDescriptionArgumentType(context, "description");
                                                    boolean removed = descriptionManager.viewedDescriptions.remove(viewableDescription.toString());
                                                    if (removed) {
                                                        context.getSource().sendFeedback(Text.translatable("command.lceui.descriptions.cleared", viewableDescription));
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    context.getSource().sendFeedback(Text.translatable("command.lceui.descriptions.notfound"));
                                                    return 0;
                                                })))
                                .then(ClientCommandManager.literal("grant")
                                        .then(ClientCommandManager.literal("all")
                                                .executes(context -> {
                                                    int entryCount = 0;
                                                    Map<Identifier, Collection<Identifier>> descriptionTags = ClientTagHelper.getTagsInPath(new Identifier(LCEUIMod.MOD_ID, "descriptions"));
                                                    for (Identifier tag : descriptionTags.keySet()) {
                                                        if (descriptionManager.viewedDescriptions.add("#" + tag.toString()))
                                                            ++entryCount;
                                                    }
                                                    for (Block block : Registry.BLOCK) {
                                                        String description = Registry.BLOCK.getId(block).getNamespace() + ":block/" + Registry.BLOCK.getId(block).getPath();
                                                        if (DescriptionManager.findTagForID(Registry.BLOCK.getId(block)).isEmpty()) {
                                                            if (descriptionManager.viewedDescriptions.add(description))
                                                                ++entryCount;
                                                        }
                                                    }
                                                    for (Item item : Registry.ITEM) {
                                                        String description = Registry.ITEM.getId(item).getNamespace() + ":item/" + Registry.ITEM.getId(item).getPath();
                                                        if (descriptionManager.viewedDescriptions.add(description))
                                                            ++entryCount;
                                                    }
                                                    for (EntityType<?> entityType : Registry.ENTITY_TYPE) {
                                                        String description = Registry.ENTITY_TYPE.getId(entityType).getNamespace() + ":entity/" + Registry.ENTITY_TYPE.getId(entityType).getPath();
                                                        if (descriptionManager.viewedDescriptions.add(description))
                                                            ++entryCount;
                                                    }
                                                    if (entryCount > 0) {
                                                        context.getSource().sendFeedback(Text.translatable("command.lceui.descriptions.grant.all." + (entryCount == 1 ? "single" : (entryCount == 2 ? "dual" : "plural")), entryCount));
                                                        return Command.SINGLE_SUCCESS;
                                                    }
                                                    context.getSource().sendFeedback(Text.translatable("command.lceui.descriptions.notfound"));
                                                    return 0;
                                                }))
                                        .then(ClientCommandManager.literal("only")
                                                .then(ClientCommandManager.argument("description", ViewableDescriptionArgumentType.viewableDescriptionArgumentType())
                                                        .executes(context -> {
                                                            ViewableDescription viewableDescription = ViewableDescriptionArgumentType.getViewableDescriptionArgumentType(context, "description");
                                                            descriptionManager.viewedDescriptions.add(viewableDescription.toString());
                                                            context.getSource().sendFeedback(Text.translatable("command.lceui.descriptions.grant.only", viewableDescription.toString()));
                                                            return Command.SINGLE_SUCCESS;
                                                        })))))));
    }

    public static boolean isKeybindPressed(int pressedKeyCode, InputUtil.Type type) {
        InputUtil.Key key = KeyBindingHelper.getBoundKeyOf(LCEKeyBindings.showDescription);
        if (key.getCategory() != type)
            return false;

        return key.getCode() == pressedKeyCode;
    }

    public static Identifier getCleanIdentifier(Identifier identifier) {
        return new Identifier(identifier.getNamespace(), identifier.getPath()
                .replace("models/", "")
                .replace(".json", "")
                .replace("block/", "")
                .replace("item/", "")
                .replace("entity/", ""));

    }
}
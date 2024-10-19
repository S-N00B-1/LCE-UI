package net.kyrptonaught.lceui.whatsThis;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Objects;
import java.util.Optional;

public class DescriptionInstance {
    private ItemStack displayStack;
    private ItemDescription displayDescription;
    private BakedModel displayModel;

    private Screen boundToScreen;
    private float openTicks = 150.0f;


    public static Optional<DescriptionInstance> ofItem(ItemStack stack) {
        Optional<ItemDescription> optional = WhatsThisInit.descriptionManager.getDescriptionForItem(stack);
        return optional.map(itemDescription -> ofItem(stack, itemDescription));
    }

    public static DescriptionInstance ofItem(ItemStack stack, ItemDescription itemDescription) {
        DescriptionInstance instance = new DescriptionInstance();
        instance.displayStack = stack;
        instance.displayDescription = itemDescription;
        return instance;
    }

    public static Optional<DescriptionInstance> ofEntity(Entity entity) {
        Optional<ItemDescription> optional = WhatsThisInit.descriptionManager.getDescriptionForEntity(entity.getType());
        return optional.map(itemDescription -> ofEntity(entity, itemDescription));
    }

    public static DescriptionInstance ofEntity(Entity entity, ItemDescription itemDescription) {
        DescriptionInstance instance = new DescriptionInstance();
        instance.displayStack = entity.getPickBlockStack();
        instance.displayDescription = itemDescription;
        return instance;
    }

    public static Optional<DescriptionInstance> ofBlock(World world, BlockPos pos, BlockState blockState) {
        Optional<ItemDescription> optional = WhatsThisInit.descriptionManager.getDescriptionForBlock(blockState);
        if (optional.isEmpty()) return Optional.empty();
        return Optional.of(ofBlock(world, pos, blockState, WhatsThisInit.descriptionManager.getDescriptionForBlock(blockState).get()));
    }

    public static DescriptionInstance ofBlock(World world, BlockPos pos, BlockState blockState, ItemDescription itemDescription) {
        if (blockState == null || blockState.isAir() || itemDescription == null) return null;
        ItemStack itemStack = blockState.getBlock().asItem().getDefaultStack();
        if (itemStack.isEmpty())
            itemStack = blockState.getBlock().getPickStack(world, pos, blockState);

        return ofItem(itemStack, itemDescription);
    }

    public DescriptionInstance bindToScreen(Screen screen) {
        this.boundToScreen = screen;
        return this;
    }

    public void tickOpen(float delta) {
        openTicks -= delta;
    }

    public boolean shouldHide(MinecraftClient client) {
        return false;
    }

    public boolean shouldClose(MinecraftClient client) {
        if (openTicks <= 0) return true;

        return !Objects.equals(boundToScreen, client.currentScreen);
    }

    public String getGroupKey() {
        return displayDescription.group;
    }

    public MutableText getNameTranslation() {
        return displayDescription.text.name;
    }

    public MutableText getDescTranslation() {
        return displayDescription.text.description;
    }

    public ItemStack getItemStack() {
        return displayStack;
    }

    public BakedModel getDisplayModel(MinecraftClient client) {
        if (displayModel == null && displayDescription.displaysicon && displayStack != null) {
            if (displayDescription.isFieldBlank(displayDescription.model))
                displayModel = client.getItemRenderer().getModel(displayStack, null, client.player, 0);

            else {
                ModelIdentifier modelID = new ModelIdentifier(WhatsThisInit.getCleanIdentifier(new Identifier(displayDescription.model)), "inventory");
                displayModel = client.getBakedModelManager().getModel(modelID);
            }
        }
        return displayModel;
    }
}

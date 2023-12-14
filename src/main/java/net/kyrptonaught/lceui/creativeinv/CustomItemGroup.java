package net.kyrptonaught.lceui.creativeinv;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.kyrptonaught.lceui.creativeinv.resourceloaders.ItemGroupResourceLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class CustomItemGroup {
    public static final List<CustomItemGroup> ITEM_GROUPS = new ArrayList<>();

    private final Identifier id;
    private final Identifier resourceLocation;
    private final CopyOnWriteArrayList<ItemStack> itemStackList;
    private int index;

    private CustomItemGroup(Identifier id, Identifier resourceLocation, CopyOnWriteArrayList<ItemStack> itemStackList) {
        this.id = id;
        this.resourceLocation = resourceLocation;
        this.itemStackList = itemStackList;
    }

    public static CustomItemGroup createAndRegister(Identifier id, CopyOnWriteArrayList<ItemStack> itemStackList) {
        CustomItemGroup group = new CustomItemGroup(id, new Identifier(id.getNamespace(), "textures/gui/creativeinv/tabs/" + id.getPath() + ".png"), itemStackList);
        group.index = registerGroup(group);
        return group;
    }

    public static CustomItemGroup createAndRegister(Identifier id, Collection<ItemStack> itemStackList) {
        return createAndRegister(id, new CopyOnWriteArrayList<>(itemStackList));
    }

    public static int registerGroup(CustomItemGroup group) {
        ITEM_GROUPS.add(group);
        return ITEM_GROUPS.indexOf(group);
    }

    public Identifier getId() {
        return this.id;
    }

    public Identifier getResourceLocation() {
        return this.resourceLocation;
    }

    public CopyOnWriteArrayList<ItemStack> getItemStackList() {
        return this.itemStackList;
    }

    public int getIndex() {
        return this.index;
    }

    public Text getName() {
        if (this.id.getPath().startsWith("mod.")) {
            Optional<ModContainer> optional = FabricLoader.getInstance().getModContainer(this.id.getPath().substring(4));
            return optional.map(modContainer -> Text.literal(modContainer.getMetadata().getName())).orElseGet(() -> Text.literal(this.id.getPath()));
        }
        return Text.translatable(this.id.getNamespace() + ".itemGroup." + this.id.getPath());
    }

    public void appendStacks(DefaultedList<ItemStack> list) {
        list.addAll(this.itemStackList);
    }

    public static Optional<CustomItemGroup> getById(Identifier id) {
        for (CustomItemGroup group : ITEM_GROUPS) {
            if (group.getId().getNamespace().equals(id.getNamespace()) && group.getId().getPath().equals(id.getPath())) {
                return Optional.of(group);
            }
        }
        return Optional.empty();
    }

    public static boolean contain(Item item) {
        for (CustomItemGroup itemGroup : CustomItemGroup.ITEM_GROUPS) {
            for (ItemStack itemStack : itemGroup.getItemStackList()) {
                if (new ItemStack(item).isOf(itemStack.getItem())) return true; // itemStack.isOf(item) doesn't work for some reason???
            }
        }
        return false;
    }

    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ItemGroupResourceLoader());
    }
}

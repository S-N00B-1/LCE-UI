package net.kyrptonaught.lceui.creativeinv;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.kyrptonaught.lceui.creativeinv.resourceloaders.ItemGroupResourceLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CustomItemGroup {
    public static final List<CustomItemGroup> ITEM_GROUPS = new ArrayList<>();
//
//    public static final CustomItemGroup BUILDING_BLOCKS = createAndRegister(new Identifier(LCEUIMod.MOD_ID, "building_blocks"), Arrays.asList(new ItemStack(Blocks.STONE), new ItemStack(Blocks.DIRT)));
//    public static final CustomItemGroup DECORATIONS = createAndRegister(new Identifier(LCEUIMod.MOD_ID, "decorations"), Arrays.asList(new ItemStack(Blocks.DANDELION), new ItemStack(Blocks.POPPY)));
//    public static final CustomItemGroup REDSTONE_AND_TRANSPORTATION = createAndRegister(new Identifier(LCEUIMod.MOD_ID, "redstone_and_transportation"), Arrays.asList(new ItemStack(Items.MINECART), new ItemStack(Items.CHEST_MINECART)));
//    public static final CustomItemGroup MATERIALS = createAndRegister(new Identifier(LCEUIMod.MOD_ID, "materials"), Arrays.asList(new ItemStack(Items.DIAMOND), new ItemStack(Items.EMERALD)));
//    public static final CustomItemGroup FOOD = createAndRegister(new Identifier(LCEUIMod.MOD_ID, "food"), Arrays.asList(new ItemStack(Items.BREAD), new ItemStack(Items.GOLDEN_CARROT)));
//    public static final CustomItemGroup TOOLS_AND_WEAPONS = createAndRegister(new Identifier(LCEUIMod.MOD_ID, "tools_and_weapons"), Arrays.asList(new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.DIAMOND_SHOVEL)));
//    public static final CustomItemGroup POTIONS = createAndRegister(new Identifier(LCEUIMod.MOD_ID, "potions"), Arrays.asList(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
//    public static final CustomItemGroup MISCELLANEOUS = createAndRegister(new Identifier(LCEUIMod.MOD_ID, "miscellaneous"), Arrays.asList(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.LAVA_BUCKET)));

    private final Identifier id;
    private final Identifier resourceLocation;
    private final List<ItemStack> itemStackList;
    private int index;

    private CustomItemGroup(Identifier id, Identifier resourceLocation, List<ItemStack> itemStackList) {
        this.id = id;
        this.resourceLocation = resourceLocation;
        this.itemStackList = itemStackList;
    }

    public static CustomItemGroup createAndRegister(Identifier id, List<ItemStack> itemStackList) {
        CustomItemGroup group = new CustomItemGroup(id, new Identifier(id.getNamespace(), "textures/gui/creativeinv/tabs/" + id.getPath() + ".png"), itemStackList);
        group.index = registerGroup(group);
        return group;
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

    public List<ItemStack> getItemStackList() {
        return this.itemStackList;
    }

    public int getIndex() {
        return this.index;
    }

    public Text getName() {
        return Text.translatable(this.id.getNamespace() + ".itemGroup." + this.id.getPath());
    }

    public void appendStacks(DefaultedList<ItemStack> list) {
        list.addAll(this.itemStackList);
    }

    @Nullable
    public static CustomItemGroup getById(Identifier id) {
        for (CustomItemGroup group : ITEM_GROUPS) {
            if (group.resourceLocation == id) {
                return group;
            }
        }
        return null;
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

package net.kyrptonaught.lceui.creativeinv;

import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.kyrptonaught.lceui.LCEUIMod;
import net.kyrptonaught.lceui.whatsThis.resourceloaders.ItemGroupResourceLoader;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomItemGroup {
    public static final List<CustomItemGroup> LCE_ITEM_GROUPS = new ArrayList<>();

    public static final CustomItemGroup BUILDING_BLOCKS = new CustomItemGroup(new Identifier(LCEUIMod.MOD_ID, "building_blocks"), Arrays.asList(new ItemStack(Blocks.STONE), new ItemStack(Blocks.DIRT)));
    public static final CustomItemGroup DECORATIONS = new CustomItemGroup(new Identifier(LCEUIMod.MOD_ID, "decorations"), Arrays.asList(new ItemStack(Blocks.DANDELION), new ItemStack(Blocks.POPPY)));
    public static final CustomItemGroup REDSTONE_AND_TRANSPORTATION = new CustomItemGroup(new Identifier(LCEUIMod.MOD_ID, "redstone_and_transportation"), Arrays.asList(new ItemStack(Items.MINECART), new ItemStack(Items.CHEST_MINECART)));
    public static final CustomItemGroup MATERIALS = new CustomItemGroup(new Identifier(LCEUIMod.MOD_ID, "materials"), Arrays.asList(new ItemStack(Items.DIAMOND), new ItemStack(Items.EMERALD)));
    public static final CustomItemGroup FOOD = new CustomItemGroup(new Identifier(LCEUIMod.MOD_ID, "food"), Arrays.asList(new ItemStack(Items.BREAD), new ItemStack(Items.GOLDEN_CARROT)));
    public static final CustomItemGroup TOOLS_AND_WEAPONS = new CustomItemGroup(new Identifier(LCEUIMod.MOD_ID, "tools_and_weapons"), Arrays.asList(new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.DIAMOND_SHOVEL)));
    public static final CustomItemGroup POTIONS = new CustomItemGroup(new Identifier(LCEUIMod.MOD_ID, "potions"), Arrays.asList(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
    public static final CustomItemGroup MISCELLANEOUS = new CustomItemGroup(new Identifier(LCEUIMod.MOD_ID, "miscellaneous"), Arrays.asList(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.LAVA_BUCKET)));

    private Identifier id;
    private Identifier resourceLocation;
    private List<ItemStack> itemStackList;
    private int index;

    public CustomItemGroup(Identifier id, Identifier resourceLocation, List<ItemStack> itemStackList) {
        this.id = id;
        this.resourceLocation = resourceLocation;
        this.index = LCE_ITEM_GROUPS.size();
        this.itemStackList = itemStackList;

        LCE_ITEM_GROUPS.add(this);
    }

    public CustomItemGroup(Identifier id, List<ItemStack> itemStackList) {
        this(id, new Identifier(id.getNamespace(), "textures/gui/creativeinv/tabs/" + id.getPath() + ".png"), itemStackList);
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

    public static void init() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new ItemGroupResourceLoader());
        System.out.println("Registering item groups");
    }
}

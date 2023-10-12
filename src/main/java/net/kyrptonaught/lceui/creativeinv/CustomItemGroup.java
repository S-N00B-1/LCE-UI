package net.kyrptonaught.lceui.creativeinv;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CustomItemGroup {
    public static List<CustomItemGroup> LCE_ITEM_GROUPS = new ArrayList<>();

    public static final CustomItemGroup BUILDING_BLOCKS = new CustomItemGroup("building_blocks", Arrays.asList(new ItemStack(Blocks.STONE), new ItemStack(Blocks.DIRT)));
    public static final CustomItemGroup DECORATIONS = new CustomItemGroup("decorations", Arrays.asList(new ItemStack(Blocks.DANDELION), new ItemStack(Blocks.POPPY)));
    public static final CustomItemGroup REDSTONE_AND_TRANSPORTATION = new CustomItemGroup("redstone_and_transportation", Arrays.asList(new ItemStack(Items.MINECART), new ItemStack(Items.CHEST_MINECART)));
    public static final CustomItemGroup MATERIALS = new CustomItemGroup("materials", Arrays.asList(new ItemStack(Items.DIAMOND), new ItemStack(Items.EMERALD)));
    public static final CustomItemGroup FOOD = new CustomItemGroup("food", Arrays.asList(new ItemStack(Items.BREAD), new ItemStack(Items.GOLDEN_CARROT)));
    public static final CustomItemGroup TOOLS_AND_WEAPONS = new CustomItemGroup("tools_and_weapons", Arrays.asList(new ItemStack(Items.DIAMOND_SWORD), new ItemStack(Items.DIAMOND_SHOVEL)));
    public static final CustomItemGroup POTIONS = new CustomItemGroup("potions", Arrays.asList(PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.FIRE_RESISTANCE), PotionUtil.setPotion(new ItemStack(Items.POTION), Potions.WATER)));
    public static final CustomItemGroup MISCELLANEOUS = new CustomItemGroup("miscellaneous", Arrays.asList(new ItemStack(Items.WATER_BUCKET), new ItemStack(Items.LAVA_BUCKET)));

    private String id;
    private Identifier resourceLocation;
    private List<ItemStack> itemStackList;
    private int index;

    public CustomItemGroup(String id, Identifier resourceLocation, List<ItemStack> itemStackList) {
        this.id = id;
        this.resourceLocation = Objects.requireNonNullElseGet(resourceLocation,
                () -> new Identifier(LCEUIMod.MOD_ID, "textures/gui/creativeinv/tabs/" + id + ".png"));
        this.index = LCE_ITEM_GROUPS.size();
        this.itemStackList = itemStackList;

        LCE_ITEM_GROUPS.add(this);
    }

    public CustomItemGroup(String id, List<ItemStack> itemStackList) {
        this(id, new Identifier(LCEUIMod.MOD_ID, "textures/gui/creativeinv/tabs/" + id + ".png"), itemStackList);
    }

    public String getId() {
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
        return Text.translatable("lceui.itemGroup." + this.id);
    }

    public void appendStacks(DefaultedList<ItemStack> list) {
        list.addAll(this.itemStackList);
    }

    public static void registerItemGroups() {
        System.out.println("Registering item groups");
    }
}

package net.kyrptonaught.lceui.creativeinv;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public record CustomItemGroup(int index, String id, Identifier resourceLocation) {
    public static CustomItemGroup[] LCE_ITEM_GROUPS = new CustomItemGroup[8];

    public static final CustomItemGroup BUILDING_BLOCKS = new CustomItemGroup(0, "building_blocks", null);
    public static final CustomItemGroup DECORATIONS = new CustomItemGroup(1, "decorations", null);
    public static final CustomItemGroup REDSTONE_AND_TRANSPORTATION = new CustomItemGroup(2,
            "redstone_and_transportation", null);
    public static final CustomItemGroup MATERIALS = new CustomItemGroup(3, "materials", null);
    public static final CustomItemGroup FOOD = new CustomItemGroup(4, "food", null);
    public static final CustomItemGroup TOOLS_AND_WEAPONS = new CustomItemGroup(5, "tools_and_weapons", null);
    public static final CustomItemGroup POTIONS = new CustomItemGroup(6, "potions", null);
    public static final CustomItemGroup MISCELLANEOUS = new CustomItemGroup(7, "miscellaneous", null);

    public CustomItemGroup(int index, String id, @Nullable Identifier resourceLocation) {
        this.index = index;
        this.id = id;
        this.resourceLocation = Objects.requireNonNullElseGet(resourceLocation,
                () -> new Identifier(LCEUIMod.MOD_ID, "textures/gui/creativeinv/tabs/" + id + ".png"));
        LCE_ITEM_GROUPS[index] = this;
    }

    public static void registerItemGroups() {
        System.out.println("registering item groups");
    }
}

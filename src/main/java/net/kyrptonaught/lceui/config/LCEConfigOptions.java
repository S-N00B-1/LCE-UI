package net.kyrptonaught.lceui.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

public class LCEConfigOptions implements AbstractConfigFile {
    @Comment("Makes the text shadow render 1/3 of a pixel down and to the right rather than a full pixel")
    public boolean closerTextShadows = true;
    @Comment("Makes renamed items gold instead of italic")
    public boolean renamedItemsHaveGoldName = true;
    @Comment("Makes items smaller even outside of scalable slots")
    public boolean smallerItemsOutsideOfScalableSlots = true;
    @Comment("Enables the LCE Creative mode inventory")
    public boolean creativeInventory = true;
    @Comment("Enables the LCE Survival mode inventory")
    public boolean survivalInventory = true;
    @Comment("Enables classic crafting (normal Java crafting)")
    public boolean classicCrafting = false;
    @Comment("Enables the LCE generic container inventory")
    public boolean containerInventory = true;
    @Comment("Enables the LCE 3x3 generic container inventory")
    public boolean container3x3Inventory = true;
    @Comment("Enables the LCE hopper inventory")
    public boolean hopperInventory = true;
    @Comment("Enables the LCE shulker box inventory")
    public boolean shulkerBoxInventory = true;
    @Comment("Makes the chat's width the whole screen")
    public boolean chatWidth = true;
    @Comment("Makes the chat's Y position the same as LCE (almost; it's actually 1 pixel off)")
    public boolean chatYPos = true;
    @Comment("Recolors the chat to LCE's color")
    public boolean recolorChat = true;
    @Comment("Makes the chat's text 2/3 of its normal size")
    public boolean rescaleChatText = true;
    @Comment("Turns the sign UI into LCE's sign UI")
    public boolean sign = true;
}

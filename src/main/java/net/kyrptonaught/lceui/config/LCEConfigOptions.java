package net.kyrptonaught.lceui.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

public class LCEConfigOptions implements AbstractConfigFile {
    @Comment("Makes the text shadow render 1/3 of a pixel down and to the right rather than a full pixel")
    public boolean closerTextShadows = true;
    @Comment("Enables the LCE Creative mode inventory")
    public boolean creativeInventory = true;
    @Comment("Makes the chat's width the whole screen")
    public boolean chatWidth = true;
//    @Comment("Makes the chat's height the exact same as LCE (35px, in Java it's either 36px or 34px with no in-between")
//    public boolean chatHeight = true;
    @Comment("Recolors the chat to LCE's color")
    public boolean recolorChat = true;
    @Comment("Makes the chat's text 2/3 of its normal size")
    public boolean rescaleChatText = true;
    @Comment("Defines the hotbar scale")
    public int hotbarScale = 2; // TODO: make this actually do something
}

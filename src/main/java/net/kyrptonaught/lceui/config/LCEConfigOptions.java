package net.kyrptonaught.lceui.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

public class LCEConfigOptions implements AbstractConfigFile {
    @Comment("Makes the text shadow render 1/3 of a pixel down and to the right rather than a full pixel")
    public boolean closerTextShadows = true;
}

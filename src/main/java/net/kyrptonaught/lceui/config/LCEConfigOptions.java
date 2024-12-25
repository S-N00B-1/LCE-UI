package net.kyrptonaught.lceui.config;

import blue.endless.jankson.Comment;
import net.kyrptonaught.kyrptconfig.config.AbstractConfigFile;

public class LCEConfigOptions implements AbstractConfigFile {
    @Comment("Makes the text shadow render 1/3 of a pixel down and to the right rather than a full pixel")
    public boolean closerTextShadows = true;
    @Comment("Enables the 'What's This' Tips when you see a new thing.")
    public boolean whatsThis = true;
    @Comment("Makes the tooltip rendering similar to LCE.")
    public boolean tooltips = true;


    @Comment("Makes the chat's width the whole screen")
    public boolean chatWidth = true;
    @Comment("Makes the chat's Y position the same as LCE (almost; it's actually 1 pixel off)")
    public boolean chatYPos = true;
    @Comment("Recolors the chat to LCE's color")
    public boolean recolorChat = true;
    @Comment("Makes the chat's text 2/3 of its normal size")
    public boolean rescaleChatText = false;

    @Comment("Hides the Dark Overlay that appears in UI's")
    public boolean removeTransparentBG = true;
    @Comment("Hides the In Game HUD when a diffrent UI is displayed")
    public boolean hideHudWhenInUI = true;

    @Comment("Stops the crosshair from inverting colours similar to LCE")
    public boolean lceCrosshair = true;
    @Comment("Opacity of the crosshair if LCE Crosshair is enabled")
    public float lceCrosshairOpacity = 0.8f;

    @Comment("Enables fade out of hotbar when idle.")
    public boolean fadeOut = true;
    @Comment("Amount of ticks of being idle before fadeout starts")
    public int idleInputTimeout = 60;
    @Comment("The minimum opacity the hotbar can fade to.")
    public float minFadeValue = 0.2f;

    @Comment("Makes the panorama a 2D scrolling texture, like LCE.")
    public boolean lcePan = true;
    @Comment("Renders the panorama in place of the normal options background")
    public boolean renderPanoramaEverywhere = false;
    @Comment("Prioritises L4J Panorama textures over LCEUI's")
    public boolean l4jPanSupport = true; // There's always a bigger fish
}

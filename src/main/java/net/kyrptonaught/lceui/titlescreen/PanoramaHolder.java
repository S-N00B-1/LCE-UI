package net.kyrptonaught.lceui.titlescreen;

import net.kyrptonaught.lceui.LCEUIMod;
import net.minecraft.util.Identifier;

// We only need on panorama renderer for both the title screen & options if it's enabled
// That way there's no weird jumps in panorama position as you change it.
public class PanoramaHolder {
    public static final Identifier lceuiPan = new Identifier(LCEUIMod.MOD_ID, "textures/gui/title/background/69/day/69_day_large.png");
    public static final LegacyPanoramaRenderer PANORAMA_CUBE_MAP = new LegacyPanoramaRenderer(lceuiPan);

    // Legacy4j, being a much more popular mod, would have more resource packs than we do, so prioritise their assets if present.
    public static final Identifier l4jPan = new Identifier("legacy", "textures/gui/title/panorama_day.png");
    public static final LegacyPanoramaRenderer L4J_SUPPORT_PANORAMA_CUBE_MAP = new LegacyPanoramaRenderer(l4jPan);
}

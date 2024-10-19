package net.kyrptonaught.lceui.titlescreen;

import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

public class PanResourceReloader implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        // Unsure what to set this to, considering that if l4j
        // panorama support is enabled
        // we need to check in 2 locations for a resource change
        return null;
    }

    @Override
    public void reload(ResourceManager manager) {
        // reset Pan Dimensions Whenever the resource pack is changed.
        LegacyPanoramaRenderer.resetPanDimensions();
    }

}

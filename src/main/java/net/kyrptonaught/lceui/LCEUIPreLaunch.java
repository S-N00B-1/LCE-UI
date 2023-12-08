package net.kyrptonaught.lceui;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.kyrptonaught.lceui.compat.LCEMidnightControlsCompat;

public class LCEUIPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        if (FabricLoader.getInstance().isModLoaded("midnightcontrols") && FabricLoader.getInstance().getEnvironmentType().equals(EnvType.CLIENT)) LCEMidnightControlsCompat.register();
    }
}

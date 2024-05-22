package net.hydra.jojomod;

import net.minecraftforge.fml.common.Mod;

@Mod(Roundabout.MOD_ID)
public class RoundaboutModForge {

    public RoundaboutModForge() {
        Roundabout.LOGGER.info("Hello Forge world!");
        Roundabout.init();
    }

}
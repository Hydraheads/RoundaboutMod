package net.hydra.jojomod.platform.services;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.LivingEntity;

public class FabricPlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {
        return "Fabric";
    }
    @Override
    public boolean getBoss(LivingEntity LE) {
        return LE.getType().is(ConventionalEntityTypeTags.BOSSES);
    }
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }


}
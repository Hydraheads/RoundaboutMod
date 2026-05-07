package net.hydra.jojomod.platform.services;

import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBlockTags;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.fabricmc.fabric.impl.datagen.FabricTagBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

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
    public TagKey<Block> getOreTag() {
        return ConventionalBlockTags.ORES;
    }
    @Override
    public boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
    public boolean getFluidTagPushCode(Entity ent, TagKey<Fluid> $$0, double $$1){
        return true;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }


}
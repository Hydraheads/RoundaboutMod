package net.hydra.jojomod.platform.services;

import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;


public class ForgePlatformHelper implements IPlatformHelper {

    public String getPlatformName() {
        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean getBoss(LivingEntity LE) {
        return LE.getType().is(Tags.EntityTypes.BOSSES);
    }

    @Override
    public TagKey<Block> getOreTag() {
        return Tags.Blocks.ORES;
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return !FMLLoader.isProduction();
    }

    public boolean getFluidTagPushCode(Entity ent, TagKey<Fluid> $$0, double $$1){
        ent.updateFluidHeightAndDoFluidPushing();
        if($$0 == FluidTags.WATER) return ent.isInFluidType(net.minecraftforge.common.ForgeMod.WATER_TYPE.get());
        else if ($$0 == FluidTags.LAVA) return ent.isInFluidType(net.minecraftforge.common.ForgeMod.LAVA_TYPE.get());
        else return false;
    }

}
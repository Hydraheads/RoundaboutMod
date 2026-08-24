package net.hydra.jojomod.platform.services;

import net.hydra.jojomod.Roundabout;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;


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

    public ResourceLocation soundId(SoundEvent event){
        Roundabout.LOGGER.info("fgfg");
        return ForgeRegistries.SOUND_EVENTS.getKey(event);
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
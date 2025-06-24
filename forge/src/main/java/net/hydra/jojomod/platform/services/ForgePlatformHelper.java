package net.hydra.jojomod.platform.services;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
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

}
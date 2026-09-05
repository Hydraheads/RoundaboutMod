package net.hydra.jojomod.entity.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;

public class AvoidLightNodeEvaluator extends WalkNodeEvaluator {


    @Override
    public BlockPathTypes getBlockPathType(BlockGetter level, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);

        int light = mob.level().getBrightness(LightLayer.BLOCK, pos);
        int light2 = mob.level().getBrightness(LightLayer.SKY, pos);
        int lightSelf = mob.level().getBrightness(LightLayer.BLOCK, new BlockPos(mob.getBlockX(), mob.getBlockY(), mob.getBlockZ()));
        int lightSelf2 = mob.level().getBrightness(LightLayer.BLOCK, new BlockPos(mob.getBlockX(), mob.getBlockY(), mob.getBlockZ()));
        long timeOfDay = mob.level().getDayTime() % 24000L;
        boolean isWeatherNotClear = mob.level().isRaining() || mob.level().isThundering();
        boolean isDay = timeOfDay < 12555L || timeOfDay > 23470;

        if (light > 11 && lightSelf < light || isDay && light2 > 14 && lightSelf2 < light2 && !isWeatherNotClear) {
                return BlockPathTypes.BLOCKED;
        }
        return super.getBlockPathType(level, x, y, z);
    }
}

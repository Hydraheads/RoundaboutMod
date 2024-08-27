package net.hydra.jojomod.block;

import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EnderBloodBlock extends BloodBlock{
    /**Blood that teleports like a chorus fruit when walked on.*/
    public EnderBloodBlock(Properties $$0) {
        super($$0);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if ($$3 instanceof LivingEntity && !MainUtil.hasEnderBlood($$3)){
            MainUtil.randomChorusTeleport((LivingEntity) $$3);
            $$1.removeBlock($$2, false);
        }
    }

    @Override
    public void tick(BlockState $$0, ServerLevel $$1, BlockPos $$2, RandomSource $$3) {
        super.tick($$0,$$1,$$2,$$3);
        if ($$1.isRaining() && this.isNearRain($$1, $$2)) {
            $$1.removeBlock($$2, false);
        }
    }


    protected boolean isNearRain(Level $$0, BlockPos $$1) {
        return $$0.isRainingAt($$1)
                || $$0.isRainingAt($$1.west())
                || $$0.isRainingAt($$1.east())
                || $$0.isRainingAt($$1.north())
                || $$0.isRainingAt($$1.south());
    }
}

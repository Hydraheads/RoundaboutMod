package net.hydra.jojomod.block;

import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.util.HeatUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ColdAirBlock extends RoundaboutAttackBlock {
    public ColdAirBlock(Properties $$0) {
        super($$0);
    }


    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if ($$3 instanceof LivingEntity LE && !LE.isInvulnerable()) {
            if (FateTypes.isVampire(LE)){
                $$3.makeStuckInBlock($$0, new Vec3((double)0.3F, (double)0.3F, (double)0.3F));
            } else {
                $$3.makeStuckInBlock($$0, new Vec3((double)0.8F, (double)0.8F, (double)0.8F));
            }
            if (!$$1.isClientSide) {
                HeatUtil.addHeat($$3,-1);
            }
        }
    }
}

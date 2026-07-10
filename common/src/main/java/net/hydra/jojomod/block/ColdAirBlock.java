package net.hydra.jojomod.block;

import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.Main;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class ColdAirBlock extends RoundaboutAttackBlock {
    public ColdAirBlock(Properties $$0) {
        super($$0);
    }


    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if ($$3 instanceof LivingEntity LE && !LE.isInvulnerable()) {
            if (!(((StandUser) LE).roundabout$getStandPowers() instanceof PowersWhiteAlbum PW &&
                    PowerTypes.hasStandActive(LE))) {
                if (LE.onGround()) {
                    if ($$3 instanceof Player pl &&
                            !(pl.level().getBlockState(
                                    BlockPos.containing(pl.getPosition(1).subtract(0, 0.5f, 0))).getBlock() instanceof FrozenBlock)
                    ) {
                        if (!(pl.hurtTime > 0)){
                            if (MainUtil.canFreeze(pl)) {
                                $$3.makeStuckInBlock($$0, new Vec3((double) 0.8F, (double) 0.8F, (double) 0.8F));
                            } else {
                                $$3.makeStuckInBlock($$0, new Vec3((double) 0.4F, (double) 0.9F, (double) 0.4F));
                            }
                        }
                    }
                } else {
                    if ($$1.isClientSide()){
                        $$3.setDeltaMovement($$3.getDeltaMovement().subtract(0,0.06,0));
                    }
                }
            }
        }
    }
}

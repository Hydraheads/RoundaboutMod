package net.hydra.jojomod.block;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.Main;
import net.minecraft.world.damagesource.DamageTypes;
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
                    if (
                            !(LE.level().getBlockState(
                                    BlockPos.containing(LE.getPosition(1).subtract(0, 0.5f, 0))).getBlock() instanceof FrozenBlock)
                    ) {
                        if ($$3 instanceof Player pl ){
                            if (!(pl.hurtTime > 0 ||
                                    !((StandUser)pl).roundabout$getLogSource().is(ModDamageTypes.STAND)
                            )){
                                if (MainUtil.canFreeze(pl)) {
                                    $$3.makeStuckInBlock($$0, new Vec3((double) 0.8F, (double) 0.8F, (double) 0.8F));
                                } else {
                                    $$3.makeStuckInBlock($$0, new Vec3((double) 0.4F, (double) 0.9F, (double) 0.4F));
                                }
                            }
                        } else {
                            if (MainUtil.canFreeze($$3) && LE.hurtTime <= 0) {
                                $$3.makeStuckInBlock($$0, new Vec3((double) 0.88F, (double) 0.88F, (double) 0.88F));
                            }
                        }
                    }
                } else {
                    if ($$1.isClientSide() && $$3 instanceof Player pl){
                        if (ClientUtil.isPlayer(pl)) {
                            $$3.setDeltaMovement($$3.getDeltaMovement().subtract(0, 0.06, 0));
                        }
                    }
                }
            }
        }
    }
}

package net.hydra.jojomod.block;

import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhiteAlbum;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class IceSpikeBlock
        extends StickyIceCoatingBlock {
    public IceSpikeBlock(Properties properties) {
        super(properties);
    }


    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if ($$3 instanceof LivingEntity LE && !LE.isInvulnerable() && !MainUtil.isBossMob($$3)) {
            if (!(((StandUser)LE).roundabout$getStandPowers() instanceof PowersWhiteAlbum PW &&
                    PowerTypes.hasStandActive(LE))) {
                if (LE.onGround()) {
                    if ((!(LE instanceof Player pl && pl.isCreative()))){
                        if (!$$1.isClientSide && ($$3.xOld != $$3.getX() || $$3.zOld != $$3.getZ())) {
                            double $$4 = Math.abs($$3.getX() - $$3.xOld);
                            double $$5 = Math.abs($$3.getZ() - $$3.zOld);
                            if ($$4 >= 0.003F || $$5 >= 0.003F) {
                                $$3.hurt($$1.damageSources().generic(), 0.5F);
                            }
                            if (MainUtil.getMobBleed(LE)){
                                MainUtil.makeBleed($$3,0,300,null);
                            }
                        }
                    }
                }
            }
        }
        super.entityInside($$0,$$1,$$2,$$3);
    }
}
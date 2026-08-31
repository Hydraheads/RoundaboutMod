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

public class StickyIceCoatingBlock
        extends WhiteAlbumCoatingBlock {
    public StickyIceCoatingBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean canSurvive(BlockState $$0, LevelReader $$1, BlockPos $$2) {
        BlockPos $$3 = $$2.below();
        return ($$1.getBlockState($$3).isFaceSturdy($$1, $$3, Direction.UP) ||
                $$1.getBlockState($$3).getBlock() instanceof LeavesBlock ||
                $$1.getBlockState($$3).getBlock() instanceof LeavesBlock) &&
                !$$1.getBlockState($$3).is(ModBlocks.WHITE_ALBUM_ICE_SLAB);
    }

    @Override
    public boolean isPathfindable(BlockState $$0, BlockGetter $$1, BlockPos $$2, PathComputationType $$3) {
        return true;
    }
    public void entityInside(BlockState $$0, Level $$1, BlockPos $$2, Entity $$3) {
        if ($$3 instanceof LivingEntity LE && !LE.isInvulnerable() && !MainUtil.isBossMob($$3)) {
            if (!(((StandUser)LE).roundabout$getStandPowers() instanceof PowersWhiteAlbum PW &&
                    PowerTypes.hasStandActive(LE))) {
                if (LE.onGround()) {
                    if (FateTypes.isVampire(LE)) {
                        if (this instanceof IceSpikeBlock){
                            $$3.makeStuckInBlock($$0, new Vec3((double) 0.6F, (double) 0.9F, (double) 0.6F));
                        } else {
                            $$3.makeStuckInBlock($$0, new Vec3((double) 0.3F, (double) 0.8F, (double) 0.3F));
                        }
                    } else {
                        if (LE instanceof Player || LE instanceof CloneEntity) {
                            if ((LE.hurtTime <= 3) ||
                                    (((StandUser)LE).roundabout$getLogSource() == null) ||
                                    !(((StandUser)LE).roundabout$getLogSource() != null &&
                                            (
                                                    ((StandUser)LE).roundabout$getLogSource().is(ModDamageTypes.STAND) ||
                                            ((StandUser)LE).roundabout$getLogSource().is(DamageTypes.PLAYER_ATTACK) ||
                                    ((StandUser)LE).roundabout$getLogSource().is(DamageTypes.ARROW) ||
                                                            ((StandUser)LE).roundabout$getLogSource().is(ModDamageTypes.BULLET) ||
                                                            ((StandUser)LE).roundabout$getLogSource().is(ModDamageTypes.SNIPER_BULLET)||
                                                            ((StandUser)LE).roundabout$getLogSource().is(ModDamageTypes.GASOLINE_EXPLOSION)||
                                                            ((StandUser)LE).roundabout$getLogSource().is(ModDamageTypes.KNIFE)
                                    ))
                            ) {
                                if (!(LE.level().getBlockState(
                                        BlockPos.containing(LE.getPosition(1).subtract(0, 0.5f, 0))).getBlock() instanceof FrozenBlock)
                                ) {
                                    if (this instanceof IceSpikeBlock){
                                        $$3.makeStuckInBlock($$0, new Vec3((double) 0.66F, (double) 0.9F, (double) 0.66F));
                                    } else {
                                        $$3.makeStuckInBlock($$0, new Vec3((double) 0.46F, (double) 0.8F, (double) 0.46F));
                                    }
                                }
                            }
                        } else {
                            if (this instanceof IceSpikeBlock){
                                $$3.makeStuckInBlock($$0, new Vec3((double) 0.9F, (double) 0.9F, (double) 0.9F));
                            } else {
                                $$3.makeStuckInBlock($$0, new Vec3((double) 0.8F, (double) 0.8F, (double) 0.8F));
                            }
                        }
                    }
                }
            }
            if (!$$1.isClientSide) {
                //HeatUtil.addHeat($$3,-1);
            }
        }
    }

    public float apM(float input){
        return input;
    }
    @SuppressWarnings("deprecation")
    @Override
    public boolean isCollisionShapeFullBlock(BlockState p_262062_, BlockGetter p_261848_, BlockPos p_261466_) {
        return false;
    }
    @Override
    public int range1(){
        return 2;
    }
    @Override
    public int range2(){
        return 5;
    }
    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
            return Shapes.empty();
    }
    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level,
                                        BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
    @Override
    public VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos,
                                     CollisionContext context) {
        return Shapes.empty();
    }
}
package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.ArrayList;
import java.util.List;

public class ColdBlastProjectile extends RoundaboutGeneralProjectile{
    public ColdBlastProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, Level $$1) {
        super($$0, $$1);
    }

    public ColdBlastProjectile(LivingEntity $$1, Level $$2) {
        this(ModEntities.COLD_BLAST_PROJECTILE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    protected ColdBlastProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    public int getMaxLifeSpan(){
        return 20;
    }
    protected ColdBlastProjectile(EntityType<RoundaboutGeneralProjectile> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    public final AnimationState ripperEyes = new AnimationState();

    @Override
    public void tick() {
        if (!level().isClientSide()){
            if (tickCount % 1 == 0) {
                if (level() instanceof ServerLevel sl) {
                    sl.sendParticles(ModParticles.VACUUM,
                            this.getX(),
                            this.getY() + 0.5F,
                            this.getZ(),
                            5, 0.1,0.1,0.1, 0.1F);

                }
            }
            onChangedBlockX();
        }
        super.tick();
    }

    public boolean alreadyHitEntity(Entity entity){
        return alreadyHitEntities.contains(entity);
    }

    public List<Entity> alreadyHitEntities = new ArrayList<>();

    @Override
    protected void onHitEntity(EntityHitResult $$0) {
        if (!level().isClientSide()) {
            Entity ent = $$0.getEntity();
            if (ent != null && ent.isAlive()) {
                if (!alreadyHitEntity($$0.getEntity())) {
                    if (ent instanceof LivingEntity lv && !(getOwner() != null && getOwner().getUUID() == ent.getUUID())) {
                        blastEntity(lv);
                    }
                }
            }
        }
    }
    public void blastEntity(LivingEntity entity){
        //Add hurt code here
        //Roundabout.LOGGER.info("charge-> "+charge+" power-> "+power);
        if (!entity.isInvulnerable()){
            if (entity instanceof Player pl){
                HeatUtil.addHeat(entity,-25);
            } else {
                HeatUtil.addHeat(entity,-40);
            }
        }

        alreadyHitEntities.add(entity);
    }

    public void onChangedBlockX(){
        BlockPos blockPos = blockPosition();
        onChangedBlock2(blockPos);
        onChangedBlock2(blockPos.below());
        onChangedBlock2(blockPos.below().below());
    }
    public void onChangedBlock2(BlockPos blockPos){
        if (getOwner() != null) {
            boolean canFreezeGrass = ClientNetworking.getAppropriateConfig().whiteAlbumSettings.freezesGrassv2;
            BlockState blockState = ModBlocks.WHITE_ALBUM_ICE_BLOCK.defaultBlockState();
            int j = Math.min(16, 2 + 1);
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
            for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-j, -1, -j), blockPos.offset(j, -1, j))) {
                BlockState blockState3;
                mutableBlockPos.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                BlockState blockState2 = level().getBlockState(mutableBlockPos);
                if (!blockState2.isAir() || (blockState3 = level().getBlockState(blockPos2)) != FrostedIceBlock.meltsInto()
                        || !blockState.canSurvive(level(), blockPos2) ||
                        !level().isUnobstructed(blockState, blockPos2, CollisionContext.empty())) continue;
                level().setBlockAndUpdate(blockPos2, blockState);
                level().scheduleTick(blockPos2, ModBlocks.WHITE_ALBUM_ICE_BLOCK, Mth.nextInt(level().getRandom(), 110, 130));
            }

            j = 2;
            blockState = ModBlocks.WHITE_ALBUM_ICE_SLAB.defaultBlockState();
            for (BlockPos blockPos2 : BlockPos.betweenClosed(blockPos.offset(-j, 0, -j), blockPos.offset(j, 0, j))) {

                mutableBlockPos.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
                BlockState blockState2 = level().getBlockState(mutableBlockPos);
                BlockState blockState3 = level().getBlockState(mutableBlockPos.below());
                if (!blockState.canSurvive(level(), blockPos2) ||
                        !level().isUnobstructed(blockState, blockPos2, CollisionContext.empty())) continue;
                if (blockState3.isAir() ||
                        (MainUtil.getIsGamemodeApproriateForGrief(getOwner()) && canFreezeGrass &&
                                blockState3.canBeReplaced() &&
                                !(blockState3.getBlock() instanceof LiquidBlockContainer) &&
                                !(blockState3.getBlock() instanceof FireBlock) &&
                                !(blockState3.getBlock() instanceof StandFireBlock)
                                &&
                                !blockState3.liquid() &&
                                !(blockState3.hasProperty(BlockStateProperties.WATERLOGGED) &&
                                        blockState3.getValue(BlockStateProperties.WATERLOGGED)
                                )
                        )
                ) {
                    level().setBlockAndUpdate(blockPos2, blockState);
                    level().scheduleTick(blockPos2, ModBlocks.WHITE_ALBUM_ICE_SLAB, Mth.nextInt(level().getRandom(), 110, 130));
                }
            }
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if (!level().isClientSide()) {
            discard();
        }
    }
    @Override
    public boolean needsStandUser(){
        return false;
    }
    @Override
    public boolean killAtZero(){
        return false;
    }
}

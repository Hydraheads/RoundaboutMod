package net.hydra.jojomod.entity.projectile;

import net.hydra.jojomod.block.ModBlocks;
import net.hydra.jojomod.block.StandFireBlock;
import net.hydra.jojomod.block.StickyIceCoatingBlock;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.BlockWallEntity;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.HeatUtil;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
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


    public boolean hasSpikes = false;
    public ColdBlastProjectile(LivingEntity $$1, Level $$2) {
        this(ModEntities.COLD_BLAST_PROJECTILE, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    protected ColdBlastProjectile(EntityType<? extends RoundaboutGeneralProjectile> $$0, double $$1, double $$2, double $$3, Level $$4) {
        this($$0, $$4);
        this.setPos($$1, $$2, $$3);
    }
    public int getMaxLifeSpan(){
        return 19;
    }
    protected ColdBlastProjectile(EntityType<RoundaboutGeneralProjectile> $$0, LivingEntity $$1, Level $$2) {
        this($$0, $$1.getX(), $$1.getEyeY() - 0.1F, $$1.getZ(), $$2);
        this.setOwner($$1);
    }
    public final AnimationState ripperEyes = new AnimationState();

    public boolean playedSound = false;
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
                if (ent instanceof BlockWallEntity){
                    discard();
                    return;
                }

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
        if (PowerTypes.isInADifferentExistence(entity,this)){
            return;
        }
        if (!entity.isInvulnerable()){
            if (entity instanceof Player pl){
                if (HeatUtil.getHeat(pl) > -97){
                    HeatUtil.addHeat(entity,-33);
                }
            } else {
                HeatUtil.makeAngryAtFreeze(entity,getUser());
                HeatUtil.addHeat(entity,-40);
                if (entity instanceof Mob mob && !(entity instanceof AbstractVillager) && getUser() != null
                && !(getUser() instanceof Player pl && pl.isCreative())) {
                    mob.setTarget(getUser());
                    getUser().setLastHurtMob(mob);
                }
            }
        }

        alreadyHitEntities.add(entity);
        if (!playedSound){
            playedSound = true;

            this.level().playSound(null, this.blockPosition(),  ModSounds.DING_EVENT,
                    SoundSource.PLAYERS, 0.8F, 1.4F);
        }

        ((ServerLevel) this.level()).sendParticles(ModParticles.ICE_SPARKLE, entity.getX(),
                entity.getY()+(entity.getBbHeight()*0.5), entity.getZ(),
                30,
                1, 0.4, 1,
                0.01);
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
                    level().scheduleTick(blockPos2, ModBlocks.WHITE_ALBUM_ICE_SLAB, Mth.nextInt(level().getRandom(), 150, 170));
                }
            }
        }

    }

    @Override
    protected void onHitBlock(BlockHitResult $$0) {
        super.onHitBlock($$0);
        if (!level().isClientSide()) {
            int range = 1;
            for (int y = -1; y < 3; y++) {
                for (int x = -range; x <= range; x++) {
                    for (int z = -range; z <= range; z++) {
                        BlockPos targetPos = getOnPos().offset(x, y, z);
                        Block block = ModBlocks.STICKY_ICE;
                        if (hasSpikes){
                            block = ModBlocks.ICE_SPIKE;
                        }

                        BlockState iceState = block.defaultBlockState();

                        if (canFreeze(targetPos)
                                && iceState.canSurvive(level(), targetPos)) {
                                level().setBlockAndUpdate(targetPos, iceState);
                                level().scheduleTick(targetPos, block, Mth.nextInt(level().getRandom(), 141, 145));
                        }
                        // placement logic
                    }
                }
            }
            discard();
        }
    }
    public boolean canFreeze(BlockPos pos) {
        BlockState state = level().getBlockState(pos);
        if (MainUtil.getIsGamemodeApproriateForGrief(getOwner()) ||
                state.isAir() || state.is(ModBlocks.WHITE_ALBUM_ICE_SLAB)) {
            if (!state.canBeReplaced())
                return false;

            if (!state.getFluidState().isEmpty())
                return false;

            if (state.getBlock() instanceof LiquidBlockContainer)
                return false;

            if (state.getBlock() instanceof FireBlock)
                return false;

            if (state.getBlock() instanceof StickyIceCoatingBlock)
                return false;

            if (state.getBlock() instanceof StandFireBlock)
                return false;

            if (state.hasProperty(BlockStateProperties.WATERLOGGED)
                    && state.getValue(BlockStateProperties.WATERLOGGED))
                return false;

        return true;
        }
        return false;
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

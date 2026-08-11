package net.hydra.jojomod.block;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.AcidExposureTracker;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.config.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public final class HallucinatoryAcidBlockEntity extends BlockEntity {
    private static final TagKey<Block> DISSOLVABLE_WOOD = TagKey.create(Registries.BLOCK,
            Roundabout.location("acid_dissolvable_wood"));
    private UUID owner;
    private long expiresAt;
    private int dissolveProgress;
    private double dissolveAccumulator;
    private long dissolveTarget = Long.MIN_VALUE;

    public HallucinatoryAcidBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlocks.HALLUCINATORY_ACID_BLOCK_ENTITY, pos, state);
    }

    public void initialize(UUID owner, long expiresAt) {
        this.owner = owner;
        this.expiresAt = expiresAt;
        setChanged();
    }

    public void copyLifetimeTo(HallucinatoryAcidBlockEntity other) {
        other.initialize(owner, expiresAt);
    }

    public CompoundTag saveTransferData() {
        CompoundTag tag = new CompoundTag();
        if (owner != null) tag.putUUID("Owner", owner);
        tag.putLong("ExpiresAt", expiresAt);
        return tag;
    }

    public void loadTransferData(CompoundTag tag) {
        owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
        expiresAt = tag.getLong("ExpiresAt");
        setChanged();
    }

    public void clearDissolveProgress(ServerLevel level) {
        if (dissolveTarget != Long.MIN_VALUE) {
            BlockPos target = BlockPos.of(dissolveTarget);
            level.destroyBlockProgress(dissolveCrackId(target), target, -1);
        }
        dissolveTarget = Long.MIN_VALUE;
        dissolveProgress = 0;
        dissolveAccumulator = 0.0D;
    }

    public boolean isOwner(Entity entity) {
        return owner != null && owner.equals(entity.getUUID());
    }

    public static void serverTick(net.minecraft.world.level.Level level, BlockPos pos,
                                  BlockState state, HallucinatoryAcidBlockEntity acid) {
        if (!(level instanceof ServerLevel server)) return;
        long time = server.getGameTime();
        if (ClientNetworking.getAppropriateConfig().whitesnakeSettings.waterWashesAwayAcid
                && state.getFluidState().is(Fluids.WATER)) {
            server.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            return;
        }
        if (acid.expiresAt == 0L) {
            acid.expiresAt = time + ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinatoryAcidDespawnTime;
        }
        if (acid.ownerDesummoned(server)) {
            server.removeBlock(pos, false);
            return;
        }
        if (acid.ownerWithinPauseRange(server, pos)) {
            acid.expiresAt++;
        } else if (time >= acid.expiresAt) {
            server.removeBlock(pos, false);
            return;
        }

        if (state.getBlock() instanceof HallucinatoryAcidBlock) {
            acid.tickDissolve(server, pos);
        } else {
            acid.clearDissolveProgress(server);
        }

        Vec3 center = Vec3.atCenterOf(pos);
        double range = ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinatoryAcidEffectRange;
        for (LivingEntity living : server.getEntitiesOfClass(LivingEntity.class,
                new AABB(pos).inflate(range), entity -> entity.isAlive()
                        && (acid.owner == null || !acid.owner.equals(entity.getUUID())))) {
            if (living.distanceToSqr(center) <= range * range) AcidExposureTracker.touch(living, time);
        }
    }

    private void tickDissolve(ServerLevel level, BlockPos acidPos) {
        Config.WhitesnakeSettings config = ClientNetworking.getAppropriateConfig().whitesnakeSettings;
        if (!config.acidGriefing || config.acidDissolveSpeed <= 0.0D) {
            clearDissolveProgress(level);
            return;
        }
        BlockPos target = acidPos.below();
        BlockState targetState = level.getBlockState(target);
        if (!targetState.is(DISSOLVABLE_WOOD) || targetState.getDestroySpeed(level, target) < 0.0F) {
            clearDissolveProgress(level);
            return;
        }
        long targetKey = target.asLong();
        if (dissolveTarget != targetKey) {
            clearDissolveProgress(level);
            dissolveTarget = targetKey;
        }
        dissolveAccumulator += config.acidDissolveSpeed / 20.0D;
        int stages = (int) dissolveAccumulator;
        if (stages <= 0) return;
        dissolveAccumulator -= stages;
        for (int stage = 0; stage < stages; stage++) {
            dissolveProgress++;
            if (dissolveProgress >= 10) {
                level.destroyBlockProgress(dissolveCrackId(target), target, -1);
                dissolveProgress = 0;
                dissolveAccumulator = 0.0D;
                dissolveTarget = Long.MIN_VALUE;
                level.destroyBlock(target, true);
                if (level.getBlockState(acidPos).getBlock() instanceof HallucinatoryAcidBlock acidBlock) {
                    level.scheduleTick(acidPos, acidBlock, 2);
                }
                return;
            }
        }
        level.destroyBlockProgress(dissolveCrackId(target), target, dissolveProgress - 1);
    }

    private static int dissolveCrackId(BlockPos pos) {
        return pos.hashCode() ^ 0x57534E4B;
    }

    private boolean ownerDesummoned(ServerLevel server) {
        if (!ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinatoryAcidDisappearsOnDesummon || owner == null) return false;
        Entity entity = server.getEntity(owner);
        return entity instanceof LivingEntity && !((StandUser) entity).roundabout$getActive();
    }

    private boolean ownerWithinPauseRange(ServerLevel server, BlockPos pos) {
        double range = ClientNetworking.getAppropriateConfig().whitesnakeSettings.hallucinatoryAcidDespawnPauseRange;
        if (owner == null || range <= 0.0D) return false;
        Entity entity = server.getEntity(owner);
        return entity != null && entity.isAlive()
                && entity.distanceToSqr(Vec3.atCenterOf(pos)) <= range * range;
    }

    @Override
    public void setRemoved() {
        if (level instanceof ServerLevel server) clearDissolveProgress(server);
        super.setRemoved();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (owner != null) tag.putUUID("Owner", owner);
        tag.putLong("ExpiresAt", expiresAt);
        tag.putInt("DissolveProgress", dissolveProgress);
        tag.putDouble("DissolveAccumulator", dissolveAccumulator);
        if (dissolveTarget != Long.MIN_VALUE) tag.putLong("DissolveTarget", dissolveTarget);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : null;
        expiresAt = tag.getLong("ExpiresAt");
        dissolveProgress = tag.getInt("DissolveProgress");
        dissolveAccumulator = tag.getDouble("DissolveAccumulator");
        dissolveTarget = tag.contains("DissolveTarget")
                ? tag.getLong("DissolveTarget") : Long.MIN_VALUE;
    }
}

package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.LifePunchCloneEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.hydra.jojomod.stand.powers.PowersKingNothing;
import net.hydra.jojomod.util.S2CPacketUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TrackingCloudEntity extends StandEntity {

    public static final double SPEED = 0.3;

    public static final byte
            PLAYER = 0,
            MOB = 1,
            BLOCK = 2;
    Entity targetEntity = null;
    BlockPos targetBPos = null;
    private boolean hasTarget = false;

    public TrackingCloudEntity(
            EntityType<? extends Mob> entityType,
            Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    public void tick() {
        this.setFadeOut(((byte) 1));
        if (getUser() == null || !getUser().isAlive() || !(getUserData(
                getUser()).roundabout$getStandPowers() instanceof PowersKingNothing PKN)) {
            this.discard();
            return;
        }
        if (PKN.getMemorizedScentType() == MOB || PKN.getMemorizedScentType() == PLAYER) {
            // PKN.clearTrackedBlockPos();
            List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(0.5f), entity ->
                    PKN.isInEntityGroup(entity.getType())
                            || entity.getUUID().equals(PKN.getMemorizedPlayer())
                            || (entity instanceof CloneEntity CE && (entity instanceof KingCrimsonCloneEntity
                            || entity instanceof LifePunchCloneEntity) && CE.getPlayerUUID().isPresent()
                            && CE.getPlayerUUID().get().equals(PKN.getMemorizedPlayer())));
            if (!list.isEmpty() && PKN.isTrackingMemorizedScent()) {
                PKN.highlightEntities.removeIf(entity -> !entity.equals(list.get(0)));
                PKN.highlightEntities.add(list.get(0));
            }
            else {
                PKN.highlightEntities.clear();
            }
        }
        else if (getUser() instanceof Player PE) {
            PKN.highlightEntities.clear();
            boolean posHasThing = false;
            boolean otherPosHasThing = false;
            BlockPos pos = blockPosition();
            BlockPos pos2 = null;
            BlockState state = level().getBlockState(pos);
            if (PKN.isInBlockGroup(state.getBlock()) && PKN.isTrackingMemorizedScent()) {
                posHasThing = true;
                if (state.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
                    if (state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
                        pos2 = pos.below();
                    }
                    else {
                        pos2 = pos.above();
                    }
                }
                else if (state.hasProperty(BlockStateProperties.CHEST_TYPE)
                        && state.getValue(BlockStateProperties.CHEST_TYPE) != ChestType.SINGLE) {
                    boolean isLeft = state.getValue(ChestBlock.TYPE) == ChestType.LEFT;
                    switch (state.getValue(ChestBlock.FACING)) {
                        case NORTH -> pos2 = isLeft ? pos.east() : pos.west();
                        case SOUTH -> pos2 = isLeft ? pos.west() : pos.east();
                        case WEST -> pos2 = isLeft ? pos.north() : pos.south();
                        case EAST -> pos2 = isLeft ? pos.south() : pos.north();
                    }
                }
                else if (state.hasProperty(BlockStateProperties.BED_PART)) {
                    boolean isBottom = state.getValue(BlockStateProperties.BED_PART) == BedPart.FOOT;
                    switch (Objects.requireNonNull(BedBlock.getBedOrientation(level(), pos))) {
                        case WEST -> pos2 = isBottom ? pos.west() : pos.east();
                        case EAST -> pos2 = isBottom ? pos.east() : pos.west();
                        case NORTH -> pos2 = isBottom ? pos.north() : pos.south();
                        case SOUTH -> pos2 = isBottom ? pos.south() : pos.north();
                    }
                }
                otherPosHasThing = pos2 != null;
                if (!PKN.highlightBPosses.contains(pos) || (pos2 != null && !PKN.highlightBPosses.contains(pos2))) {
                    PKN.highlightBPosses.add(pos);
                    if (pos2 != null) {
                        PKN.highlightBPosses.add(pos2);
                    }
                    S2CPacketUtil.knAddItemContainer(PE, pos, pos2 == null ? pos : pos2);
                }
            }
            if (!level().isClientSide) {
                Set<BlockPos> removed = new HashSet<>();
                for (BlockPos bpos : PKN.highlightBPosses) {
                    if ((posHasThing && bpos.equals(pos)) || (otherPosHasThing && bpos.equals(pos2))) continue;
                    removed.add(bpos);
                    S2CPacketUtil.knRemoveItemContainer(PE, bpos);
                }
                PKN.highlightBPosses.removeAll(removed);
            }
        }
        if (tickCount % 2 == 1) {
            redirectVector();
        }
        if (!hasTarget) {
            this.discard();
            return;
        }
        super.tick();
    }

    public void redirectVector() {
        if (((StandUser) getUser()).roundabout$getStandPowers() instanceof PowersKingNothing PKN) {
            byte memorizedScentType = PKN.getMemorizedScentType();
            Vec3 targetPos = null;
            Entity oldTargetEnt = targetEntity;
            BlockPos oldTargetBPos = targetBPos;
            switch (memorizedScentType) {
                case PLAYER -> {
                    Entity player = this.level().getPlayerByUUID(PKN.getMemorizedPlayer());
                    if (player == null) {
                        hasTarget = false;
                        break;
                    }
                    if (((StandUser) player).roundabout$getLifePunchClone() != null) {
                        player = ((StandUser) player).roundabout$getLifePunchClone();
                    }
                    else if (((StandUser) player).roundabout$getStandPowers() instanceof PowersKingCrimson PKC
                            && PKC.timeEraseActive && PKC.activeClone != null) {
                        player = PKC.activeClone;
                    }
                    else if (PowerTypes.isInADifferentExistence(player, this) || distanceTo(player)
                            > ClientNetworking.getAppropriateConfig().kingNothingSettings.playerTrackingRange) {
                        hasTarget = false;
                        break;
                    }
                    targetPos = player.getBoundingBox().getCenter();
                    targetEntity = player;
                    if (!targetEntity.equals(oldTargetEnt)) {
                        // FIND BETTER SOUND
                        if (this.getUser() instanceof ServerPlayer pl) {
                            S2CPacketUtil.sendPlaySoundPacket(pl, this.getUser().getId(), PowersKingNothing.FOUND_ITEM);
                        }
                    }
                    hasTarget = true;
                }
                case MOB -> {
                    Entity closest = null;
                    float closestDist = Float.MAX_VALUE;
                    List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(
                                    ClientNetworking.getAppropriateConfig().kingNothingSettings.mobTrackingRange),
                            entity -> PKN.isInEntityGroup(entity.getType()));
                    for (Entity ent : list) {
                        if (ent == null || ((LivingEntity) ent).isDeadOrDying() || PowerTypes.isInADifferentExistence(
                                this, ent)) {
                            continue;
                        }
                        float potentialBest = this.distanceTo(ent);
                        if (potentialBest < closestDist) {
                            closest = ent;
                            closestDist = potentialBest;
                            if (closestDist == 0) {
                                break;
                            }
                        }
                    }
                    if (closest == null) {
                        hasTarget = false;
                    }
                    else {
                        targetPos = closest.getBoundingBox().getCenter();
                        targetEntity = closest;
                        if (!targetEntity.equals(oldTargetEnt)) {
                            // FIND BETTER SOUND
                            if (this.getUser() instanceof ServerPlayer pl) {
                                S2CPacketUtil.sendPlaySoundPacket(pl, this.getUser().getId(),
                                        PowersKingNothing.FOUND_ITEM);
                            }
                        }
                        hasTarget = true;
                    }
                }
                case BLOCK -> {
                    BlockPos closest = null;
                    Vec3 closestPos = null;
                    float closestDist = Float.MAX_VALUE;
                    AABB $$0 = getBoundingBox().inflate(
                            ClientNetworking.getAppropriateConfig().kingNothingSettings.blockTrackingRange);
                    Iterable<BlockPos> posList =
                            BlockPos.betweenClosed(Mth.floor($$0.minX), Mth.floor($$0.minY), Mth.floor($$0.minZ),
                                    Mth.floor($$0.maxX), Mth.floor($$0.maxY), Mth.floor($$0.maxZ));
                    for (BlockPos pos : posList) {
                        if (!PKN.isInBlockGroup(level().getBlockState(pos).getBlock())) continue;
                        if (distanceToSqr(pos.getCenter()) < closestDist) {
                            closestPos = pos.getCenter();
                            closest = new BlockPos(pos);
                            closestDist = (float) distanceToSqr(pos.getCenter());
                        }
                    }
                    if (closestPos == null) {
                        hasTarget = false;
                    }
                    else {
                        targetPos = closestPos;
                        targetBPos = closest;
                        if (!targetBPos.equals(oldTargetBPos)) {
                            // FIND BETTER SOUND
                            if (this.getUser() instanceof ServerPlayer pl) {
                                S2CPacketUtil.sendPlaySoundPacket(pl, this.getUser().getId(),
                                        PowersKingNothing.FOUND_ITEM);
                            }
                        }
                        hasTarget = true;
                    }
                }
            }

            if (targetPos != null) {
                Vec3 vector = new Vec3(
                        (targetPos.x() - this.getX()),
                        (targetPos.y() - this.getY()),
                        (targetPos.z() - this.getZ())
                ).normalize().scale(SPEED * 0.75);
                if (vector.length() > 1.0E-1) {
                    this.setDeltaMovement(vector);
                }
                else {
                    this.setDeltaMovement(Vec3.ZERO);
                }
            }
            else {
                this.setDeltaMovement(Vec3.ZERO);
            }
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public boolean isInBlock() {
        return level().getBlockState(blockPosition()).isCollisionShapeFullBlock(level(), blockPosition());
    }
}

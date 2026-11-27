package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.KingCrimsonCloneEntity;
import net.hydra.jojomod.entity.LifePunchCloneEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.hydra.jojomod.stand.powers.PowersKingNothing;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TrackingCloudEntity extends StandEntity {

    public static final double SPEED = 0.2;

    public static final byte
            PLAYER = 0,
            MOB = 1,
            BLOCK = 2;

    private boolean hasTarget = false;

    public TrackingCloudEntity(
            EntityType<? extends Mob> entityType,
            Level world) {
        super(entityType, world);
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
            if (!list.isEmpty()) {
                PKN.setTrackedEntity(list.get(0));
            }
            else {
                PKN.clearTrackedEntity();
            }
        }
        else {
            PKN.clearTrackedEntity();
            if (PKN.isInBlockGroup(level().getBlockState(blockPosition()).getBlock())) {
                // PKN.setTrackedBlockPos(blockPosition());
            }
            else {
                // PKN.clearTrackedBlockPos();
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
                    else if (PowerTypes.isInADifferentExistence(player, this) || distanceTo(player) > 64) {
                        hasTarget = false;
                        break;
                    }
                    targetPos = player.getBoundingBox().getCenter();
                    hasTarget = true;
                }
                case MOB -> {
                    Entity closest = null;
                    float closestDist = Float.MAX_VALUE;
                    List<Entity> list = this.level().getEntities(this, this.getBoundingBox().inflate(64f),
                            entity -> PKN.isInEntityGroup(entity.getType()));
                    for (Entity ent : list) {
                        if (ent == null || ((LivingEntity) ent).isDeadOrDying() || PowerTypes.isInADifferentExistence(
                                this, ent)) continue;
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
                        hasTarget = true;
                    }
                }
                case BLOCK -> {
                    Vec3 closest = null;
                    float closestDist = Float.MAX_VALUE;
                    AABB $$0 = getBoundingBox().inflate(32f);
                    Iterable<BlockPos> posList =
                            BlockPos.betweenClosed(Mth.floor($$0.minX), Mth.floor($$0.minY), Mth.floor($$0.minZ),
                                    Mth.floor($$0.maxX), Mth.floor($$0.maxY), Mth.floor($$0.maxZ));
                    for (BlockPos pos : posList) {
                        if (!PKN.isInBlockGroup(level().getBlockState(pos).getBlock())) continue;
                        if (distanceToSqr(pos.getCenter()) < closestDist) {
                            closest = pos.getCenter();
                            closestDist = (float) distanceToSqr(pos.getCenter());
                        }
                    }
                    if (closest == null) {
                        hasTarget = false;
                    }
                    else {
                        targetPos = closest;
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

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
}

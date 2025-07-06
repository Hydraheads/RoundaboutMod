package net.hydra.jojomod.event;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.ICreeper;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.sound.ModSounds;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.RelativeMovement;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Set;

public class SavedSecond {

    public float headYRotation;
    public Vec2 rotationVec;
    public Vec3 position;
    public Vec3 deltaMovement;
    public boolean hasHadParticle = false;
    public Entity isTickingParticles = null;

    public float fallDistance = 0;
    public ResourceKey<DimensionType> dimensionTypeId = null;

    public SavedSecond(float headYRotation,Vec2 rotationVec,Vec3 position, Vec3 deltaMovement, float fallDistance,
                       ResourceKey<DimensionType> dimensionId){
        this.headYRotation = headYRotation;
        this.rotationVec = new Vec2(rotationVec.x,rotationVec.y);
        this.position = new Vec3(position.x,position.y,position.z);
        this.deltaMovement = new Vec3(deltaMovement.x,deltaMovement.y,deltaMovement.z);
        this.fallDistance = fallDistance;
        dimensionTypeId = dimensionId;
    }

    public static SavedSecond saveEntitySecond(Entity ent) {
        if (ent instanceof Player PL) {
            return new SavedSecondPlayer(
                    PL.getYHeadRot(),
                    PL.getRotationVector(),
                    PL.getPosition(1),
                    PL.getDeltaMovement(),
                    ent.fallDistance,
                    ent.level().dimensionTypeId(),
                    PL.getActiveEffects(),
                    PL.getHealth(),
                    PL.getRemainingFireTicks(),
                    ((StandUser)PL).roundabout$getRemainingFireTicks(),
                    ((StandUser)PL).roundabout$getOnStandFire(),
                    ((StandUser)PL).roundabout$getGasolineTime(),
                    PL.getAirSupply(),
                    ((StandUser)PL).roundabout$getLocacacaCurse(),
                    PL.getFoodData().getFoodLevel(),
                    PL.getFoodData().getSaturationLevel(),
                    PL.getFoodData().getExhaustionLevel()
            );
        } if (ent instanceof Creeper CE) {
            return new SavedSecondCreeper(
                    CE.getYHeadRot(),
                    CE.getRotationVector(),
                    CE.getPosition(1),
                    CE.getDeltaMovement(),
                    ent.fallDistance,
                    ent.level().dimensionTypeId(),
                    CE.getActiveEffects(),
                    CE.getHealth(),
                    CE.getRemainingFireTicks(),
                    ((StandUser)CE).roundabout$getRemainingFireTicks(),
                    ((StandUser)CE).roundabout$getOnStandFire(),
                    ((StandUser)CE).roundabout$getGasolineTime(),
                    CE.getAirSupply(),
                    ((StandUser)CE).roundabout$getLocacacaCurse(),
                    ((ICreeper)CE).roundabout$getSwell()
            );
        } if (ent instanceof LivingEntity LE) {
            return new SavedSecondLiving(
                    LE.getYHeadRot(),
                    LE.getRotationVector(),
                    LE.getPosition(1),
                    LE.getDeltaMovement(),
                    ent.fallDistance,
                    ent.level().dimensionTypeId(),
                    LE.getActiveEffects(),
                    LE.getHealth(),
                    LE.getRemainingFireTicks(),
                    ((StandUser)LE).roundabout$getRemainingFireTicks(),
                    ((StandUser)LE).roundabout$getOnStandFire(),
                    ((StandUser)LE).roundabout$getGasolineTime(),
                    LE.getAirSupply(),
                    ((StandUser)LE).roundabout$getLocacacaCurse()
            );
        } if (ent instanceof AbstractArrow LE) {
            return new SavedSecondAbstractArrow(
                    LE.getYHeadRot(),
                    LE.getRotationVector(),
                    LE.getPosition(1),
                    LE.getDeltaMovement(),
                    ent.fallDistance,
                    ent.level().dimensionTypeId(),
                    ((IAbstractArrowAccess)LE).roundaboutGetInGround()
            );
        } if (ent != null){
            return new SavedSecond(
                    ent.getYHeadRot(),
                    ent.getRotationVector(),
                    ent.getPosition(1),
                    ent.getDeltaMovement(),
                    ent.fallDistance,
                    ent.level().dimensionTypeId()
            );
        }
        return null;
    }

    public void loadTime(Entity ent){
        if (ent == null || (dimensionTypeId != ent.level().dimensionTypeId()))
            return;
        boolean canBeRepositioned = true;
        boolean suffocationBlocker = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindStopsSuffocation;
        if (suffocationBlocker) {
            if (!canTeleportTo(ent.level(),position,ent)){
                canBeRepositioned = false;
            }
        }
        if (canBeRepositioned && !(ent.getVehicle() != null && MainUtil.canRewindInTime(ent.getVehicle(),ent))) {
            ent.setYHeadRot(this.headYRotation);
            ent.setXRot(this.rotationVec.x);
            ent.setYRot(this.rotationVec.y);
            ent.teleportTo(((ServerLevel) ent.level()),this.position.x,this.position.y,this.position.z,
                    Set.of(
                            RelativeMovement.X,
                            RelativeMovement.Y,
                            RelativeMovement.Z),
        this.headYRotation,this.rotationVec.x);
            ent.hurtMarked = true;
            ent.setDeltaMovement(this.deltaMovement);
            ent.hasImpulse = true;
            ent.fallDistance = this.fallDistance;
        }
        ((IEntityAndData)ent).roundabout$resetSecondQueue();
    }

    public static boolean canTeleportTo(Level level, Vec3 targetPos, Entity entity) {
        // Get entity dimensions
        double width = entity.getBbWidth();
        double height = entity.getBbHeight();

        // Construct bounding box at the target position
        AABB targetBox = new AABB(
                targetPos.x - width / 2.0, targetPos.y, targetPos.z - width / 2.0,
                targetPos.x + width / 2.0, targetPos.y + height, targetPos.z + width / 2.0
        );

        // 1. Check if space is clear
        if (!level.noCollision(entity, targetBox)) {
            return false;
        }


        boolean deviousStratBlocker = ClientNetworking.getAppropriateConfig().mandomSettings.timeRewindStopsDeviousStrategies;

        if (deviousStratBlocker) {
            // 2. Check for dangerous blocks inside target box
            for (BlockPos pos : BlockPos.betweenClosed(
                    Mth.floor(targetBox.minX), Mth.floor(targetBox.minY), Mth.floor(targetBox.minZ),
                    Mth.floor(targetBox.maxX), Mth.floor(targetBox.maxY), Mth.floor(targetBox.maxZ))) {

                BlockState state = level.getBlockState(pos);
                Block block = state.getBlock();

                // List of bad blocks to avoid
                if (block == Blocks.COBWEB || block == Blocks.LAVA) {
                    return false;
                }

                // Optional: also avoid fire or cactus
                if (block == Blocks.FIRE || block == Blocks.CACTUS) {
                    return false;
                }
            }
        }

        return true;
    }
}

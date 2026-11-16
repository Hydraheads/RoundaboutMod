package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.stand.powers.PowersKingNothing;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrackingCloudEntity extends StandEntity {
    /**
     * Initialize Stands
     *
     * @param entityType
     * @param world
     */
    public TrackingCloudEntity(
            EntityType<? extends Mob> entityType,
            Level world) {
        super(entityType, world);
    }

    public BlockPos targetBlockPos = null;
    public Entity targetEntity = null;

    @Override
    public void tick() {
        if (getUser() == null || !getUser().isAlive() || !(getUserData(
                getUser()).roundabout$getStandPowers() instanceof PowersKingNothing)) {
                    this.discard();
                }
    }

    public Vec3 getTargetPos() {
        if (targetBlockPos != null) {
            return targetBlockPos.getCenter();
        }
        else if (targetEntity != null) {
            return targetEntity.getBoundingBox().getCenter();
        }
        return new Vec3(1, 1, 1);
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
}

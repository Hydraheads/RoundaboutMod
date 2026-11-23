package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.hydra.jojomod.stand.powers.PowersKingNothing;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TrackingCloudEntity extends StandEntity {

    public static final double SPEED = 0.3;

    public static final byte
            PLAYER = 0,
            MOB = 1,
            BLOCK = 2;

    public TrackingCloudEntity(
            EntityType<? extends Mob> entityType,
            Level world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        this.setFadeOut(((byte) 1));
        if (getUser() == null || !getUser().isAlive() || !(getUserData(
                getUser()).roundabout$getStandPowers() instanceof PowersKingNothing)) {
                    this.discard();
                    return;
                }
        redirectVector();
        super.tick();
    }

    public void redirectVector() {
        Vec3 targetPos = new Vec3(0, 0, 0);
        boolean hasFoundTarget = false;
        if (((StandUser) getUser()).roundabout$getStandPowers() instanceof PowersKingNothing PKN) {
            switch (PKN.getMemorizedScentType()) {
                case PLAYER -> {
                    Entity player = this.level().getPlayerByUUID(PKN.getMemorizedPlayer());
                    if (player == null) return;
                    if (((StandUser) player).roundabout$getLifePunchClone() != null) {
                        player = ((StandUser) player).roundabout$getLifePunchClone();
                    }
                    else if (((StandUser) player).roundabout$getStandPowers() instanceof PowersKingCrimson PKC
                            && PKC.timeEraseActive && PKC.activeClone != null) {
                        player = PKC.activeClone;
                    }
                    else if (PowerTypes.isInADifferentExistence(player, this)) {
                        return;
                    }
                    targetPos = player.getBoundingBox().getCenter();
                }
                case MOB -> {
                    if (this.tickCount % 10 != 0) return;
                }
                case BLOCK -> {
                    if (this.tickCount % 10 != 0) return;
                }
            }
        }
        // Entity currentTarget = getTarget();
        // if (currentTarget == null) { return; }

        // Vec3 pos = currentTarget.getPosition(0);
        // Vec3 addToPosition = new Vec3(0, currentTarget.getBbHeight() * 0.5f, 0);
        // Direction direction = ((IGravityEntity) currentTarget).roundabout$getGravityDirection();
        // if (direction != Direction.DOWN) {
        //     addToPosition = RotationUtil.vecPlayerToWorld(addToPosition, direction);
        // }

        // Vec3 targetPos = pos.add(addToPosition);

        Vec3 vector = new Vec3(
                (targetPos.x() - this.getX()),
                (targetPos.y() - this.getY()),
                (targetPos.z() - this.getZ())
        ).normalize().scale(SPEED * 0.75);
        if (!vector.equals(Vec3.ZERO)) {
            this.setDeltaMovement(vector);
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

package net.hydra.jojomod.entity.substand;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.particles.PuzzleColorParticleOptions;
import net.hydra.jojomod.particles.PuzzleParticle;
import net.hydra.jojomod.stand.powers.PowersKingNothing;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;

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

    @Override
    public void tick() {
        if (getUser() == null || !getUser().isAlive() || !(getUserData(
                getUser()).roundabout$getStandPowers() instanceof PowersKingNothing)) {
                    this.discard();
                }
    }

    public static AttributeSupplier.Builder createStandAttributes() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED,
                0.2F).add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 2.0);
    }
}

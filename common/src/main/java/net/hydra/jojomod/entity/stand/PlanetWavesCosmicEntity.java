package net.hydra.jojomod.entity.stand;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;

public class PlanetWavesCosmicEntity extends PlanetWavesEntity {
    public PlanetWavesCosmicEntity(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public final AnimationState idle_cosmic = new AnimationState();

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        if (this.getUser() != null) {
            this.idle_cosmic.startIfStopped(this.tickCount);
        } else {
            this.idle_cosmic.stop();
        }
    }
}
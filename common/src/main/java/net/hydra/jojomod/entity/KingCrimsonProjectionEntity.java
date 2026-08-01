package net.hydra.jojomod.entity;

import net.hydra.jojomod.entity.navigation.ActiveCloneManager;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public class KingCrimsonProjectionEntity extends CloneEntity {
    protected KingCrimsonProjectionEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        discard();
        return false;
    }
    public boolean contains = false;
    @Override
    public void tick() {
        if (!level().isClientSide()) {
            if (!contains) {
                contains = true;
                ActiveCloneManager.add(this);
            }
        }
        super.tick();
    }

}

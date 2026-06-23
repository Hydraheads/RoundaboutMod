package net.hydra.jojomod.entity.projectile;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class IceTwisterEntity extends Entity {
    public final AnimationState twisterSpin = new AnimationState();

    public IceTwisterEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }
}

package net.hydra.jojomod.entity;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BombPlantedSpectralArrow extends SpectralArrow {


    public BombPlantedSpectralArrow(EntityType<? extends SpectralArrow> $$0, Level $$1) {
        super($$0, $$1);
    }

    public BombPlantedSpectralArrow(Level $$0, LivingEntity $$1) {
        super($$0, $$1);
    }

    public BombPlantedSpectralArrow(Level $$0, double $$1, double $$2, double $$3) {
        super($$0, $$1, $$2, $$3);
    }


    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            if (getOwner() == null || !(getOwner().isAlive() && ((StandUser)getOwner()).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ
                && PKQ.bombEntity == this)) {
                defuse();
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        if (target != getOwner() && (getOwner() != null && ((StandUser)getOwner()).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ)
                && PKQ.bombEntity.getId() == getId()) {
            PKQ.contactDetonate(target);
        }
    }

    public void defuse() {
        if (getOwner() instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }
}

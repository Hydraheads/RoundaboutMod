package net.hydra.jojomod.entity;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BombPlantedArrow extends Arrow {
    public BombPlantedArrow(EntityType<? extends Arrow> $$0, Level $$1) {
        super($$0, $$1);
    }

    public BombPlantedArrow(Level $$0, double $$1, double $$2, double $$3) {
        super($$0, $$1, $$2, $$3);
    }

    public BombPlantedArrow(Level $$0, LivingEntity $$1) {
        super($$0, $$1);
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
        if (target != getOwner() && (getOwner() != null && ((StandUser)getOwner()).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ)) {
            PKQ.arrowContacted(target);
        }
    }

    public void defuse() {
        if (getOwner() instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }
}

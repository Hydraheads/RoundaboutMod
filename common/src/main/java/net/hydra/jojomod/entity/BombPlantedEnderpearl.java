package net.hydra.jojomod.entity;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKillerQueen;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class BombPlantedEnderpearl extends ThrownEnderpearl {
    public BombPlantedEnderpearl(EntityType<? extends BombPlantedEnderpearl> p_37491_, Level p_37492_) {
        super(p_37491_, p_37492_);
    }

    public BombPlantedEnderpearl(Level level, LivingEntity p) {
        super(level, p);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        if (target != getOwner() && (getOwner() != null && ((StandUser)getOwner()).roundabout$getStandPowers() instanceof PowersKillerQueen PKQ)
                && PKQ.bombEntity.getId() == getId()) {
            PKQ.contactExplode(target);
        }
    }
}

package net.hydra.jojomod.entity;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class FogCloneEntity extends CloneEntity {
    public FogCloneEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean canCollideWith(Entity $$0) {
        if (this.getPlayer() != null && this.getPlayer().is($$0)){
            return false;
        }

        return $$0.canBeCollidedWith() && !this.isPassengerOfSameVehicle($$0);
    }

    @Override
    public void push(Entity $$0) {
        if (this.level().isClientSide()){
            Player safeLocal = ClientUtil.getPlayer();
            if (safeLocal != null && this.getPlayerUUID().isPresent() && safeLocal.getUUID().equals(this.getPlayerUUID().get())){
                return;
            }
        } else {

            if (this.getPlayer() != null && this.getPlayer().is($$0)){
                return;
            }
        }
        super.push($$0);
    }

    @Override
    public void doPush(Entity $$0) {
        if (this.level().isClientSide()){
            Player safeLocal = ClientUtil.getPlayer();
            if (safeLocal != null && this.getPlayerUUID().isPresent() && safeLocal.getUUID().equals(this.getPlayerUUID().get())){
                return;
            }
        } else {

            if (this.getPlayer() != null && this.getPlayer().is($$0)){
                return;
            }
        }
        super.doPush($$0);
    }
    @Override
    public boolean canBeCollidedWith() {
        if (this.level().isClientSide()){
            Player safeLocal = ClientUtil.getPlayer();
            if (safeLocal != null && this.getPlayerUUID().isPresent() && safeLocal.getUUID().equals(this.getPlayerUUID().get())){
                return false;
            }
        }

        return super.canBeCollidedWith();
    }
}

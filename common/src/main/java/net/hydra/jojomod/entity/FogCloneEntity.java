package net.hydra.jojomod.entity;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.CloneEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class FogCloneEntity extends CloneEntity {

    public int timer = 0;
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

    public int getTimer(){
        return timer;
    }
    public void setTimer(int timer){
        this.timer = timer;
    }
    @Override
    public void tick() {
        if (!this.level().isClientSide()) {
            this.timer--;
            if (this.timer < 0) {
                this.goPoof();
            }
        }
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

    public void goPoof(){
        simulatePoof(new Vec3(this.getX(),this.getY(),this.getZ()));
        this.discard();
    }

    public void simulatePoof(Vec3 vec){
        ((ServerLevel) this.level()).sendParticles(ModParticles.FOG_CHAIN, vec.x(),
                vec.y()+this.getEyeHeight(), vec.z(),
                12, 0.2, 0.3, 0.2, 0.3);
        this.level().playSound(null,  vec.x(),
                vec.y(), vec.z(), ModSounds.POP_EVENT, this.getSoundSource(), 1.0F, (float)(1F+ Math.random()*0.03));
    }

    @Override
    public boolean hurt(DamageSource $$0, float $$1) {
        if (this.isInvulnerableTo($$0)) {
            return false;
        } else if (this.level().isClientSide) {
            return false;
        } else if (this.isDeadOrDying()) {
            return false;
        }
        goPoof();
        return false;
    }
}

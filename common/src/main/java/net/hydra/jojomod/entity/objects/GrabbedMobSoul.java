package net.hydra.jojomod.entity.objects;

import net.hydra.jojomod.access.NoVibrationEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;

public class GrabbedMobSoul extends StandEntity{

    public GrabbedMobSoul(EntityType<? extends Mob> entityType, Level world) {
        super(entityType, world);
    }

    public final AnimationState animationHeld = new AnimationState();

    @Override
    public void tick(){
        super.tick();
        if(this.getUser() != null && (this.getUser().isRemoved() || this.getUser().isDeadOrDying())){
            forceDespawn(true);
        }
        if(this.getUser() == null){
            forceDespawn(true);
        }
    }

    @Override
    public void setupAnimationStates() {
        super.setupAnimationStates();
        this.animationHeld.startIfStopped(this.tickCount);
    }

    @Override
    public boolean isValid(boolean userActive, LivingEntity thisStand, LivingEntity userEntity){
        return true;
    }

    @Override
    public boolean needsActive(){
        return false;
    }

    @Override
    public boolean lockPos(){
        return false;
    }

    @Override
    public boolean canBeHitByProjectile() { return false;}

    @Override
    public boolean canBeHitByStands() { return false;}

    @Override
    public boolean mayInteract(Level $$0, BlockPos pos) { return false;}

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public boolean canAttack(LivingEntity le){
        return false;
    }
    @Override
    public boolean canBeSeenAsEnemy() {return false; }

    @Override
    public boolean canBeAffected(MobEffectInstance $$0) {
        return false;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isInvulnerable() { return true;}

    @Override
    public boolean canBeCollidedWith() { return false;}

    @Override
    public boolean isPushedByFluid() { return false;}

    @Override
    public boolean isNoGravity() { return true;}

    @Override
    public boolean canCollideWith(Entity $$0) { return false;}

    @Override
    public boolean hasNoPhysics(){
        return true;
    }
}

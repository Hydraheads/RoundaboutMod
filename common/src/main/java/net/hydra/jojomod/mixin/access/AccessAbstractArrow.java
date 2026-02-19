package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersD4C;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractArrow.class)
public abstract class AccessAbstractArrow extends Entity implements IAbstractArrowAccess {
    /**Allows access to abstract arrow functions and adds logic for dealing with arrows*/

    @Override
    public void roundabout$resetPiercedEntities(){
        resetPiercedEntities();
    }
    @Override
    public void roundabout$setLastState(BlockState last){
        lastState = last;
    }


    @Override
    public ItemStack roundabout$GetPickupItem(){
        return this.getPickupItem();
    }
    @Override
    public boolean roundabout$GetInGround(){
        return this.inGround;
    }

    @Override
    public void roundabout$SetInGround(boolean inGround){
        this.inGround = inGround;
    }

    public byte roundabout$GetPierceLevel(){
        return this.getPierceLevel();
    }
    @Override
    @Nullable
    public EntityHitResult roundabout$FindHitEntity(Vec3 $$0, Vec3 $$1){
        return this.findHitEntity($$0,$$1);
    }

    @Inject(method = "onHitEntity", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHitEntity(EntityHitResult $$0, CallbackInfo ci) {
        Entity entity = $$0.getEntity();

        if (entity instanceof LivingEntity LE){
            StandUser user = ((StandUser) entity);
            if (user.roundabout$isParallelRunning())
            {
                ci.cancel();
                return;
            }

            StandPowers entityPowers = user.roundabout$getStandPowers();
            if (!this.level().isClientSide && entityPowers instanceof PowersD4C d4cPowers)
            {
                if (d4cPowers.meltDodgeTicks >= 0)
                {
                    d4cPowers.meltDodge((AbstractArrow)(Object)this);
                    ci.cancel();
                    return;
                }
            }

            if (entityPowers.dealWithProjectile(this,$$0)){
                ci.cancel();
                this.discard();
                return;
            } else if (entityPowers.dealWithProjectileNoDiscard(this,$$0)){
                ci.cancel();
                return;
            }
        }


        if (entity instanceof PenetratableWithProjectile PP){
            if (PP.dealWithPenetration(this)){
                ci.cancel();
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    @Nullable
    private BlockState lastState;

    @Shadow
    private void resetPiercedEntities() {
    }

    @Shadow
    public boolean isNoPhysics(){
        return false;
    }
    @Shadow
    protected boolean canHitEntity(Entity $$0x){
        return false;
    }

    @Shadow
    protected boolean tryPickup(Player $$0){return false;}

    public AccessAbstractArrow(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    protected boolean inGround;
    @Shadow
    public int shakeTime;
    @Shadow
    public byte getPierceLevel(){
        return 0;
    }
    @Shadow
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 $$0, Vec3 $$1){
        return null;
    }

    @Shadow protected abstract ItemStack getPickupItem();

}

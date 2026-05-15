package net.hydra.jojomod.mixin.access;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.networking.ServerToClientPackets;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.WaterFluid;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(AbstractArrow.class)
public abstract class AccessAbstractArrow extends Entity implements IAbstractArrowAccess {

    @Shadow
    private int knockback;

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
    public IntOpenHashSet rdbt$piercingIgnoreEntityIds(){
        return piercingIgnoreEntityIds;
    }
    @Override
    public List<Entity> rdbt$piercedAndKilledEntities(){
        return piercedAndKilledEntities;
    }
    @Override
    public int rdbt$knockback(){
        return knockback;
    }
    @Override
    public void rdbt$piercingIgnoreEntityIds(IntOpenHashSet mp){
        piercingIgnoreEntityIds = mp;
    }
    @Override
    public void rdbt$piercedAndKilledEntities(List<Entity> ent){
        piercedAndKilledEntities = ent;
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

    /**Manhattan Transfer*/
    @Override
    public boolean roundabout$GetIsManhattan(){
        return this.inGround;
    }

    @Override
    public void roundabout$SetIsManhattan(boolean isManhattanProjectile){
        this.isManhattanProjectile = isManhattanProjectile;
    }

    public boolean isManhattanProjectile;

    @Inject(method = "onHitEntity", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHitEntity(EntityHitResult $$0, CallbackInfo ci) {
        Entity entity = $$0.getEntity();

        if (entity instanceof LivingEntity LE){
            StandUser user = ((StandUser) entity);

            StandPowers entityPowers = user.roundabout$getStandPowers();

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

    @Inject(method = "onHitEntity", at = @At(value = "TAIL"),cancellable = true)
    private void roundabout$onHitEntityHattan(EntityHitResult $$0, CallbackInfo ci) {
        Entity entity = $$0.getEntity();
        AbstractArrow ABA = (AbstractArrow) (Object) this;
        Projectile ABC = (AbstractArrow) (Object) this;
        if(isManhattanProjectile){
            ABA.setDeltaMovement(0.001, 0.001, 0.001);
            entity.hurt(damageSources().arrow(ABA, entity), roundabout$lastHattanDamage);
        }
    }

    @Unique
    public float roundabout$lastHattanDamage = 0;

    @Unique
    @Override
    public void roundabout$setHattanDamage(float manhattanDmg){
            roundabout$lastHattanDamage = manhattanDmg;
    }

    @Unique
    @Override
    public float roundabout$getHattanDamage(){
        return  roundabout$lastHattanDamage;
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Nullable
    private IntOpenHashSet piercingIgnoreEntityIds;

    @Shadow
    @Nullable
    private List<Entity> piercedAndKilledEntities;
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

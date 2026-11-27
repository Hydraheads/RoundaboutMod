package net.hydra.jojomod.mixin.access;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.PenetratableWithProjectile;
import net.hydra.jojomod.entity.objects.GentlyWeepsEntity;
import net.hydra.jojomod.entity.stand.ManhattanTransferEntity;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.damagesource.DamageSource;
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
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(AbstractArrow.class)
public abstract class AccessAbstractArrow extends Entity implements IAbstractArrowAccess {

    @Shadow
    private int knockback;

    /**Allows access to abstract arrow functions and adds logic for dealing with arrows*/

    @Override
    public void sba$resetPiercedEntities(){
        resetPiercedEntities();
    }
    @Override
    public void sba$setLastState(BlockState last){
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
    public ItemStack sba$GetPickupItem(){
        return this.getPickupItem();
    }
    @Override
    public boolean sba$GetInGround(){
        return this.inGround;
    }

    @Override
    public void sba$SetInGround(boolean inGround){
        this.inGround = inGround;
    }

    public byte sba$GetPierceLevel(){
        return this.getPierceLevel();
    }
    @Override
    @Nullable
    public EntityHitResult sba$FindHitEntity(Vec3 $$0, Vec3 $$1){
        return this.findHitEntity($$0,$$1);
    }

    /**Manhattan Transfer*/
    @Override
    public boolean sba$GetIsManhattan(){
        return this.isManhattanProjectile;
    }

    @Override
    public void sba$SetIsManhattan(boolean isManhattanProjectile){
            this.isManhattanProjectile = isManhattanProjectile;
    }

    @Override
    public void sba$isArrowTooSlow(boolean isTooSlow){
        this.isArrowTooSlow = isTooSlow;
    }

    public boolean isManhattanProjectile;
    public boolean isArrowTooSlow;
    public boolean isManhattanProjectileMobAI = false;

    @Override
    public void sba$isMobAiArrow(boolean isMobAIArrow){
        this.isManhattanProjectileMobAI = isMobAIArrow;
    }

    @Inject(method = "onHitEntity", at = @At(value = "HEAD"),cancellable = true)
    private void sba$onHitEntity(EntityHitResult $$0, CallbackInfo ci) {
        Entity entity = $$0.getEntity();
        AbstractArrow ABA = (AbstractArrow) (Object) this;

        if (ABA.getPierceLevel() > 0){
            return;
            //Unfortunately, piercing crossbows crash with basically any prevention of their methods because they are
            // coded very poorly
        }
        if (entity instanceof LivingEntity LE){
            StandUser user = ((StandUser) entity);

            StandPowers entityPowers = user.sba$getStandPowers();

            if (entityPowers.dealWithProjectile(this,$$0)){
                ci.cancel();
                this.discard();
                return;
            } else if (entityPowers.dealWithProjectileNoDiscard(this,$$0)){
                ci.cancel();
                return;
            }
        } else if (entity instanceof GentlyWeepsEntity gwe){
            GentlyWeepsEntity.dealWithProjectile(ABA,gwe);
            ci.cancel();
            return;
        }


        if (entity instanceof PenetratableWithProjectile PP){
            if (PP.dealWithPenetration(this)){
                ci.cancel();
            }
        }

        if(entity instanceof ManhattanTransferEntity ME && isManhattanProjectile){
            ME.getUser().hurt(damageSources().arrow(ABA, ABA.getOwner()), sba$lastHattanDamage);
            this.discard();
            ci.cancel();
        }

        if(isManhattanProjectile && !isManhattanProjectileMobAI){
            ABA.setDeltaMovement(0.0001, 0.0001, 0.0001);
            entity.invulnerableTime = 0;
            /** It's important to keep it here, because it should slow the arrow when it lands and then apply the damage at the very end*/
        }

    }

    @Inject(method = "onHitEntity", at = @At(value = "TAIL"),cancellable = true)
    private void sba$onHitEntityHattan(EntityHitResult $$0, CallbackInfo ci) {
        Entity entity = $$0.getEntity();
        AbstractArrow ABA = (AbstractArrow) (Object) this;
        if(isManhattanProjectile){
            if(!isManhattanProjectileMobAI) {
                entity.hurt(damageSources().arrow(ABA, ABA.getOwner()), sba$lastHattanDamage);
            }
            if(!isArrowTooSlow) {
                entity.invulnerableTime = 0;
                if (entity.isAlive()) {
                    doBonusDamageHattan(entity);
                }
            }
        }
    }

    protected void doBonusDamageHattan(Entity entity){
        DamageSource damageSource;
        AbstractArrow ABA = (AbstractArrow) (Object) this;
        Entity entity2 = ABA.getOwner();
        damageSource = ModDamageTypes.of(this.level(), ModDamageTypes.STAND, this, entity2);
        float amount = 1 + (sba$lastHattanDamage / 5);
        float damage = amount <= 4 ? amount : 4;

        float amountPlayersAndBosses = 1 + (sba$lastHattanDamage / 8);
        float damagePlayersAndBosses = Math.min(amountPlayersAndBosses, 1.5F);

        if(!isManhattanProjectileMobAI) {
            if (entity instanceof Player || MainUtil.isBossMob(entity)) {
                entity.hurt(damageSource, damagePlayersAndBosses);
            } else {
                entity.hurt(damageSource, damage);
            }
        } else {
            entity.hurt(damageSource, 1);
        }

    }

    @Unique
    public float sba$lastHattanDamage = 0;

    @Unique
    @Override
    public void sba$setHattanDamage(float manhattanDmg){
            sba$lastHattanDamage = manhattanDmg;
    }

    @Unique
    @Override
    public float sba$getHattanDamage(){
        return  sba$lastHattanDamage;
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

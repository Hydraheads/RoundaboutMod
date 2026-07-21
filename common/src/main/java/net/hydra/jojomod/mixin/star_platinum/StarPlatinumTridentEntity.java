package net.hydra.jojomod.mixin.star_platinum;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.ISuperThrownAbstractArrow;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrownTrident.class)
public class StarPlatinumTridentEntity {

    /**Star Platinum's super item throw ability is canceled on hit, otherwise the returning trident...
     * will be a little rebellious until super throw wears off and keep careening forward*/

    @Inject(method = "onHitEntity", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$onHitEntity(EntityHitResult $$0, CallbackInfo ci) {
        ((ISuperThrownAbstractArrow)this).roundabout$cancelSuperThrow();
    }

    @Inject(method = "onHitEntity", at = @At(value = "TAIL"),cancellable = true)
    private void roundabout$onHitEntityManhattan(EntityHitResult $$0, CallbackInfo ci) {
        Entity hit = $$0.getEntity();
        if(isManhattanTrident){
            doXTraHattanDmg(hit);
        }
    }

    protected void doXTraHattanDmg(Entity hit){
        DamageSource damageSource;
        ThrownTrident thiz = (ThrownTrident) (Object) this;

        Entity entity2 = thiz.getOwner();
        damageSource = ModDamageTypes.of(thiz.level(), ModDamageTypes.STAND, thiz, entity2);
        float amount = 2;
        float amountBossAndPlayer = 1;
        if(hit instanceof Player || MainUtil.isBossMob(hit)){
            hit.hurt(damageSource, amountBossAndPlayer);
        } else {
            hit.hurt(damageSource, amount);
        }

    }

    boolean isManhattanTrident = false;

    public void roundabout$setIsTridentManhattan(boolean hattan){
        isManhattanTrident = hattan;
    }
}

package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IFatePlayer;
import net.hydra.jojomod.access.IPowersPlayer;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.fates.powers.VampiricFate;
import net.hydra.jojomod.util.HeatUtil;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class FatesLocalPlayerMixin extends Entity {

    @Shadow protected abstract boolean isMoving();

    /**You cannot spawn sprint particles while transforming*/
    @Inject(method = "canSpawnSprintParticle", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$applyEffectTick(CallbackInfoReturnable<Boolean> cir)
    {
        if (((IFatePlayer)this).rdbt$getFatePowers() instanceof VampiricFate VP
        && VP.isPlantedInWall()){
            cir.setReturnValue(this.walkDist > VP.walkDistLast && !this.isInWater() && !this.isSpectator() && !this.isCrouching() && !this.isInLava() && this.isAlive() && !this.isInWater());
            VP.walkDistLast = walkDist;
            return;
        }

        if (((IPowersPlayer)this).rdbt$getPowers().cancelSprintParticles()){
            cir.setReturnValue(false);
        }

            if (FateTypes.isTransforming(((LocalPlayer)(Object)this))||
                    ((IFatePlayer)this).rdbt$getFatePowers().cancelSprintParticles()
    || HeatUtil.isBodyFrozen(this)) {
                cir.setReturnValue(false);
            }
    }
    /**Vampires can sprint even at low food*/
    @Inject(method = "hasEnoughFoodToStartSprinting", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$hasEnoughFoodToStartSprinting(CallbackInfoReturnable<Boolean> cir) {
        if (FateTypes.hasBloodHunger(((LocalPlayer)(Object)this))) {
            cir.setReturnValue(true);
        }
    }

    public FatesLocalPlayerMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}

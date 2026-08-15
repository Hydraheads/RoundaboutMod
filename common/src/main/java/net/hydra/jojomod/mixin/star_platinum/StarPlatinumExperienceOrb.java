package net.hydra.jojomod.mixin.star_platinum;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ExperienceOrb.class)
public abstract class StarPlatinumExperienceOrb extends Entity {

    @Shadow
    private Player followingPlayer;

    /**Inhaled EXP orbs lose gravity to get sucked in easily*/

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {
        if (((IEntityAndData)this).roundabout$getNoGravTicks() > 0){
            ((IEntityAndData)this).roundabout$setNoGravTicks(((IEntityAndData)this).roundabout$getNoGravTicks()-1);
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.03, 0.0));
            }
        }
    }

    @Inject(method = "scanForEntities", at = @At(value = "TAIL"))
    protected void roundabout$scanForEntities(CallbackInfo ci) {
        if (PowerTypes.isInADifferentExistence(followingPlayer,this)){
            followingPlayer = null;
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    public StarPlatinumExperienceOrb(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}

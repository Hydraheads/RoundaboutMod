package net.hydra.jojomod.mixin.star_platinum;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class StarPlatinumItemEntity extends Entity {

    /**This mixin makes items being sucked in by inhale lose gravity*/

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void sba$tick(CallbackInfo ci) {
        if (((IEntityAndData)this).sba$getNoGravTicks() > 0){
            ((IEntityAndData)this).sba$setNoGravTicks(((IEntityAndData)this).sba$getNoGravTicks()-1);
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.04, 0.0));
            }
        }
    }

    @Inject(method = "playerTouch", at = @At(value = "HEAD"), cancellable = true)
    protected void sba$playerTouch(Player $$0,CallbackInfo ci) {
        if (PowerTypes.isInADifferentExistence($$0,this)){
            ci.cancel();
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public StarPlatinumItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}

package net.hydra.jojomod.mixin.star_platinum;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class StarPlatinumItemEntity extends Entity {

    /**This mixin makes items being sucked in by inhale lose gravity*/

    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {
        if (((IEntityAndData)this).roundabout$getNoGravTicks() > 0){
            ((IEntityAndData)this).roundabout$setNoGravTicks(((IEntityAndData)this).roundabout$getNoGravTicks()-1);
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.04, 0.0));
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    public StarPlatinumItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}

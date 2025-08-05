package net.hydra.jojomod.mixin.stand_entities;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public abstract class StandWarden extends Monster {

    /**Stand Entities are not targeted, their users are*/

    @Inject(method = "canTargetEntity", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$canTargetEntity(Entity $$0x, CallbackInfoReturnable<Boolean> cir) {
        if ($$0x instanceof StandEntity) {
            cir.setReturnValue(false);
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    protected StandWarden(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }
}

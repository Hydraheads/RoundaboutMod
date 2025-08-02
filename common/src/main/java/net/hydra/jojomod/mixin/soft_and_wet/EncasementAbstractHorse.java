package net.hydra.jojomod.mixin.soft_and_wet;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class EncasementAbstractHorse extends Animal {
    /**Horses have their own fall damage logic, so this adds bubble encasement logic to it.*/

    @Inject(method = "calculateFallDamage", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$calculateFallDamage(float $$0, float $$1, CallbackInfoReturnable<Integer> cir) {
        StandUser user = ((StandUser) this);
        if (user.roundabout$getLeapTicks() > -1 || user.roundabout$isBubbleEncased()) {
            cir.setReturnValue(0);
            return;
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    protected EncasementAbstractHorse(EntityType<? extends Animal> $$0, Level $$1) {
        super($$0, $$1);
    }
}

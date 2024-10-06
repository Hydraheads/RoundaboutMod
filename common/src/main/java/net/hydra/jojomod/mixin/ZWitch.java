package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.monster.Witch;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Witch.class)
public class ZWitch {
    /**Minor code for stopping witches in a barrage*/

    @Shadow
    private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(Witch.class, EntityDataSerializers.BOOLEAN);

    @Inject(method = "isDrinkingPotion", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$IsDrinking(CallbackInfoReturnable<Boolean> ci) {
        if (((StandUser) this).roundabout$isDazed() ||
                (!((StandUser)this).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this).roundabout$getStandPowers().disableMobAiAttack())) {
            ci.setReturnValue(false);
        }
    }

    @Inject(method = "setUsingItem", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$SetDrinking(boolean $$0, CallbackInfo ci) {
        if (((StandUser) this).roundabout$isDazed() ||
                (!((StandUser)this).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this).roundabout$getStandPowers().disableMobAiAttack())) {
            ((Witch) (Object) this).getEntityData().set(DATA_USING_ITEM, false);
            ci.cancel();
        }
    }
}

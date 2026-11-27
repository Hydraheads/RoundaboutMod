package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.SBA;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class CenturyBoyElytraCancel {



    @Inject(method = "updateFallFlying", at=@At("HEAD"), cancellable = true)
    private void FallFlying(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        if (entity instanceof StandUser user && user.sba$getStandPowers() instanceof Powers20thCenturyBoy PCB) {
            if (PCB.invincibleState) {
                SBA.LOGGER.info("aawdawd");
//                entity.setSharedFlag(7, false);
                ci.cancel();
            }

        }
    }
}
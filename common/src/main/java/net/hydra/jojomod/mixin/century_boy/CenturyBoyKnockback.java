package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.lang.annotation.Target;

@Mixin(LivingEntity.class)
public abstract class CenturyBoyKnockback {


    @Shadow public abstract void knockback(double $$0, double $$1, double $$2);

    @Inject(method = "hurt", at = @At("HEAD"), cancellable = true)
    private void knockbackBoost(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if((Object)this instanceof StandUser user) {
            if (user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy PCB){

                Player player = (Player) (Object)this;

                if (PCB.knockbackStance){
                   player.hurtMarked = true;

                    Entity entity = source.getEntity();
                    if (entity != null) {

                        double x = entity.getX() - player.getX();
                        double y;
                        for(y = entity.getZ() - player.getZ(); x* x + y * y < 1.0E-4; y = (Math.random() - Math.random()) * 0.01) {
                            x = (Math.random() - Math.random()) * 0.01;
                        }
                        knockback((double) 0.8F,x*2,y*2);
                    }

                    cir.setReturnValue(false);
                }
            }
        }
    }
}

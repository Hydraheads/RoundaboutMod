package net.hydra.jojomod.mixin.cream;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersCream;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class CreamCancelPlayerHurtMixin extends LivingEntity {

    protected CreamCancelPlayerHurtMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z", at = @At("HEAD"), cancellable = true)
    public void roundabout$Render(DamageSource $$0, float $$1, CallbackInfoReturnable<Boolean> cir) {
        Player pl = ((Player)(Object)this);

        if (pl instanceof StandUser standUser) {
            if (standUser.roundabout$getStandPowers() instanceof PowersCream cream && cream.insideVoidInt > 0) {
                cir.cancel();
            }
        }
    }
}

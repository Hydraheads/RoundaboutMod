package net.hydra.jojomod.mixin.whitesnake.memory.horse;

import net.hydra.jojomod.event.powers.whitesnake.disc.MemoryPersonality;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class HorseMemoryRavagerRidingMixin {
    @Inject(method = "tickRidden", at = @At("HEAD"))
    private void roundaboutWhitesnake$turnWithRider(Player player, Vec3 travelVector, CallbackInfo ci) {
        if (!roundaboutWhitesnake$isControllableRavager()) return;
        Ravager ravager = (Ravager) (Object) this;
        ravager.setYRot(player.getYRot());
        ravager.setXRot(player.getXRot() * 0.5F);
        ravager.yRotO = ravager.getYRot();
        ravager.yBodyRot = ravager.getYRot();
        ravager.yHeadRot = ravager.getYRot();
    }

    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$useRiderInput(Player player, Vec3 travelVector,
                                                     CallbackInfoReturnable<Vec3> cir) {
        if (!roundaboutWhitesnake$isControllableRavager()) return;
        float forward = player.zza;
        if (forward <= 0.0F) forward *= 0.25F;
        cir.setReturnValue(new Vec3(player.xxa * 0.5F, 0.0D, forward));
    }

    @Inject(method = "getRiddenSpeed", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$useRavagerSpeed(Player player, CallbackInfoReturnable<Float> cir) {
        if (!roundaboutWhitesnake$isControllableRavager()) return;
        Ravager ravager = (Ravager) (Object) this;
        cir.setReturnValue((float) ravager.getAttributeValue(Attributes.MOVEMENT_SPEED));
    }

    private boolean roundaboutWhitesnake$isControllableRavager() {
        if (!((Object) this instanceof Ravager ravager)) return false;
        return MemoryPersonality.hasHorseMemory(ravager)
                && ravager.getFirstPassenger() instanceof Player;
    }
}

package net.hydra.jojomod.mixin.whitesnake.memory.horse;

import net.hydra.jojomod.event.powers.whitesnake.disc.MemoryPersonality;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Saddleable;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Pig.class, Strider.class})
public abstract class HorseMemorySaddledControlMixin {
    @Inject(method = "getControllingPassenger", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$allowSaddledHorseMemoryControl(
            CallbackInfoReturnable<LivingEntity> cir) {
        Mob mob = (Mob) (Object) this;
        Entity passenger = mob.getFirstPassenger();
        if (roundaboutWhitesnake$isControllable(mob) && passenger instanceof Player player) {
            cir.setReturnValue(player);
        }
    }

    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$useHorseMovementInput(Player player, Vec3 travelVector,
                                                             CallbackInfoReturnable<Vec3> cir) {
        Mob mob = (Mob) (Object) this;
        if (!roundaboutWhitesnake$isControllable(mob)) return;
        float forward = player.zza;
        if (forward <= 0.0F) forward *= 0.25F;
        cir.setReturnValue(new Vec3(player.xxa * 0.5F, 0.0D, forward));
    }

    private static boolean roundaboutWhitesnake$isControllable(Mob mob) {
        return MemoryPersonality.hasHorseMemory(mob)
                && mob instanceof Saddleable saddleable && saddleable.isSaddled();
    }
}

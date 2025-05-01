package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public class ZVillagerHostilesSensor {

    @Inject(method = "isHostile", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$customServerAiStep(LivingEntity $$0, CallbackInfoReturnable<Boolean> cir) {
        if ($$0 instanceof Player PE){
            IPlayerEntity ple = ((IPlayerEntity) PE);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                if (ShapeShifts.isZombie(shift)) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
    @Inject(method = "isClose", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$customServerAiStep(LivingEntity $$0, LivingEntity $$1, CallbackInfoReturnable<Boolean> cir) {
        if ($$1 instanceof Player PE){
            cir.setReturnValue($$1.distanceToSqr($$0) <= (double)(64));
        }
    }
}

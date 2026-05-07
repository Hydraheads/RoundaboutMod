package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VillagerHostilesSensor.class)
public abstract class FateVillagerHostilesSensor {

    @Shadow protected abstract boolean isClose(LivingEntity livingEntity, LivingEntity livingEntity2);


    /**Villages consider scary (zombie) players enemies*/
    @Inject(method = "isHostile", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$customServerAiStep(LivingEntity $$0, CallbackInfoReturnable<Boolean> cir) {
        if ($$0 instanceof Player PE){
            IPlayerEntity ple = ((IPlayerEntity) PE);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift == ShapeShifts.PLAYER) {
                if (FateTypes.isScary(PE)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
        }
    }
}

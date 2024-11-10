package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NeutralMob.class)
public interface ZNeutralMob {

    @Inject(method = "isAngryAt", at = @At(value = "HEAD"),cancellable = true)
     default void roundabout$isAngryAt(LivingEntity $$0, CallbackInfoReturnable<Boolean> cir) {
        if ((this instanceof Wolf) && $$0 instanceof Player PE){
            IPlayerEntity ple = ((IPlayerEntity) $$0);
            byte shape = ple.roundabout$getShapeShift();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(shape);
            if (shift != ShapeShifts.PLAYER) {
                if (shift == ShapeShifts.SKELETON) {
                    cir.setReturnValue(true);
                }
            }
        }
    }
}

package net.hydra.jojomod.mixin.soft_and_wet;

import net.hydra.jojomod.access.ILevelAccess;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PiglinAi.class)
public class SoftAndWetPiglinAiMixin {
    /**This mixin is in relation to plundering sound. When sound is plundered from an area, wardens cannot hear it.*/
    @Inject(method = "findNearestValidAttackTarget(Lnet/minecraft/world/entity/monster/piglin/Piglin;)Ljava/util/Optional;", at = @At(value = "HEAD"),cancellable = true)
    private static void roundabout$findNearestValidAttackTarget(Piglin $$0x, CallbackInfoReturnable<Optional<? extends LivingEntity>> cir) {
        if (((StandUser)$$0x).roundabout$getEyeSightTaken() != null && $$0x.getLastHurtByMob() == null){
            cir.setReturnValue(Optional.empty());
        }
    }
}

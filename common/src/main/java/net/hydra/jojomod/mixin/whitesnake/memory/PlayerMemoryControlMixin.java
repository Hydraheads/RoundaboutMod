package net.hydra.jojomod.mixin.whitesnake.memory;

import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.event.powers.whitesnake.disc.MemoryAiController;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMemoryControlMixin {
    @Inject(method = "interactOn(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/InteractionResult;",
            at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$blockInteraction(Entity target, InteractionHand hand,
                                                       CallbackInfoReturnable<InteractionResult> cir) {
        if (!DiscItemData.hasPlayerControl((Player) (Object) this)) cir.setReturnValue(InteractionResult.FAIL);
    }

    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$blockAttack(Entity target, CallbackInfo ci) {
        if (!DiscItemData.hasPlayerControl((Player) (Object) this) && !MemoryAiController.isAiAttack()) ci.cancel();
    }
}

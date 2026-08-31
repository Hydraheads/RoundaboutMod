package net.hydra.jojomod.mixin.whitesnake.memory.horse;

import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public abstract class LobotomizedHorseMovementMixin {
    @Inject(method = "getRiddenInput", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$preventRiddenMovement(Player player, Vec3 movement,
                                                              CallbackInfoReturnable<Vec3> cir) {
        AbstractHorse horse = (AbstractHorse) (Object) this;
        if (DiscItemData.isLobotomized(horse)) cir.setReturnValue(Vec3.ZERO);
    }

    @Inject(method = "getRiddenSpeed", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$preventRiddenSpeed(Player player,
                                                           CallbackInfoReturnable<Float> cir) {
        AbstractHorse horse = (AbstractHorse) (Object) this;
        if (DiscItemData.isLobotomized(horse)) cir.setReturnValue(0.0F);
    }

    @Inject(method = "executeRidersJump", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$preventRiddenJump(float strength, Vec3 movement,
                                                          CallbackInfo ci) {
        AbstractHorse horse = (AbstractHorse) (Object) this;
        if (DiscItemData.isLobotomized(horse)) ci.cancel();
    }
}

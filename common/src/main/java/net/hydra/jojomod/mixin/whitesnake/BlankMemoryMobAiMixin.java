package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.event.powers.disc.MemoryAiController;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class BlankMemoryMobAiMixin {
    @Inject(method = "serverAiStep", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$runBlankMemoryAi(CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (DiscItemData.isLobotomized(mob)) {
            mob.setTarget(null);
            mob.getNavigation().stop();
            ci.cancel();
            return;
        }
        if (!DiscItemData.isBlankMemoryMob(mob)) return;
        MemoryAiController.tickBlankMob(mob);
        mob.getNavigation().tick();
        mob.getMoveControl().tick();
        mob.getLookControl().tick();
        mob.getJumpControl().tick();
        ci.cancel();
    }
}

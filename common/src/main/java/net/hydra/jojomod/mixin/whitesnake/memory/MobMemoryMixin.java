package net.hydra.jojomod.mixin.whitesnake.memory;

import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.whitesnake.disc.DiscItemData;
import net.hydra.jojomod.event.powers.whitesnake.disc.MemoryAiController;
import net.hydra.jojomod.event.powers.whitesnake.disc.MemoryPersonality;
import net.hydra.jojomod.event.powers.whitesnake.disc.WhitesnakeDiscUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMemoryMixin {
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

    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$memoryControlsTarget(LivingEntity target, CallbackInfo ci) {
        Mob mob = (Mob) (Object) this;
        if (target == null) return;
        if (DiscItemData.isBlankMemoryMob(mob)) {
            ci.cancel();
            return;
        }
        byte personality = WhitesnakeDiscUtil.effectivePersonality(mob);
        if ((personality == MemoryPersonality.HOSTILE || personality == MemoryPersonality.ZOMBIE
                || personality == MemoryPersonality.CREEPER)
                && WhitesnakeDiscUtil.canCarrySightDisc(mob)
                && !((DiscBearer) mob).roundabout$hasSightDisc()
                && mob.getLastHurtByMob() == null) {
            double range = mob.getAttributeValue(Attributes.FOLLOW_RANGE) * 0.07D;
            if (mob.distanceToSqr(target) <= range * range) return;
            ci.cancel();
            return;
        }
        if (!WhitesnakeDiscUtil.hasForeignMemory(mob)) return;
        if (personality == MemoryPersonality.HORSE) {
            ci.cancel();
            return;
        }
        if (personality == MemoryPersonality.CREEPER) {
            if (!(mob instanceof Creeper) || !(target instanceof Player)) ci.cancel();
            return;
        }
        if (!((DiscBearer) mob).roundabout$getMemoryTameOwnerId().isEmpty()) {
            if (!MemoryAiController.isTameMemoryTarget(mob, target)) ci.cancel();
            return;
        }
        if (personality == MemoryPersonality.PASSIVE || personality == MemoryPersonality.PLAYER
                || (personality == MemoryPersonality.NEUTRAL && target != mob.getLastHurtByMob())) {
            ci.cancel();
        }
    }

    @Inject(method = "interact", at = @At("RETURN"), cancellable = true)
    private void roundaboutWhitesnake$mountHorseMemoryMob(Player player, InteractionHand hand,
                                                           CallbackInfoReturnable<InteractionResult> cir) {
        Mob mob = (Mob) (Object) this;
        if (cir.getReturnValue() != InteractionResult.PASS
                || !MemoryPersonality.hasHorseMemory(mob)
                || player.isSecondaryUseActive() || !mob.getPassengers().isEmpty()) return;
        if (!mob.level().isClientSide()) player.startRiding(mob);
        cir.setReturnValue(InteractionResult.sidedSuccess(mob.level().isClientSide()));
    }
}

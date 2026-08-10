package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.event.powers.disc.MemoryPersonality;
import net.hydra.jojomod.event.powers.disc.DiscItemData;
import net.hydra.jojomod.access.DiscBearer;
import net.hydra.jojomod.event.powers.disc.WhitesnakeDiscUtil;
import net.hydra.jojomod.event.powers.disc.MemoryAiController;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public abstract class MemoryMobTargeting {
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void roundabout$memoryControlsTarget(LivingEntity target, CallbackInfo ci) {
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
}

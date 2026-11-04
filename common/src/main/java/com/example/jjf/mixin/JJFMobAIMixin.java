
package com.example.jjf.mixin;

import com.example.jjf.entity.projectile.JJFSpitEntity;
import com.example.jjf.stand.powers.PowersJumpinJackFlash;
import com.example.jjf.zero_gravity.ZeroGravityAccess;
import net.hydra.jojomod.event.index.PowerIndex;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PowersJumpinJackFlash.class, remap = false)
public class JJFMobAIMixin {

    @Inject(method = "tickPower", at = @At("TAIL"), remap = false)
    private void jjf$mobAI(CallbackInfo ci) {
        PowersJumpinJackFlash powers =
                (PowersJumpinJackFlash) (Object) this;

        if (!(powers.getSelf() instanceof Mob mob)) {
            return;
        }

        if (mob.level().isClientSide()) {
            return;
        }

        LivingEntity target = mob.getTarget();

        if (target == null || !target.isAlive()) {
            return;
        }

        if (powers.getActivePower() != PowerIndex.NONE) {
            return;
        }

        double distance = mob.distanceTo(target);

        if (distance < 6.0D || distance > 20.0D) {
            return;
        }

        if (target instanceof ZeroGravityAccess access
                && access.jjf$getZeroGravityState().isSpread()) {
            return;
        }

        if (powers.onCooldown(PowerIndex.SKILL_2)) {
            return;
        }

        fireSpit(mob, target);
        powers.setCooldown(PowerIndex.SKILL_2, 80);
    }

    private void fireSpit(Mob mob, LivingEntity target) {
        JJFSpitEntity spit = new JJFSpitEntity(
                mob.level(),
                mob
        );

        Vec3 start = new Vec3(
                mob.getX(),
                mob.getEyeY() - 0.12D,
                mob.getZ()
        );

        Vec3 direction = new Vec3(
                target.getX(),
                target.getEyeY(),
                target.getZ()
        ).subtract(start).normalize();

        start = start.add(direction.scale(0.35D));

        spit.setPos(
                start.x,
                start.y,
                start.z
        );

        spit.shoot(
                direction.x,
                direction.y,
                direction.z,
                1.6F,
                0.6F
        );

        mob.level().addFreshEntity(spit);
    }
}

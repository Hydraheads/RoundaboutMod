package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.jumpingJackFlash.JJFFloatingBlocks;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FallingBlockEntity.class)
public class GravityFallingBlockEntityMixin {
    @Unique
    private int jjf$floatTicks;

    @Unique
    private int jjf$projectileCooldown;

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void jjf$floatBlock(CallbackInfo ci) {
        FallingBlockEntity block = (FallingBlockEntity) (Object) this;
        boolean real = block.getTags().contains(JJFFloatingBlocks.REAL_TAG);
        boolean fake = block.getTags().contains(JJFFloatingBlocks.FAKE_TAG);
        if (!real && !fake) {
            return;
        }
        if (block.level().isClientSide()) {
            return;
        }
        jjf$floatTicks++;
        if (jjf$floatTicks >= 20 * 10) {
            if (fake) {
                block.discard();
            } else {
                block.removeTag(JJFFloatingBlocks.REAL_TAG);
                block.setNoGravity(false);
            }
            ci.cancel();
            return;
        }

        if (jjf$projectileCooldown > 0) {
            jjf$projectileCooldown--;
        } else {
            for (Projectile projectile : block.level().getEntitiesOfClass(Projectile.class,
                    block.getBoundingBox().inflate(0.35D),
                    p -> p.isAlive() && p.getDeltaMovement().lengthSqr() > 0.0025D)) {
                Vec3 incoming = projectile.getDeltaMovement();
                if (JJFFloatingBlocks.knockBack(block, incoming, 0.35D + Math.min(0.8D, incoming.length() * 0.55D))) {
                    jjf$projectileCooldown = 5;
                    break;
                }
            }
        }
        block.setNoGravity(true);
        Vec3 velocity = block.getDeltaMovement();
        Vec3 next = new Vec3(velocity.x * 0.992D,
                Math.min(0.20D, velocity.y * 0.992D + 0.001D),
                velocity.z * 0.992D);
        block.setDeltaMovement(next);
        block.move(MoverType.SELF, next);
        block.hasImpulse = true;
        block.hurtMarked = true;
        ci.cancel();
    }

    @Inject(method = "tick", at = @At(value = "TAIL"))
    protected void roundabout$tick(CallbackInfo ci) {
        ((IEntityAndData)this).roundabout$universalTick();
        ((IEntityAndData)this).roundabout$tickQVec();
    }
}

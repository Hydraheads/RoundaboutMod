package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IParticleAccess;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractArrow.class)
public abstract class ZAbstractArrow extends Entity {

    @Shadow
    protected boolean canHitEntity(Entity $$0x){
        return false;
    }

    public ZAbstractArrow(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    /**Negates the gravity modifier in TS, so that the function beneath can do it itself.*/
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setDeltaMovement(DDD)V"))
    private void roundaboutSetDeltaWithoutGravity(AbstractArrow arrow, double x, double y, double z) {
        if (((TimeStop) arrow.level()).inTimeStopRange(arrow) && ((IProjectileAccess) this).getRoundaboutIsTimeStopCreated()) {
            Vec3 delta = arrow.getDeltaMovement();
            arrow.setDeltaMovement(delta.x,delta.y,delta.z);
        } else {
            arrow.setDeltaMovement(x,y,z);
        }
    }
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;setPos(DDD)V",shift = At.Shift.BEFORE), cancellable = true)
    private void roundaboutSetPosForTS(CallbackInfo ci) {
        if (((TimeStop) ((AbstractArrow)(Object)this).level()).inTimeStopRange(((AbstractArrow)(Object)this)) && ((IProjectileAccess) this).getRoundaboutIsTimeStopCreated()) {
            float speedMod = ((IProjectileAccess) this).getRoundaboutSpeedMultiplier();
            Vec3 position = new Vec3(((AbstractArrow)(Object)this).getX(),((AbstractArrow)(Object)this).getY(),((AbstractArrow)(Object)this).getZ());

            Vec3 reducedDelta = ((AbstractArrow)(Object)this).getDeltaMovement();
            reducedDelta =  reducedDelta.multiply(speedMod,speedMod,speedMod);

            Vec3 pos = position();
            Vec3 pos2 = position().add(this.getDeltaMovement()).add(reducedDelta);
            float inflateDist = (float) Math.max(Math.max(reducedDelta.x, reducedDelta.y), reducedDelta.z);
            HitResult mobHit =  ProjectileUtil.getEntityHitResult(
                    this.level(), this, pos, pos2, this.getBoundingBox().expandTowards(this.getDeltaMovement()).inflate(1+inflateDist), this::canHitEntity
            );
            if (mobHit != null){
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.4,0.4,0.4));
            }

            if (speedMod > 0.01) {
                ((IProjectileAccess) this).setRoundaboutSpeedMultiplier((float) (speedMod*= 0.85F));
                ((AbstractArrow)(Object)this).setPos(position.x + reducedDelta.x, position.y + reducedDelta.y, position.z + reducedDelta.z);
            } else {
                ((IProjectileAccess) this).setRoundaboutIsTimeStopCreated(false);
            }
            this.checkInsideBlocks();
            ci.cancel();
        }
    }
}

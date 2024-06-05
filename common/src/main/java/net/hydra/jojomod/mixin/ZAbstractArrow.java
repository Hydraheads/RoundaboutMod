package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IAbstractArrowAccess;
import net.hydra.jojomod.access.IParticleAccess;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.entity.TimeMovingProjectile;
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
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractArrow.class)
public abstract class ZAbstractArrow extends Entity implements IAbstractArrowAccess {
    /**Makes the arrows, knives, and tridents thrown in timestop */

    @Shadow
    protected boolean canHitEntity(Entity $$0x){
        return false;
    }

    public ZAbstractArrow(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    protected boolean inGround;

    @Override
    public boolean roundaboutGetInGround(){
        return this.inGround;
    }

    @Override
    public void roundaboutSetInGround(boolean inGround){
        this.inGround = inGround;
    }

    public byte roundaboutGetPierceLevel(){
        return this.getPierceLevel();
    }
    @Shadow
    public byte getPierceLevel(){
        return 0;
    }
    @Override
    @Nullable
    public EntityHitResult roundaboutFindHitEntity(Vec3 $$0, Vec3 $$1){
        return this.findHitEntity($$0,$$1);
    }
    @Shadow
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 $$0, Vec3 $$1){
        return null;
    }
    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    private void roundaboutSetPosForTS(CallbackInfo ci) {
        if (((TimeStop)this.level()).inTimeStopRange(((AbstractArrow)(Object)this)) && ((IProjectileAccess) this).getRoundaboutIsTimeStopCreated()) {
            super.tick();
            TimeMovingProjectile.tick((AbstractArrow) (Object) this);
            this.checkInsideBlocks();
            ci.cancel();
        }
    }
}

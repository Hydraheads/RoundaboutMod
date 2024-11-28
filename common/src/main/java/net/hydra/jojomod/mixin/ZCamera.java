package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.ICamera;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.Camera;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class ZCamera implements ICamera {


    @Shadow
    private Entity entity;
    @Shadow
    private BlockGetter level;

    @Unique
    private Entity roundabout$povSwitch;
    @Override
    @Unique
    public void roundabout$setEntity(Entity entity){
        if (roundabout$povSwitch != entity) {
            this.roundabout$povSwitch = entity;
        }
    }
    @Shadow
    public void setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f) {}


    @Shadow private boolean initialized;

    @Shadow private boolean detached;

    @Shadow protected abstract void setRotation(float $$0, float $$1);

    @Shadow protected abstract void setPosition(Vec3 $$0);
    @Shadow protected abstract void setPosition(double d, double e, double f);

    @Shadow protected abstract void move(double $$0, double $$1, double $$2);

    @Shadow protected abstract double getMaxZoom(double $$0);

    @Shadow private float yRot;

    @Shadow private float xRot;

    @Shadow private float eyeHeight;

    @Shadow private float eyeHeightOld;


    @Unique
    public float roundabout$getViewXRot(Entity ent, float $$0) {
        return $$0 == 1.0F ? ent.getXRot() : MainUtil.controlledLerpAngleDegrees(ClientUtil.getFrameTime(),ent.xRotO,ent.getXRot(),1);
    }

    @Unique
    public float roundabout$getViewYRot(Entity ent, float $$0) {
        return $$0 == 1.0F ? ent.getYRot() : MainUtil.controlledLerpAngleDegrees(ClientUtil.getFrameTime(),ent.yRotO,ent.getYRot(),1);
    }
    @Unique
    public void roundabout$setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f) {
        this.initialized = true;
        this.level = blockGetter;
        this.entity = entity;
        this.detached = true;
        this.setRotation(roundabout$getViewYRot(entity,f),roundabout$getViewXRot(entity,f));
        double xx = Mth.lerp((double)f, roundabout$povSwitch.xo, roundabout$povSwitch.getX());
        double yy = Mth.lerp((double)f, roundabout$povSwitch.yo, roundabout$povSwitch.getY()) +
                (double)Mth.lerp(f, this.eyeHeightOld, this.eyeHeight);
        double zz = Mth.lerp((double)f, roundabout$povSwitch.zo, roundabout$povSwitch.getZ());
        this.setPosition(xx, yy, zz);
        if (bl) {
            if (bl2) {
                this.setRotation(this.yRot + 180.0f, -this.xRot);
            }
            this.move(-this.getMaxZoom(4.0), 0.0, 0.0);
        } else if (entity instanceof LivingEntity && ((LivingEntity)entity).isSleeping()) {
            Direction direction = ((LivingEntity)entity).getBedOrientation();
            this.setRotation(direction != null ? direction.toYRot() - 180.0f : 0.0f, 0.0f);
            this.move(0.0, 0.3, 0.0);
        }
    }

    @Unique
    public boolean roundabout$cleared = false;
    @Inject(method = "setup", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        if (!roundabout$cleared) {
            if (entity != null && ((TimeStop) entity.level()).CanTimeStopEntity(entity)) {
                    f = ((IEntityAndData) entity).roundabout$getPreTSTick();
                    roundabout$cleared = true;
                    this.setup(blockGetter, entity, bl, bl2, f);
                    roundabout$cleared = false;
                ci.cancel();
                return;
            }
        }

        if (roundabout$povSwitch != null && entity != null && !roundabout$povSwitch.is(entity)){
            if (roundabout$povSwitch.isAlive() && !roundabout$povSwitch.isRemoved()){
                roundabout$setup(blockGetter, entity, bl, bl2, f);
                ci.cancel();
                return;
            } else {
                roundabout$povSwitch = null;
            }
        } else {
            roundabout$povSwitch = null;
        }
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$tick(CallbackInfo ci) {
        if (roundabout$povSwitch != null && !(entity !=null && roundabout$povSwitch.is(entity))){
            this.eyeHeightOld = this.eyeHeight;
            this.eyeHeight += (this.roundabout$povSwitch.getEyeHeight() - this.eyeHeight) * 0.5f;
            ci.cancel();
        }
    }

}

package net.hydra.jojomod.mixin.items;

import net.hydra.jojomod.access.ICamera;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.Poses;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class ChairCameraMixin implements ICamera {
    @Shadow private boolean initialized;
    @Shadow private Entity entity;
    @Shadow private float eyeHeightOld;
    @Shadow private float eyeHeight;

    @Shadow
    protected abstract void setPosition(double x, double y, double z);

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void roundabout$tick(CallbackInfo ci) {
        if (entity instanceof Player player) {
            IPlayerEntity ipe = (IPlayerEntity) player;
            byte poseEmote = ipe.roundabout$GetPoseEmote();
            if (poseEmote == Poses.SITTING.id) {
                this.eyeHeightOld = this.eyeHeight;
                this.eyeHeight += (this.entity.getEyeHeight() - this.eyeHeight) * 0.5F;
                ci.cancel();
            }
        }
    }

    @Inject(method = "setup", at = @At("RETURN"))
    private void roundabout$setup(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (entity instanceof Player player) {
            IPlayerEntity ipe = (IPlayerEntity) player;
            byte poseEmote = ipe.roundabout$GetPoseEmote();
            if (poseEmote == Poses.SITTING.id) {
                Camera self = (Camera)(Object)this;
                Vec3 pos = self.getPosition();
                double loweredY = pos.y - (player.getEyeHeight() * 0.35F);
                this.setPosition(pos.x, loweredY, pos.z);
            }
        }
    }

}

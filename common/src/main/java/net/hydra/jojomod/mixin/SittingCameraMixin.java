package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.Poses;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.text.Position;

@Mixin(Camera.class)
public abstract class SittingCameraMixin {
    @Shadow protected abstract void setPosition(Vec3 pos);

    @SuppressWarnings("deprecation")
    @Inject(method = "setup", at = @At("TAIL"))
    private void sittingFixClipping(BlockGetter $$0, Entity $$1, boolean $$2, boolean $$3, float $$4, CallbackInfo ci) {
        if ($$1 instanceof Player player && player instanceof IPlayerEntity ipe) {
            if (ipe.roundabout$GetPoseEmote() == Poses.SITTING.id) {
                Camera self = (Camera)(Object)this;
                Vec3 fixpos = self.getPosition().add(0.0D, 0.40D, 0.0D);
                if (ClientUtil.checkIfFirstPerson()) {
                    this.setPosition(fixpos);
                }
            }
        }
    }
}

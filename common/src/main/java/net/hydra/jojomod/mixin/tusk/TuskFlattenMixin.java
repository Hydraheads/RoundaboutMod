package net.hydra.jojomod.mixin.tusk;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersTusk;
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
public abstract class TuskFlattenMixin {
    @Shadow
    protected abstract void setPosition(Vec3 vec3);

    @Inject(method = "setup",at = @At("TAIL"))
    private void roundabout$flattenCamera(BlockGetter $$0, Entity $$1, boolean $$2, boolean $$3, float partialTicks, CallbackInfo ci) {
        if ($$1 instanceof Player P) {
            StandUser SU = (StandUser) P;
            if (SU.roundabout$getStandPowers() instanceof PowersTusk PT && PT.flattenTicks > 0) {
                if (PT.getActivePower() != PowersTusk.FLATTEN) {partialTicks *= -1;}

                if (ClientUtil.checkIfFirstPerson()) {
                    Camera self = (Camera) (Object) this;
                    Vec3 fixpos = self.getPosition().add(0.0,  -1 * Math.min(1,(PT.flattenTicks+partialTicks)/5 ), 0.0);
                    this.setPosition(fixpos);
                }
            }
        }
    }

    //                Camera self = (Camera)(Object)this;
    //                Vec3 fixpos = self.getPosition().add(0.0, 0.40, 0.0);
    //                if (ClientUtil.checkIfFirstPerson()) {
    //                    this.setPosition(fixpos);
    //                }
}

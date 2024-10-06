package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class ZCamera {
    @Shadow
    public void setup(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f) {}


    public boolean roundabout$cleared = false;
    @Inject(method = "setup", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$BobView(BlockGetter blockGetter, Entity entity, boolean bl, boolean bl2, float f, CallbackInfo ci) {
        if (!roundabout$cleared) {
            if (entity != null && ((TimeStop) entity.level()).CanTimeStopEntity(entity)) {
                    f = ((IEntityAndData) entity).roundabout$getPreTSTick();
                    roundabout$cleared = true;
                    this.setup(blockGetter, entity, bl, bl2, f);
                    roundabout$cleared = false;
                ci.cancel();
            }
        }
    }
}

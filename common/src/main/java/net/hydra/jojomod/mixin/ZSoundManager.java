package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.ILevelAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SoundManager.class)
public class ZSoundManager {

    @Inject(method = "play", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$play(SoundInstance $$0, CallbackInfo ci) {
        if (Minecraft.getInstance().player != null){
            ILevelAccess access = ((ILevelAccess)Minecraft.getInstance().player.level());
            if(access.roundabout$isSoundPlundered(new BlockPos((int) $$0.getX(), (int) $$0.getY(), (int) $$0.getZ()))){
                ci.cancel();
            }
        }
    }
}

package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value= PrimaryLevelData.class)
public class ExperimentalFixD4CForge {
    //Skip experimental warning
    @Inject(method = "hasConfirmedExperimentalWarning()Z", at = @At("HEAD"), cancellable = true,remap=false)
    private void roundabout$askForBackup(CallbackInfoReturnable<Boolean> cir) {
        if (ConfigManager.getClientConfig().disableObviousExperimentalWarning){
            cir.setReturnValue(true);
        }
    }
}

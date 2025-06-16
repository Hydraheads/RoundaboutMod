package net.hydra.jojomod.mixin.fabric;

import net.hydra.jojomod.util.config.ConfigManager;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldOpenFlows;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value= WorldOpenFlows.class, priority = 100)
public abstract class ExperimentalFixD4CFabric {


    //Skip experimental warning
    //Ignore the squiggly lines,the compiler literally does not know how to count local variables
    @ModifyVariable(method = "doLoadLevel(Lnet/minecraft/client/gui/screens/Screen;Ljava/lang/String;ZZ)V", at = @At(value = "STORE"),
            ordinal=3)
    private boolean roundabout$experimental(boolean value,Screen screen, String string, boolean bl, boolean bl2) {
        if (ConfigManager.getClientConfig().disableObviousExperimentalWarning){
            return false;
        }
        return value;
    }
}

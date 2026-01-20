package net.hydra.jojomod.mixin.forge;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.index.AnubisMoment;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ToggleKeyMapping;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ToggleKeyMapping.class)
public abstract class AnubisForgeToggleKeyMapping {


    ///  forge overrides isDown inside of ToggleKeyMapping so...
    @Inject(method = "isDown",at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$forgeToggleKeyForcePressed(CallbackInfoReturnable<Boolean> cir) {
        ToggleKeyMapping This = (ToggleKeyMapping) (Object) this;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        StandUser SU = (StandUser) player;
        if (SU != null) {
            if (SU.roundabout$getStandPowers() != null) {
                if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
                    if (SU.roundabout$getUniqueStandModeToggle()) {
                        int time = PA.getMaxPlayTime() - PA.playTime;
                        for (int i = 0; i < PA.playKeys.size(); i++) {
                            KeyMapping key = PA.playKeys.get(i);
                            Byte byt = PA.playBytes.get(i);

                            if (byt == AnubisMoment.SPRINT || byt == AnubisMoment.CROUCH) {
                                if (key.getName().equals(This.getName())) {
                                    if (PA.isPressed(byt, time)) {
                                        cir.setReturnValue(true);
                                        cir.cancel();
                                    }
                                }
                            }



                        }
                    }
                }
            }
        }
    }

}

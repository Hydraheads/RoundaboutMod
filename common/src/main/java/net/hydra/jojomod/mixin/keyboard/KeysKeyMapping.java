package net.hydra.jojomod.mixin.keyboard;

import com.mojang.blaze3d.platform.InputConstants;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.gui.NoCancelInputScreen;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(KeyMapping.class)
public abstract class KeysKeyMapping implements IKeyMapping {

    @Shadow
    public abstract String getName();

    /**No cancel input screens let you continue walking and pressing buttons like jump while in a gui,
     * they are important for active combat selection like the soft and wet bubble menu*/


    @Inject(method = "releaseAll()V", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$KP1(CallbackInfo ci) {
        Minecraft placingBlocksAndSh = Minecraft.getInstance();

        if (placingBlocksAndSh.screen instanceof NoCancelInputScreen){
            ci.cancel();
            for (KeyMapping $$0 : ALL.values()) {
                if ($$0.same(KeyInputRegistry.pose)){
                    ((IKeyMapping)$$0).roundabout$release();
                }
            }
        }
    }

    @Unique
    @Override
    public InputConstants.Key roundabout$justTellMeTheKey(){
        return key;
    }
    @Unique
    @Override
    public void roundabout$release(){
        release();
    }

    @Inject(method = "isDown",at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$forcePressed(CallbackInfoReturnable<Boolean> cir) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        StandUser SU = (StandUser) player;
        if (SU.roundabout$getStandPowers() != null) {
            if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA) {
                if (SU.roundabout$getUniqueStandModeToggle()) {
                    int time = PowersAnubis.MaxPlayTime-PA.playTime;
                    for(int i=0;i<PA.playKeys.size();i++) {
                        KeyMapping key = PA.playKeys.get(i);

                        if (key.getName().equals(this.getName())) {
                            if (PA.isPressed(PA.playBytes.get(i), time)) {
                                cir.setReturnValue(true);
                                cir.cancel();
                            }
                        } else {/// admittedly a little scuffed, I'll change it if it breaks
                            if (PA.isPressed(PA.playBytes.get(i), time) && PA.playBytes.get(i) > 20) {
                                player.getInventory().selected = ((int)PA.playBytes.get(i))-21;
                            }
                        }

                    }
                }
            }
        }
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    private static Map<String, KeyMapping> ALL;

    @Shadow
    private void release(){
    }
    @Shadow
    private InputConstants.Key key;

}

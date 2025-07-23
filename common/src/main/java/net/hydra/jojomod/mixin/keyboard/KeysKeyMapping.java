package net.hydra.jojomod.mixin.keyboard;

import com.mojang.blaze3d.platform.InputConstants;
import net.hydra.jojomod.access.IKeyMapping;
import net.hydra.jojomod.client.KeyInputRegistry;
import net.hydra.jojomod.client.gui.NoCancelInputScreen;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(KeyMapping.class)
public class KeysKeyMapping implements IKeyMapping {

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

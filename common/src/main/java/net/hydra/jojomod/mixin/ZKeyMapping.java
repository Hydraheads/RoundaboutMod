package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.platform.InputConstants;
import net.hydra.jojomod.Roundabout;
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
public class ZKeyMapping implements IKeyMapping {
    @Shadow
    private InputConstants.Key key;

    @Unique
    @Override
    public InputConstants.Key roundabout$justTellMeTheKey(){
        return key;
    }

    @Shadow
    @Final
    private static Map<String, KeyMapping> ALL;

    @Shadow
    private void release(){

    }

    @Unique
    @Override
    public void roundabout$release(){
        release();
    }
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
}

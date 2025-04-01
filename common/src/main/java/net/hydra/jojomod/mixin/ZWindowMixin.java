package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.platform.Window;
import net.hydra.jojomod.access.IGameRenderer;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public class ZWindowMixin {
    @Unique private int roundabout$frameCount = 0;

    // test for timestop screen freezing
//    @Inject(method="updateDisplay", at=@At("HEAD"), cancellable = true)
//    private void roundabout$updateDisplay(CallbackInfo ci)
//    {
//        Minecraft client = Minecraft.getInstance();
//        if (client.level != null)
//        {
//            if (((TimeStop)client.level).inTimeStopRange(client.player))
//            {
//                    ci.cancel();
//            }
//            else
//                roundabout$frameCount = 0;
//        }
//    }
}

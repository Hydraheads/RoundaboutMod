package net.hydra.jojomod.mixin;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ResourceLocation.class)
public class DimensionResourceKeyD4C {
//    @Shadow @Final private String path;
//
//    @Shadow @Final private String namespace;
//
//    @Inject(method = "toString", at=@At("HEAD"), cancellable = true)
//    /** makes it say minecraft:overworld as the dimension name for D4C worlds (for F3) */
//    private void roundabout$location(CallbackInfoReturnable<String> cir)
//    {
//        if (this.path.startsWith("d4c-") && this.namespace.equals("roundabout"))
//        {
//            cir.setReturnValue("minecraft:overworld");
//            cir.cancel();
//        }
//    }
}

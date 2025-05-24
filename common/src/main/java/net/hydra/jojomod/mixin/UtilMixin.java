package net.hydra.jojomod.mixin;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Util.class)
public class UtilMixin {
    /**Cancel data fixing logging*/
    @Inject(method = "doFetchChoiceType(Lcom/mojang/datafixers/DSL$TypeReference;Ljava/lang/String;)Lcom/mojang/datafixers/types/Type;", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$doFetchChoiceType(DSL.TypeReference $$0, String $$1, CallbackInfoReturnable<Type<?>> cir) {
        if ($$1.contains("roundabout") || $$1.contains("stereo") || $$1.contains("stand_fire") || $$1.contains("mirror")
                || $$1.contains("bubble_scaffold")){
            cir.setReturnValue(null);
        }
    }
}

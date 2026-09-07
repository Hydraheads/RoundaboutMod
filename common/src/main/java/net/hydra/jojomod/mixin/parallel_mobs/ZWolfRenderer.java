package net.hydra.jojomod.mixin.parallel_mobs;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ISheep;
import net.hydra.jojomod.access.IWolf;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfRenderer.class)
public class ZWolfRenderer {
    private final ResourceLocation RDBT$ALT_A = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/parallel_mobs/wolf_1.png");
    private final ResourceLocation RDBT$ALT_B = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/parallel_mobs/wolf_2.png");
    private final ResourceLocation RDBT$ALT_C = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/parallel_mobs/wolf_3.png");
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "HEAD"),cancellable = true)
    protected void roundabout$getTextureLocation(Wolf $$0, CallbackInfoReturnable<ResourceLocation> cir) {
        if ($$0 != null) {
            IWolf isheep = ((IWolf) $$0);
            byte ip2 = isheep.roundabout$isAlt();
            if (ip2 > 0){
                if (ip2 > 2){
                    cir.setReturnValue(RDBT$ALT_C);
                } else if (ip2 > 1){
                    cir.setReturnValue(RDBT$ALT_B);
                } else {
                    cir.setReturnValue(RDBT$ALT_A);
                }
            }
        }
    }
}

package net.hydra.jojomod.mixin.parallel_mobs;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPig;
import net.hydra.jojomod.access.ISheep;
import net.minecraft.client.renderer.entity.SheepRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.entity.animal.Sheep;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SheepRenderer.class)
public class ZSheepRenderer {

    private final ResourceLocation RDBT$ALT_A = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/parallel_mobs/sheep_1.png");
    private final ResourceLocation RDBT$ALT_B = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/parallel_mobs/sheep_2.png");
    private final ResourceLocation RDBT$ALT_C = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/parallel_mobs/sheep_3.png");
    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Sheep;)Lnet/minecraft/resources/ResourceLocation;",
            at = @At(value = "HEAD"),cancellable = true)
    protected void roundabout$getTextureLocation(Sheep $$0, CallbackInfoReturnable<ResourceLocation> cir) {
        if ($$0 != null) {
            ISheep isheep = ((ISheep) $$0);
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

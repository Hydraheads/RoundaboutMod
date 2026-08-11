package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.client.HallucinationRenderOffset;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public abstract class HallucinationEntityRendererMixin<T extends Entity> {
    @Inject(method = "getRenderOffset", at = @At("RETURN"), cancellable = true)
    private void roundaboutWhitesnake$misplaceHallucinations(T rendered, float partialTick,
                                                             CallbackInfoReturnable<Vec3> cir) {
        cir.setReturnValue(cir.getReturnValue().add(HallucinationRenderOffset.forEntity(rendered)));
    }
}

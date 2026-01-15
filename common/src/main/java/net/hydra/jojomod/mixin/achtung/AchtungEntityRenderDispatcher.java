package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class AchtungEntityRenderDispatcher {

    /***
     * While this is marked as an Achtung Baby class, it is certainly wise to re-use
     * it for anything pertaining to living entity transparency. Note that nonliving
     * entities aren't reliable in their ability to be partially see through which is the
     * reason a lot of these mixins exist, but all standard living entities have the
     * capacity to be see-through because vanilla uses scoreboard team invisibility on them
     */
    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "HEAD"), cancellable = true)
    protected <E extends Entity>  void roundabout$render(E entity, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int light, CallbackInfo ci) {
        float throwFadeToTheEther = ClientUtil.getThrowFadePercent(entity,$$5);

        /**Insert code for other partially visible stands here, this is mirrored across visage
         * parts and armor rendering.*/


        ClientUtil.setThrowFadeToTheEther(throwFadeToTheEther);
    }
    @Inject(method = "render(Lnet/minecraft/world/entity/Entity;DDDFFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "TAIL"))
    protected <E extends Entity>  void roundabout$renderTail(E entity, double $$1, double $$2, double $$3, float $$4, float $$5, PoseStack $$6, MultiBufferSource $$7, int light, CallbackInfo ci) {

        ClientUtil.setThrowFadeToTheEther(1);
    }
}

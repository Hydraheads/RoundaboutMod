package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class AchtungLevelRenderer {


    /**
     * Code for Achtung Baby entity rendering, the roundabout$renderEntity part
     * is to cancel rendering on nonliving entities, living entities
     * already have partial rendering canceling when the isInvisible function
     * returns true in the entity class
     *
     * roundabout$renderEntityEnd is for resetting the invisibility value in case a mod cancels
     * the entity rendering, we don't want to accidentally make random things transparent
     * */

    @Inject(method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "HEAD"),
            cancellable = true)
    private void roundabout$renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float partialTick, PoseStack stack, MultiBufferSource buffer, CallbackInfo ci) {
        if (entity != null){
            IEntityAndData entityAndData = ((IEntityAndData)entity);
            if (!(entity instanceof LivingEntity) && entityAndData.roundabout$getTrueInvisibility() > -1 && !ClientUtil.checkIfClientCanSeeInvisAchtung()){
                /**A side effect of canceling rendering like this is for Achtung on non living entities is they do not
                 * render flames while invisible, this would be an easy fix but I don't know if it is necessary.
                 * LivingEntities use Entity function getInvisible entirely.*/
                ci.cancel();
                return;
            }
        }
    }
    @Inject(method = "renderEntity(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;)V",
            at = @At(value = "TAIL"))
    private void roundabout$renderEntityEnd(Entity entity, double cameraX, double cameraY, double cameraZ,  float partialTick, PoseStack stack, MultiBufferSource buffer, CallbackInfo ci) {
        ClientUtil.setThrowFadeToTheEther(1F);
        ClientUtil.saveBufferTexture = null;
    }
}

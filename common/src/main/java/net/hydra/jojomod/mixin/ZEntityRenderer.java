package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.stand.renderers.AbstractDisguiseRenderer;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class ZEntityRenderer<T extends Entity> {

    /**Cancel hitbox rendering for stuff like go beyond*/
    @Inject(method = "shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z", at = @At(value = "HEAD"), cancellable = true)
    private <E extends Entity> void rdbt$renderHitbox(T $$0, Frustum $$1, double $$2, double $$3, double $$4, CallbackInfoReturnable<Boolean> cir) {
        if (ClientUtil.forceEntityRendering($$0)){
            cir.setReturnValue(true);
        }
    }

    /**Cancel original nametag when disguised*/
    @Inject(method = "renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void rdbt$cancelNameTagWhenDisguised(T entity, Component displayName, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof StandUser su && su.roundabout$isDisguised()) {
            com.mojang.authlib.GameProfile profile = su.roundabout$getDisguiseProfile();
            // compares nametag to the disguised nametag, if it's not the same that it gets deleted
            if (profile != null && profile.getName() != null) {
                if (!displayName.getString().equals(profile.getName())) {
                    ci.cancel();
                }
            }
        }
    }
}
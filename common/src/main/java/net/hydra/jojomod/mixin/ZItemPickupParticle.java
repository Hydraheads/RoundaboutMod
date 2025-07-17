package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.stand.powers.PowersAchtungBaby;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ItemPickupParticle.class)
public class ZItemPickupParticle {

    @Shadow @Final private Entity target;
    @Shadow @Final private Entity itemEntity;
    @Unique
    public boolean roundabout$invisibilityPickedUp = false;
    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$renderHead(VertexConsumer $$0, Camera $$1, float $$2, CallbackInfo ci) {
        if (roundabout$invisibilityPickedUp){
            /**Achtung users with invis vision are the only ones that can see items being picked up by achtung invis
             * players*/
            if (!ClientUtil.checkIfClientCanSeeInvisAchtung()){
                ci.cancel();
            } else {
                if (target != null) {

                    IEntityAndData entityAndData = ((IEntityAndData) target);
                    if (entityAndData.roundabout$getTrueInvisibility() > -1){
                        roundabout$invisibilityPickedUp = true;
                        if (itemEntity != null){
                            ((IEntityAndData)itemEntity).roundabout$setTrueInvisibility(entityAndData.roundabout$getTrueInvisibility());
                        }
                    }


                    float throwFadeToTheEther = 1f;
                    if (entityAndData.roundabout$getTrueInvisibility() > -1) {
                        throwFadeToTheEther = throwFadeToTheEther * 0.4F;
                    }
                    ClientUtil.setThrowFadeToTheEther(throwFadeToTheEther);
                }
            }
        }
    }
    @Inject(method = "render", at = @At(value = "TAIL"))
    protected void roundabout$renderTail(VertexConsumer $$0, Camera $$1, float $$2, CallbackInfo ci) {
        ClientUtil.setThrowFadeToTheEther(1);
    }
    @Inject(method = "getRenderType()Lnet/minecraft/client/particle/ParticleRenderType;", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getRenderType(CallbackInfoReturnable<ParticleRenderType> cir) {
        if (roundabout$invisibilityPickedUp){
            cir.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
        }
    }
    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;Lnet/minecraft/client/renderer/RenderBuffers;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "TAIL"))
    protected void roundabout$init(EntityRenderDispatcher $$0, RenderBuffers $$1, ClientLevel $$2, Entity $$3, Entity $$4, CallbackInfo ci) {
        if ($$4 != null && ((IEntityAndData)$$4).roundabout$getTrueInvisibility() > -1){
            roundabout$invisibilityPickedUp = true;
            if ($$3 != null){
                ((IEntityAndData)$$3).roundabout$setTrueInvisibility(((IEntityAndData)$$4).roundabout$getTrueInvisibility());
            }
        }
    }

}

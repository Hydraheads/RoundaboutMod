package net.hydra.jojomod.mixin.achtung;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ItemPickupParticle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemPickupParticle.class)
public class AchtungItemPickupParticle {

    /***
     * Code for Achtung Baby Item Pickup Rendering!
     * The game renders items as particles as they are being picked up, this
     * makes them see through (unless on fabulous)
     *
     * There is a packet sent to the client to spawn these particles, which exists in init.
     * When the entity the particle is going towards is invisible, it hides them by
     * marking the entity as invisible
     *
     * REMOVED FROM THE CODE
     * It causes bizarre rendering bugs with particles, making them turn pitch black, and it does
     * not have to do with throwfade to ether. So it is now worth keeping in.
     */
    @Unique
    public boolean roundabout$invisibilityPickedUp = false;
    @Inject(method = "render", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$renderHead(VertexConsumer $$0, Camera $$1, float $$2, CallbackInfo ci) {
        //if (roundabout$invisibilityPickedUp){
            /**Achtung users with invis vision are the only ones that can see items being picked up by achtung invis
             * players*/

            /**
            if (!ClientUtil.checkIfClientCanSeeInvisAchtung() || ClientUtil.isFabulous()){
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
                    return;
                }
            }
        }
        ClientUtil.setThrowFadeToTheEther(1);
             **/
    }
    @Inject(method = "render", at = @At(value = "TAIL"))
    protected void roundabout$renderTail(VertexConsumer $$0, Camera $$1, float $$2, CallbackInfo ci) {
        //ClientUtil.setThrowFadeToTheEther(1);
    }
    @Inject(method = "getRenderType()Lnet/minecraft/client/particle/ParticleRenderType;", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getRenderType(CallbackInfoReturnable<ParticleRenderType> cir) {
        //if (roundabout$invisibilityPickedUp && !ClientUtil.isFabulous()){
            //cir.setReturnValue(ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT);
        //}
    }
    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;Lnet/minecraft/client/renderer/RenderBuffers;Lnet/minecraft/client/multiplayer/ClientLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/entity/Entity;)V", at = @At(value = "TAIL"))
    protected void roundabout$init(EntityRenderDispatcher $$0, RenderBuffers $$1, ClientLevel $$2, Entity $$3, Entity $$4, CallbackInfo ci) {
        //if ($$4 != null && ((IEntityAndData)$$4).roundabout$getTrueInvisibility() > -1){
            //roundabout$invisibilityPickedUp = true;
           // if ($$3 != null){
               // ((IEntityAndData)$$3).roundabout$setTrueInvisibility(((IEntityAndData)$$4).roundabout$getTrueInvisibility());
            //}
        //}
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow @Final private Entity target;
    @Shadow @Final private Entity itemEntity;

}

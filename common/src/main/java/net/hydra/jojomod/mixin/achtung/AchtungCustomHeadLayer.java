package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomHeadLayer.class)
public class AchtungCustomHeadLayer {
    /***
     * Code for Achtung Baby Item in hand rendering. First person held items in theory become
     * invisible partially thanks to this spot in the code working its magic
     *
     */
    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/world/entity/LivingEntity;FFFFFF)V", at = @At(value = "HEAD"), cancellable = true)
    public<T extends LivingEntity, M extends EntityModel<T>>
    void roundabout$renderHeadItemsAchtung(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci) {
        if ($$3 != null){
            StandUser user = ((StandUser)$$3);

            float throwFadeToTheEther = 1f;
            IEntityAndData entityAndData = ((IEntityAndData) user);
            throwFadeToTheEther = ClientUtil.getThrowFadePercent($$3,$$4);
            if (entityAndData.roundabout$getTrueInvisibility() > -1){
                if (!ClientUtil.checkIfClientCanSeeInvisAchtung()){
                    ClientUtil.setThrowFadeToTheEther(throwFadeToTheEther);
                    ci.cancel();
                    return;
                }
            }
            ClientUtil.setThrowFadeToTheEther(throwFadeToTheEther);
        }

    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
}

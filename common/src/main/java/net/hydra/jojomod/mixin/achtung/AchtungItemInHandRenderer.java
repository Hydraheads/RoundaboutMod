package net.hydra.jojomod.mixin.achtung;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersAchtungBaby;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class AchtungItemInHandRenderer {

    /***
     * Code for Achtung Baby Item in hand rendering. First person held items in theory become
     * invisible partially thanks to this spot in the code working its magic
     *
     */
    @Inject(method = "renderHandsWithItems", at = @At(value = "HEAD"), cancellable = true)
    public<T extends LivingEntity, M extends EntityModel<T>>
    void roundabout$renderHandsWithItemsAchtung(float partialTick, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource,
                                                LocalPlayer localPlayer, int light, CallbackInfo ci) {
        if (localPlayer != null){

            StandUser user = ((StandUser)localPlayer);

            float throwFadeToTheEther = 1f;
            IEntityAndData entityAndData = ((IEntityAndData) user);
            if (entityAndData.roundabout$getTrueInvisibility() > -1){
                throwFadeToTheEther = throwFadeToTheEther*0.4F;
                /**
                StandPowers powers = user.roundabout$getStandPowers();
                if (powers instanceof PowersAchtungBaby PB && PB.invisibleVisionOn()){
                    AbstractClientPlayer $$14 = this.minecraft.player;
                    if ($$14 != null) {
                        PlayerRenderer $$15 = (PlayerRenderer) this.entityRenderDispatcher.<AbstractClientPlayer>getRenderer($$14);
                        $$15.getModel().setAllVisible(true);

                    }
                }
                 **/
            }
            ClientUtil.setThrowFadeToTheEther(throwFadeToTheEther);
        }

    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow @Final private Minecraft minecraft;

    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;
}

package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.entity.client.PreRenderEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.presets.PowersMagiciansRed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

public class CrossfireHurricaneRenderer extends EntityRenderer<CrossfireHurricaneEntity> {

    private final CrossfireHurricaneModel model;

    public CrossfireHurricaneRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new CrossfireHurricaneModel($$0.bakeLayer(ModEntityRendererClient.CROSSFIRE_LAYER));
    }



    public void render(CrossfireHurricaneEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {

            $$3.pushPose();
            float rsize = $$0.getMaxSize();
            if ($$0.getRenderSize() < rsize){
                $$0.setRenderSize(Math.min((float) ($$0.getLastRenderSize() + ($$2 * (float)$$0.getAccrualRate())),rsize));
            }

            //$$3.mulPose(Axis.ZP.rotationDegrees(-180));
            //$$3.mulPose(Axis.YP.rotationDegrees((float) Mth.rotLerp($$2, (float) $$0.renderRotation, (float) $$0.lastRenderRotation)));
            $$3.mulPose(Axis.YP.rotationDegrees(-1*(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot()))));
            float fsize = $$0.getRenderSize() * 0.035f;
            $$3.scale(1.1f+fsize, 1.1f+fsize, 1.1f+fsize);
            VertexConsumer $$6 = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation($$0)));
            this.model.renderToBuffer($$3, $$6, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.57f);
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, 15728880);
        }
    }



    public static final ResourceLocation CROSSFIRE_HURRICANE_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_2_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_2.png");
    public static final ResourceLocation CROSSFIRE_HURRICANE_3_TEXTURE = new ResourceLocation(Roundabout.MOD_ID,"textures/entity/projectile/crossfire_hurricane_3.png");

    @Override
    public ResourceLocation getTextureLocation(CrossfireHurricaneEntity var1) {
        int tc = var1.tickCount%9;
        if (tc >2){
            return CROSSFIRE_HURRICANE_2_TEXTURE;
        } if (tc >5){
            return CROSSFIRE_HURRICANE_3_TEXTURE;
        }
        return CROSSFIRE_HURRICANE_TEXTURE;
    }


}
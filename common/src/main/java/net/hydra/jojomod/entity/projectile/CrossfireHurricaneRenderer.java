package net.hydra.jojomod.entity.projectile;

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
            float rsize = $$0.getSize() * 0.04f;
            $$3.translate(0f,1.96f+rsize,0f);

            $$3.mulPose(Axis.ZP.rotationDegrees(-180));
            //$$3.mulPose(Axis.YP.rotationDegrees((float) Mth.rotLerp($$2, (float) $$0.renderRotation, (float) $$0.lastRenderRotation)));
            $$3.mulPose(Axis.YP.rotationDegrees(Mth.rotLerp($$2, $$0.yRotO, $$0.getYRot())));
            float fsize = $$0.getSize() * 0.035f;
            $$3.scale(1.1f+fsize, 1.1f+fsize, 1.1f+fsize);
            VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect($$4, this.model.renderType(this.getTextureLocation($$0)), false, false);
            this.model.renderToBuffer($$3, $$6, $$5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, $$5);
        }
    }




    @Override
    public ResourceLocation getTextureLocation(CrossfireHurricaneEntity var1) {
        return ModEntities.CROSSFIRE_HURRICANE_TEXTURE;
    }


}
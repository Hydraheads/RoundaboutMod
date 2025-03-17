package net.hydra.jojomod.entity.projectile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.ILivingEntityAccess;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.ModEntities;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
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
import net.minecraft.world.entity.LivingEntity;

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
            LivingEntity user = $$0.getStandUser();
            if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                if ($$0.getCrossNumber() > 0) {
                    if (PMR.hurricaneSpecial == null) {
                        PMR.hurricaneSpecial = new ArrayList<>();
                    }
                    List<CrossfireHurricaneEntity> hurricaneSpecial2 = new ArrayList<>(PMR.hurricaneSpecial) {
                    };
                    if (!hurricaneSpecial2.isEmpty()) {
                        int totalnumber = hurricaneSpecial2.size();
                        double lerpX = (user.getX() * $$2) + (user.xOld * (1.0f - $$2));
                        double lerpY =(user.getY() * $$2) + (user.yOld * (1.0f - $$2));
                        double lerpZ = (user.getZ() * $$2) + (user.zOld * (1.0f - $$2));
                        PMR.transformHurricane($$0,totalnumber, lerpX,
                                lerpY, lerpZ);
                    }
                }
            }
            $$3.pushPose();
            float rsize = $$0.getSize() * 0.04f;
            $$3.translate(0f,1.96f+rsize,0f);
            $$3.mulPose(Axis.ZP.rotationDegrees(-180));
            $$3.mulPose(Axis.YP.rotationDegrees(Mth.lerp($$2, $$0.yRotO, $$0.getYRot())));
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
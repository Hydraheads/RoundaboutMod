package net.hydra.jojomod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.stand.MagiciansRedEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.StandModel;
import net.hydra.jojomod.event.index.OffsetIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class MagiciansRedSpinEffectLayer<T extends StandEntity> extends RenderLayer<T, StandModel<T>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/orange_flame_spin.png");
    public static final ResourceLocation TEXTURE_BLUE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/blue_flame_spin.png");
    public static final ResourceLocation TEXTURE_PURPLE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/purple_flame_spin.png");
    public static final ResourceLocation TEXTURE_GREEN = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/green_flame_spin.png");
    public static final ResourceLocation TEXTURE_DREAD = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/dread_flame_spin.png");
    public static final String BOX = "box";
    private final ModelPart box;

    public ResourceLocation getMRTextureLocation(MagiciansRedEntity entity) {
        byte BT = entity.getSkin();
        if (BT == MagiciansRedEntity.BLUE_SKIN || BT == MagiciansRedEntity.BLUE_ACE_SKIN){
            return TEXTURE_BLUE;
        } else if (BT == MagiciansRedEntity.PURPLE_SKIN){
            return TEXTURE_PURPLE;
        } else if (BT == MagiciansRedEntity.GREEN_SKIN){
            return TEXTURE_GREEN;
        } else if (BT == MagiciansRedEntity.DREAD_SKIN || BT == MagiciansRedEntity.DREAD_BEAST_SKIN){
            return TEXTURE_DREAD;
        }
        return TEXTURE;
    }
    public MagiciansRedSpinEffectLayer(RenderLayerParent<T, StandModel<T>> $$0, EntityModelSet $$1) {
        super($$0);
        ModelPart $$2 = $$1.bakeLayer(ModEntityRendererClient.MR_SPIN_LAYER);
        this.box = $$2.getChild("box");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition $$0 = new MeshDefinition();
        PartDefinition $$1 = $$0.getRoot();
        $$1.addOrReplaceChild("box", CubeListBuilder.create().texOffs(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16.0F, 32.0F, 16.0F), PartPose.ZERO);
        return LayerDefinition.create($$0, 64, 64);
    }

    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T $$3, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        LivingEntity user = $$3.getUser();
        if (user != null) {
            StandUser standUser = (StandUser) user;
            if (standUser.roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
                if ($$3.getOffsetType() == OffsetIndex.LOOSE && !$$3.getDisplay()) {
                    VertexConsumer $$10 = $$1.getBuffer(RenderType.entityCutoutNoCull(getMRTextureLocation((MagiciansRedEntity) $$3)));

                    for (int $$11 = 0; $$11 < 3; ++$$11) {
                        $$0.pushPose();
                        float $$12 = $$7 * (float) (-(45 + $$11));
                        $$0.mulPose(Axis.YP.rotationDegrees($$12));
                        float $$13 = 0.75F * (float) $$11;
                        $$0.scale($$13, $$13, $$13);
                        $$0.translate(0.0F, -0.2F + 0.6F * (float) $$11, 0.0F);
                        this.box.render($$0, $$10, $$2, OverlayTexture.NO_OVERLAY);
                        $$0.popPose();
                    }

                }

            }
        }
    }
}


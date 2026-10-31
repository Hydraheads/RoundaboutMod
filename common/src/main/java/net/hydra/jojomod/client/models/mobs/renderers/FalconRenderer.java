package net.hydra.jojomod.client.models.mobs.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.client.models.mobs.FalconModel;
import net.hydra.jojomod.client.models.mobs.StrayCatEntityModel;
import net.hydra.jojomod.entity.mobs.FalconEntity;
import net.hydra.jojomod.entity.mobs.StrayCatEntity;
import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FalconRenderer<T extends FalconEntity>
        extends MobRenderer<T, FalconModel<T>> {

    private static final ResourceLocation PET_SHOP =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/falcon/pet_shop.png");
    private static final ResourceLocation APLOMADO =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/falcon/aplomado.png");
    private static final ResourceLocation GERIFALTE =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/falcon/gerifalte.png");
    private static final ResourceLocation CARACARA =
            new ResourceLocation(Roundabout.MOD_ID,"textures/entity/falcon/caracara.png");

    public FalconRenderer(EntityRendererProvider.Context context) {
        super(context,new FalconModel<>(context.bakeLayer(ModEntityRendererClient.FALCON_LAYER)),0.125F);
    }

    @Override
    public ResourceLocation getTextureLocation(T falcon) {
        switch (falcon.getVariant()) {
            case APLOMADO -> {return APLOMADO;}
            case GERIFALTE -> {return GERIFALTE;}
            case CARACARA -> {return CARACARA;}
            case PET_SHOP -> {return PET_SHOP;}
        }

        return PET_SHOP;
    }

    @Override
    public void render(T entity, float $$1, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, $$1, partialTicks, poseStack, bufferSource, packedLight);
    }
}

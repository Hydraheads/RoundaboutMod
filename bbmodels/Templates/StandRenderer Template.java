package net.hydra.jojomod.client.models.stand.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;


// [Stand Name Here] = Ratt, StarPlatinum, etc.
// [Stand Entity] = your stand entity class ex. RattEntity, GreenDayEntity
// [STAND_LAYER] =

public class [Stand Name Here]Renderer extends StandRenderer<[Stand Entity]> {

    // skin 1 is default skin
    private static final ResourceLocation SKIN_1 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/[Stand Name Here]/skin_1.png");
    private static final ResourceLocation SKIN_2 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/[Stand Name Here]/skin_2.png");
    private static final ResourceLocation SKIN_3 = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/[Stand Name Here]/skin_3.png");



    public RattRenderer(EntityRendererProvider.Context context) {
        super(context, new [Stand Model]<>(context.bakeLayer(ModEntityRendererClient.[STAND_LAYER])), 0f);
    }

    @Override public ResourceLocation getTextureLocation([Stand Entity] entity) {
        byte BT = entity.getSkin();
        switch (BT) {
            case [Stand Entity].SKIN_2 -> {return SKIN_2;}
            case [Stand Entity].SKIN_3 -> {return SKIN_3;}
            // default skin
            default -> {return SKIN_1;}
        }
    }


    @Override
    public void render([Stand Entity] mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Nullable
    @Override
    protected RenderType getRenderType([Stand Entity] entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}

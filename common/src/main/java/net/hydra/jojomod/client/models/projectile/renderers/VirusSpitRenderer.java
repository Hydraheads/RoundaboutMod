package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.projectile.BloodSplatterEntity;
import net.hydra.jojomod.entity.projectile.VirusSpitEntity;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class VirusSpitRenderer extends EntityRenderer<VirusSpitEntity> {
    private static final ResourceLocation DEFAULTSPIT = new ResourceLocation(Roundabout.MOD_ID,
            "textures/entity/projectile/virus_spit.png");


    private final float scale;

    public VirusSpitRenderer(EntityRendererProvider.Context context, float scale) {
        super(context);
        this.scale = scale;
    }

    public VirusSpitRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.scale = 1f;
    }

    @Override
    public void render(VirusSpitEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // Invisible projectile model.
        // VirusSpitEntity handles the visible particle trail.
    }

    @Override
    public ResourceLocation getTextureLocation(VirusSpitEntity entity) {
        return DEFAULTSPIT;
    }
}

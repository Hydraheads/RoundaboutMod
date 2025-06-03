package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.gui.NoCancelInputScreen;
import net.hydra.jojomod.client.gui.PoseSwitcherScreen;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersSoftAndWet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.Map;

public class ShootingArmLayer <T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public ShootingArmLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/large_bubble.png");
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            if (!entity.isInvisible()) {
                if (entity!= null) {
                    StandUser user = ((StandUser)entity);
                    if (user.roundabout$getCombatMode()) {
                        if (user.roundabout$getStandPowers() instanceof PowersSoftAndWet PW) {
                            poseStack.pushPose();

                            // Translate to the right/left hand

                            getParentModel().rightArm.translateAndRotate(poseStack); // Use leftArm for off-hand

                            // Apply additional transformations
                            poseStack.translate(-0.05F, 0.65, 0F); //1 1
                            // The third value pushes it up (negative)
                            poseStack.scale(1.0F, 1.0F, 1.0F);

                            // Render your model here
                            ModStrayModels.SHOOTING_ARM.render(entity, poseStack, bufferSource, packedLight);

                            poseStack.popPose();
                        }
                    }
                }
            }
        }
    }
}

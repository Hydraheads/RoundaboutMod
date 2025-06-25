package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Quaternionf;

import java.util.Map;

public class RattShoulderLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public RattShoulderLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {
        if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
            LivingEntity livent = entity;
            if (!entity.isInvisible()) {
                if (entity != null) {
                    if (entity instanceof JojoNPC jnp && jnp.host != null) {
                        livent = jnp.host;
                    }
                    StandUser user = ((StandUser) livent);
                    int heyTicks = user.roundabout$getRattShoulderVanishTicks();
                    boolean hasHeyYaOut = (user.roundabout$getActive() && user.roundabout$getStandPowers() instanceof PowersRatt);

                    if (heyTicks > 0 || hasHeyYaOut) {
                        byte skin = user.roundabout$getStandSkin();
                        if (user.roundabout$getLastStandSkin() != skin){
                            user.roundabout$setLastStandSkin(skin);
                            heyTicks = 0;
                            user.roundabout$setRattShoulderVanishTicks(0);
                        }

                        float heyFull = 0;
                        float fixedPartial = partialTicks - (int) partialTicks;
                        if (((TimeStop)entity.level()).CanTimeStopEntity(entity)){
                            fixedPartial = 0;
                        }
                        if (hasHeyYaOut){
                            heyFull = heyTicks+fixedPartial;
                            heyFull = Math.min(heyFull/10,1f);
                        } else {
                            heyFull = heyTicks-fixedPartial;
                            heyFull = Math.max(heyFull/10,0);
                        }
                        poseStack.pushPose();

                        // Translate to the right/left hand
                            getParentModel().body.translateAndRotate(poseStack); // Use leftArm for off-hand
                            poseStack.translate(0.37F, -0.4F, 0F); //1 1
                            // The first value goes to the right (negative) and left (positive)
                            // The second value is correlated with up (negative) and down (positive)
                        // the third is further and closer, positive to head towards back negative for away
                        // Render your model here


                        poseStack.scale(0.3F,0.3F,0.3F);
                        boolean isHurt = livent.hurtTime > 0;
                        float r = isHurt ? 1.0F : 1.0F;
                        float g = isHurt ? 0.0F : 1.0F;
                        float b = isHurt ? 0.0F : 1.0F;
                        ModStrayModels.RATT_SHOULDER.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                                r, g, b, heyFull, skin);
                        poseStack.popPose();
                    }
                }
            }
        }
    }
}


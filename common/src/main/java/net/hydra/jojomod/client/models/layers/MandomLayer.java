package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersMandom;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Map;

public class MandomLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {
    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public MandomLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float var5, float var6, float var7, float partialTicks, float var9, float var10) {

        if (entity != null) {
            StandUser user = ((StandUser) entity);
            boolean hasMandom = (user.roundabout$getStandPowers() instanceof PowersMandom);
            boolean hasMandomOut = (user.roundabout$getActive() && hasMandom);
            if (ClientUtil.canSeeStands(ClientUtil.getPlayer())) {
                if (!entity.isInvisible()) {
                        int mandomTicks = user.roundabout$getMandomVanishTicks();


                        if (mandomTicks > 0 || hasMandomOut) {
                            byte skin = user.roundabout$getStandSkin();
                            if (user.roundabout$getLastStandSkin() != skin){
                                user.roundabout$setLastStandSkin(skin);
                                mandomTicks = 0;
                                user.roundabout$setMandomVanishTicks(0);
                            }

                            float heyFull = 0;
                            float fixedPartial = partialTicks - (int) partialTicks;
                            if (((TimeStop)entity.level()).CanTimeStopEntity(entity)){
                                fixedPartial = 0;
                            }
                            if (hasMandomOut){
                                heyFull = mandomTicks+fixedPartial;
                                heyFull = Math.min(heyFull/10,1f);
                            } else {
                                heyFull = mandomTicks-fixedPartial;
                                heyFull = Math.max(heyFull/10,0);
                            }
                            poseStack.pushPose();

                            // Translate to the right/left hand
                                getParentModel().body.translateAndRotate(poseStack); // Use leftArm for off-hand
                                // Apply additional transformations
                                // The first value goes to the right (negative) and left (positive)
                                // The second value is correlated with up (negative) and down (positive)
                            // the third is further and closer, positive to head towards bakc negative for away
                            // Render your model here
                            boolean isHurt = entity.hurtTime > 0;
                            float r = isHurt ? 1.0F : 1.0F;
                            float g = isHurt ? 0.4F : 1.0F;
                            float b = isHurt ? 0.4F : 1.0F;
                            ModStrayModels.MANDOM.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                                    r, g, b, heyFull, skin);
                            poseStack.popPose();
                        }
                }
            }

            if (hasMandom && entity instanceof Player PL){
                byte style = ((IPlayerEntity)PL).roundabout$getWatchStyle();
                if (style != PowersMandom.WATCHLESS){
                    poseStack.pushPose();

                    // Translate to the right/left hand
                    getParentModel().rightArm.translateAndRotate(poseStack); // Use leftArm for off-hand
                    // Apply additional transformations
                    // The first value goes to the right (negative) and left (positive)
                    // The second value is correlated with up (negative) and down (positive)
                    // the third is further and closer, positive to head towards bakc negative for away
                    // Render your model here
                    boolean isHurt = entity.hurtTime > 0;
                    float r = isHurt ? 1.0F : 1.0F;
                    float g = isHurt ? 0.4F : 1.0F;
                    float b = isHurt ? 0.4F : 1.0F;
                    if ((getParentModel() instanceof PlayerModel<?> PM && ((IPlayerModel)PM).roundabout$getSlim())){
                        ModStrayModels.MANDOM_WATCH_SMALL.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                                r, g, b, 1, style);
                    } else {
                        ModStrayModels.MANDOM_WATCH.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                                r, g, b, 1, style);
                    }
                    poseStack.popPose();
                }
            }
        }
    }
}


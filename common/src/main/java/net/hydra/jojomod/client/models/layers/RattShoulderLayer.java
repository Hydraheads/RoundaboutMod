package net.hydra.jojomod.client.models.layers;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.entity.stand.RattEntity;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.mixin.PlayerEntity;
import net.hydra.jojomod.stand.powers.PowersHeyYa;
import net.hydra.jojomod.stand.powers.PowersRatt;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

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
                    /* all of this code is copied from hey ya! */
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

                        if (hasHeyYaOut) {
                            /* idlePos == 0 is shoulder, 1 is head */
                            if (((PowersRatt) user.roundabout$getStandPowers()).scopeLevel != 0) {
                                getParentModel().rightArm.translateAndRotate(poseStack);
                                poseStack.scale(0.35F, 0.35F, 0.35F);
                                poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(new Vector3f(1, 0, 0), -90), 0, 0, -1);
                                poseStack.translate(0.35F, -1.3F, 0.6F);
                                //  poseStack.translate(0.3F, 0F, -3F);
                            } else {
                                if (user.roundabout$getIdlePos() == 0) {
                                    getParentModel().body.translateAndRotate(poseStack);
                                    poseStack.translate(0.35F, -0.4F, 0F);
                                    /* additional check for armor, cancels the check if you have a visage */
                                    boolean bl1 = false;
                                    if (user instanceof Player) {
                                        ItemStack chestplate = ((Player) user).getInventory().getArmor(2);
                                        String name = chestplate.getItem().toString();
                                        if (!name.equals("air") && !getUserVisage(((Entity) user))) {
                                            poseStack.translate(0.0F, -0.1F, 0.0F);
                                            bl1 = true;
                                        }
                                    }
                                    if (!bl1) {
                                        poseStack.translate(0.05F, 0.05F, 0.0F);
                                        poseStack.rotateAround(new Quaternionf().fromAxisAngleDeg(new Vector3f(0, 0, 1), 10), 0, 0, 0);
                                    }
                                } else {
                                    getParentModel().head.translateAndRotate(poseStack);
                                    poseStack.translate(0F, -0.9F, 0F);
                                    /* additional check for armor, cancels the check if you have a visage */
                                    if (user instanceof Player) {
                                        ItemStack chestplate = ((Player) user).getInventory().getArmor(3);
                                        String name = chestplate.getItem().toString();
                                        if (!name.equals("air") && !getUserVisage(((Entity) user))) {
                                            poseStack.translate(0.0F, -0.1F, 0.0F);
                                        }
                                    }
                                }
                                poseStack.scale(0.3F, 0.3F, 0.3F);
                            }
                            // The first value goes to the right (negative) and left (positive)
                            // The second value is correlated with up (negative) and down (positive)
                            // the third is further and closer, positive is back  negative is forward
                        }

                            boolean isHurt = livent.hurtTime > 0;
                            float r = isHurt ? 1.0F : 1.0F;
                            float g = isHurt ? 0.0F : 1.0F;
                            float b = isHurt ? 0.0F : 1.0F;
                            if (user.roundabout$getRattShoulderVanishTicks() != 0) {
                                if (user.roundabout$getStandSkin() == RattEntity.REDD_SKIN) {
                                    ModStrayModels.EYEBROW_RATT_SHOULDER.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                                            r, g, b, heyFull, skin);
                                } else {
                                    ModStrayModels.RATT_SHOULDER.render(livent, partialTicks, poseStack, bufferSource, packedLight,
                                            r, g, b, heyFull, skin);
                                }

                            }
                            poseStack.popPose();

                    }
                }
            }
        }
    }

    public boolean getUserVisage(Entity entity) {
        if (entity instanceof Player play) {
            IPlayerEntity pl = ((IPlayerEntity) play);
            ItemStack visage = pl.roundabout$getMaskSlot();
            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    return true;
                }
            }
        }
        return false;
    }
}

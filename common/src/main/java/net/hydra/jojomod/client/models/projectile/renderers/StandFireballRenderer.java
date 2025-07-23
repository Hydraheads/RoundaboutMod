package net.hydra.jojomod.client.models.projectile.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.models.projectile.StandFireballModel;
import net.hydra.jojomod.client.models.layers.ModEntityRendererClient;
import net.hydra.jojomod.entity.projectile.StandFireballEntity;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.stand.powers.PowersMagiciansRed;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class StandFireballRenderer extends EntityRenderer<StandFireballEntity> {

    private final StandFireballModel model;

    public StandFireballRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
        this.model = new StandFireballModel($$0.bakeLayer(ModEntityRendererClient.STAND_FIREBALL_LAYER));
    }


    public void render(StandFireballEntity $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        if (ClientUtil.canSeeStands(Minecraft.getInstance().player)) {
            if (((TimeStop) $$0.level()).inTimeStopRange($$0)) {
                $$2 = 0;
            }
            $$3.pushPose();

            //$$3.mulPose(Axis.ZP.rotationDegrees(-180));
            //$$3.mulPose(Axis.YP.rotationDegrees((float) Mth.rotLerp($$2, (float) $$0.renderRotation, (float) $$0.lastRenderRotation)));
            $$3.mulPose(Axis.YP.rotationDegrees($$0.getYRot()));
            $$3.mulPose(Axis.ZP.rotationDegrees($$0.getXRot()));
            $$3.scale(2.6f, 2.6f, 2.6f);
            VertexConsumer $$6 = $$4.getBuffer(RenderType.entityTranslucent(getTextureLocation($$0)));
            this.model.renderToBuffer($$3, $$6, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.4f);
            $$3.popPose();
            super.render($$0, $$1, $$2, $$3, $$4, 15728880);
        }
    }


    public static final ResourceLocation STAND_FIREBALL_TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_2.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_3.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_BLUE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_blue.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_2_BLUE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_2_blue.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_3_BLUE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_3_blue.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_PURPLE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_purple.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_2_PURPLE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_2_purple.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_3_PURPLE = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_3_purple.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_GREEN = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_green.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_2_GREEN = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_2_green.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_3_GREEN = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_3_green.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_DREAD = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_dread.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_2_DREAD = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_2_dread.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_3_DREAD = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_3_dread.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_CREAM = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_cream.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_2_CREAM = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_2_cream.png");
    public static final ResourceLocation STAND_FIREBALL_TEXTURE_3_CREAM = new ResourceLocation(Roundabout.MOD_ID, "textures/entity/projectile/stand_fireball_3_cream.png");

    public ResourceLocation getThird(StandFireballEntity var1){
        LivingEntity user = var1.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return STAND_FIREBALL_TEXTURE_3_BLUE;
            } else if (sft == StandFireType.PURPLE.id){
                return STAND_FIREBALL_TEXTURE_3_PURPLE;
            } else if (sft == StandFireType.GREEN.id){
                return STAND_FIREBALL_TEXTURE_3_GREEN;
            } else if (sft == StandFireType.DREAD.id){
                return STAND_FIREBALL_TEXTURE_3_DREAD;
            } else if (sft == StandFireType.CREAM.id){
                return STAND_FIREBALL_TEXTURE_3_CREAM;
            }
        }
        return STAND_FIREBALL_TEXTURE_3;
    }
    public ResourceLocation getFirst(StandFireballEntity var1){
        LivingEntity user = var1.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return STAND_FIREBALL_TEXTURE_BLUE;
            } else if (sft == StandFireType.PURPLE.id){
                return STAND_FIREBALL_TEXTURE_PURPLE;
            } else if (sft == StandFireType.GREEN.id){
                return STAND_FIREBALL_TEXTURE_GREEN;
            } else if (sft == StandFireType.DREAD.id){
                return STAND_FIREBALL_TEXTURE_DREAD;
            } else if (sft == StandFireType.CREAM.id){
                return STAND_FIREBALL_TEXTURE_CREAM;
            }
        }
        return STAND_FIREBALL_TEXTURE;
    }
    public ResourceLocation getSecond(StandFireballEntity var1){
        LivingEntity user = var1.getUser();
        if (user != null && ((StandUser)user).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR){
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return STAND_FIREBALL_TEXTURE_2_BLUE;
            } else if (sft == StandFireType.PURPLE.id){
                return STAND_FIREBALL_TEXTURE_2_PURPLE;
            } else if (sft == StandFireType.GREEN.id){
                return STAND_FIREBALL_TEXTURE_2_GREEN;
            } else if (sft == StandFireType.DREAD.id){
                return STAND_FIREBALL_TEXTURE_2_DREAD;
            } else if (sft == StandFireType.CREAM.id){
                return STAND_FIREBALL_TEXTURE_2_CREAM;
            }
        }
        return STAND_FIREBALL_TEXTURE_2;
    }
    @Override
    public ResourceLocation getTextureLocation(StandFireballEntity var1) {
        int tc = var1.tickCount % 5;
        if (tc > 3) {
            return getThird(var1);
        }
        if (tc > 1) {
            return getSecond(var1);
        }
        return getFirst(var1);
    }

}

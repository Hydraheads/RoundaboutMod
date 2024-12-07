package net.hydra.jojomod.entity.stand;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.entity.client.ModEntityRendererClient;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.ClientConfig;
import net.hydra.jojomod.util.ConfigManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.parser.Entity;

public class JusticeBaseRenderer extends StandRenderer<JusticeEntity> {
    public JusticeBaseRenderer(EntityRendererProvider.Context context, StandModel<JusticeEntity> entityModel, float f) {
        super(context, entityModel,f);
    }
    public void renderRightHand(PoseStack $$0, MultiBufferSource $$1, int $$2, JusticeEntity $$3) {
        this.renderHand($$0, $$1, $$2, $$3, this.model.rightHand);
    }

    public void renderLeftHand(PoseStack $$0, MultiBufferSource $$1, int $$2, JusticeEntity $$3) {
        this.renderHand($$0, $$1, $$2, $$3, this.model.leftHand);
    }

    private void renderHand(PoseStack $$0, MultiBufferSource $$1, int $$2, JusticeEntity $$3, ModelPart $$4) {
        StandModel<JusticeEntity> $$6 = this.getModel();
        $$6.attackTime = 0.0F;
        $$6.setupAnim($$3, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        $$4.xRot = 0.0F;
        $$4.render($$0, $$1.getBuffer(RenderType.entitySolid(getTextureLocation($$3))), $$2, OverlayTexture.NO_OVERLAY);
    }
    private static final ResourceLocation PART_3_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice.png");
    private static final ResourceLocation PART_3_MANGA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_manga.png");
    private static final ResourceLocation OVA_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_ova.png");
    private static final ResourceLocation BOGGED_SKIN = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_bogged.png");
    private static final ResourceLocation SKELETON = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_skeleton.png");
    private static final ResourceLocation STRAY = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_stray.png");
    private static final ResourceLocation WITHER = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_wither.png");
    private static final ResourceLocation TAROT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_tarot.png");
    private static final ResourceLocation FLAMED = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_flamed.png");
    private static final ResourceLocation TWILIGHT = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_twilight.png");
    private static final ResourceLocation PIRATE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_pirate.png");
    private static final ResourceLocation BLUE_FLAME = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/justice_flamed_blue.png");
    private static final ResourceLocation DARK_MIRAGE = new ResourceLocation(Roundabout.MOD_ID,"textures/stand/dark_mirage.png");



    @Override
    public ResourceLocation getTextureLocation(JusticeEntity entity) {
        byte BT = entity.getSkin();
        if (BT == JusticeEntity.PART_3_SKIN){
            return PART_3_SKIN;
        } else if (BT == JusticeEntity.MANGA_SKIN){
            return PART_3_MANGA_SKIN;
        } else if (BT == JusticeEntity.OVA_SKIN){
            return OVA_SKIN;
        } else if (BT == JusticeEntity.BOGGED){
            return BOGGED_SKIN;
        } else if (BT == JusticeEntity.SKELETON_SKIN){
            return SKELETON;
        } else if (BT == JusticeEntity.STRAY_SKIN){
            return STRAY;
        } else if (BT == JusticeEntity.FLAMED){
            return FLAMED;
        } else if (BT == JusticeEntity.WITHER){
            return WITHER;
        } else if (BT == JusticeEntity.TAROT){
            return TAROT;
        } else if (BT == JusticeEntity.TWILIGHT){
            return TWILIGHT;
        } else if (BT == JusticeEntity.PIRATE){
            return PIRATE;
        } else if (BT == JusticeEntity.BLUE_FLAMED){
            return BLUE_FLAME;
        } else if (BT == JusticeEntity.DARK_MIRAGE){
            return DARK_MIRAGE;
        }
        return PART_3_SKIN;
    }
    @Override
    public void render(JusticeEntity mobEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        float factor = 0.5F + (mobEntity.getSizePercent()/2);
        if (mobEntity.getSkin() == JusticeEntity.DARK_MIRAGE){
            matrixStack.scale(0.87f*factor, 0.87f*factor, 0.87f*factor);
        } else {
            matrixStack.scale(2.0f*factor,2.0f*factor,2.0f*factor);
        }

        Player pl = Minecraft.getInstance().player;
        LivingEntity user = mobEntity.getUser();
        if (user != null) {
        StandUser standUser = ((StandUser)mobEntity.getUser());
        StandPowers powers = standUser.roundabout$getStandPowers();

         if (powers.isPiloting()){
             boolean renderHand = ConfigManager.getClientConfig().renderJusticeHandsWhilePiloting;
             if (powers.getPilotingStand() != null && powers.getPilotingStand().is(mobEntity)){
                 boolean fp = Minecraft.getInstance().options.getCameraType().isFirstPerson();
                 if (fp && !mobEntity.getDisplay() && pl != null && user.is(pl)){

                     this.model.head.visible = false;
                     if (mobEntity instanceof DarkMirageEntity) {
                         this.model.body.visible = false;
                     } else {
                         if (!renderHand){
                             this.model.leftHand.visible = false;
                             this.model.rightHand.visible = false;
                         }
                     }
                 }
             }
         }
        }

        super.render(mobEntity, f, g, matrixStack, vertexConsumerProvider, i);
        this.model.head.visible = true;
        if (mobEntity instanceof DarkMirageEntity) {
            this.model.body.visible = true;
        } else {
            this.model.leftHand.visible = true;
            this.model.rightHand.visible = true;
        }
    }

    @Nullable
    @Override
    protected RenderType getRenderType(JusticeEntity entity, boolean showBody, boolean translucent, boolean showOutline) {
        return super.getRenderType(entity, showBody, true, showOutline);
    }
}


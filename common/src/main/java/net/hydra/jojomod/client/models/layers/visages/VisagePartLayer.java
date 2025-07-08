package net.hydra.jojomod.client.models.layers.visages;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.access.IPlayerModel;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.npcs.ZombieAesthetician;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.index.ShapeShifts;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.hydra.jojomod.event.powers.visagedata.VisageData;
import net.hydra.jojomod.item.MaskItem;
import net.hydra.jojomod.item.ModItems;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;

public class VisagePartLayer<T extends LivingEntity, A extends HumanoidModel<T>> extends RenderLayer<T, A> {

    private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
    private final EntityRenderDispatcher dispatcher;
    public VisagePartLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
        super(livingEntityRenderer);
        this.dispatcher = context.getEntityRenderDispatcher();
    }

    float scale = 1;
    private static final ResourceLocation TEXTURE = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/large_bubble.png");
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, float var9, float var10) {


        ItemStack visage = null;
        if (entity instanceof Player play) {
            IPlayerEntity pl = ((IPlayerEntity) play);
            visage = pl.roundabout$getMaskSlot();
            ShapeShifts shift = ShapeShifts.getShiftFromByte(pl.roundabout$getShapeShift());
            if (shift == ShapeShifts.OVA) {
                visage = ModItems.ENYA_OVA_MASK.getDefaultInstance();
            }
        } else if (entity instanceof JojoNPC jnpc){
            visage = jnpc.getBasis();
        } else if (entity instanceof ZombieAesthetician znpc){
            visage = znpc.getBasis();
        }


        boolean isHurt = entity.hurtTime > 0;
        float r = isHurt ? 1.0F : 1.0F;
        float g = isHurt ? 0.6F : 1.0F;
        float b = isHurt ? 0.6F : 1.0F;
        StandUser user = ((StandUser) entity);
        int muscle = user.roundabout$getZappedToID();
        //muscle = 100;
        if (muscle > -1){
            float scale = 1.055F;
            float alpha = 0.6F;
            float oscillation = Math.abs(((entity.tickCount % 10) + (partialTicks%1))-5)*0.04F;
            alpha += oscillation;
            if (entity.getMainArm() == HumanoidArm.RIGHT) {
                if (getParentModel() instanceof PlayerModel<?> PM && ((IPlayerModel) PM).roundabout$getSlim()) {
                    renderRightArmSlim(poseStack, bufferSource, packedLight, entity, scale, scale, scale, partialTicks,
                            r, g, b, StandIcons.MUSCLE_SLIM, 0.01F, 0, 0, alpha);
                } else {
                    renderRightArm(poseStack, bufferSource, packedLight, entity, scale, scale, scale, partialTicks,
                            r, g, b, StandIcons.MUSCLE, 0.01F, 0, 0, alpha);
                }
            }
        }


            if (visage != null && !visage.isEmpty()) {
                if (visage.getItem() instanceof MaskItem MI) {
                    VisageData vd = MI.visageData.generateVisageData(entity);
                    String path = MI.visageData.getSkinPath();
                    if (vd.rendersBreast()){
                        renderNormalBreast(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersSmallBreast()){
                        renderSmallBreast(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersPonytail()){
                        renderPonytail(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersBigHair()){
                        renderBigHair(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersDiegoHat()){
                        renderDiegoHat(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersBasicHat()){
                        renderBasicHat(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersSpikeyHair()){
                        renderSpikeyHair(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersJosukeDecals()){
                        renderJosukeDecals(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersTasselHat()){
                        renderTasselHat(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersLegCloakPart()){
                        renderLegCloakPart(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersAvdolHairPart()){
                        renderAvdolHair(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks, path,
                                r,g,b);
                    }
                    if (vd.rendersPlayerBreastPart()){
                        renderPlayerBreastPart(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                r,g,b);
                    }
                    if (vd.rendersPlayerSmallBreastPart()){
                        renderSmallPlayerBreastPart(poseStack,bufferSource, packedLight, entity, xx, yy, zz, partialTicks,
                                r,g,b);
                    }
                }
            }
    }
    public void renderRightArm(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                           float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (getParentModel().rightArm.visible) {
            poseStack.pushPose();
            getParentModel().rightArm.translateAndRotate(poseStack);
            ModStrayModels.RightArm.root().xScale = xx;
            ModStrayModels.RightArm.root().yScale = yy;
            ModStrayModels.RightArm.root().zScale = zz;
            ModStrayModels.RightArm.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            poseStack.popPose();
        }
    }
    public void renderRightArmSlim(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                               float r, float g, float b, ResourceLocation RL, float xtrans, float ytrans, float ztrans, float alpha) {
        if (getParentModel().rightArm.visible) {
            poseStack.pushPose();
            poseStack.scale(xx,yy,zz);
            getParentModel().rightArm.translateAndRotate(poseStack);
            ModStrayModels.RightArm.root().xScale = xx;
            ModStrayModels.RightArm.root().yScale = yy;
            ModStrayModels.RightArm.root().zScale = zz;
            ModStrayModels.RightArmSlim.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                    r, g, b, alpha, RL, xx, yy, zz, xtrans, ytrans, ztrans);
            poseStack.popPose();
        }
    }
    public void renderNormalBreast(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {
        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.ChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderSmallBreast(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {
        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.SmallChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderPonytail(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.PonytailPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderBigHair(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                               float r, float g, float b) {
        poseStack.pushPose();
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.BigHairPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderDiegoHat(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                              float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.DiegoHatPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderBasicHat(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                               float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.BasicHatPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderSpikeyHair(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                               float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.SpikeyHairPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderAvdolHair(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                 float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.AvdolHairPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderJosukeDecals(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                 float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.JosukeDecalsPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderTasselHat(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                 float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().head.translateAndRotate(poseStack);
        ModStrayModels.TasselHatPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path);
        poseStack.popPose();
    }
    public void renderLegCloakPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, String path,
                                   float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.LegCloakPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1, path,-1*(Math.min(getParentModel().leftLeg.xRot,getParentModel().rightLeg.xRot)));
        poseStack.popPose();
    }
    public void renderPlayerBreastPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                   float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.PlayerChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1);
        poseStack.popPose();
    }
    public void renderSmallPlayerBreastPart(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks,
                                       float r, float g, float b) {

        poseStack.pushPose();
        getParentModel().body.translateAndRotate(poseStack);
        ModStrayModels.PlayerSmallChestPart.render(entity, partialTicks, poseStack, bufferSource, packedLight,
                r, g, b, 1);
        poseStack.popPose();
    }
}

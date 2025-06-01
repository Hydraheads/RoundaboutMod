package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public class ZItemInHandLayer<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
    public ZItemInHandLayer(RenderLayerParent<T, M> $$0) {
        super($$0);
    }

    public boolean roundabout$ModifyEntity;
    public @Nullable ItemStack roundabout$RenderMainHand;
    public @Nullable ItemStack roundabout$RenderOffHand;
    public boolean dominant$Hand;


    @Unique
    private static final ResourceLocation roundabout$TEXTURE_1 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_1.png");
    @Unique
    private static final ResourceLocation roundabout$TEXTURE_2 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_2.png");
    @Unique
    private static final ResourceLocation roundabout$TEXTURE_3 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_3.png");
    @Unique
    private static final ResourceLocation roundabout$TEXTURE_4 = new ResourceLocation(Roundabout.MOD_ID, "textures/stand/soft_and_wet/projectiles/shooting_bubble_4.png");
    @Inject(method = "render", at = @At(value = "HEAD"),cancellable = true)
    public void roundabout$Render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T Entity, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9, CallbackInfo ci){
        dominant$Hand = Entity.getMainArm() == HumanoidArm.RIGHT;
        if (Entity instanceof Player) {
            roundabout$ModifyEntity = ((TimeStop) Entity.level()).CanTimeStopEntity(Entity) || ClientUtil.getScreenFreeze();
            if (roundabout$ModifyEntity) {
                if (((IEntityAndData) Entity).roundabout$getRoundaboutRenderMainHand() == null){
                    ((IEntityAndData) Entity).roundabout$setRoundaboutRenderMainHand(Entity.getMainHandItem().copy());
                }
                if (((IEntityAndData) Entity).roundabout$getRoundaboutRenderOffHand() == null){
                    ((IEntityAndData) Entity).roundabout$setRoundaboutRenderOffHand(Entity.getOffhandItem().copy());
                }
                roundabout$RenderMainHand = ((IEntityAndData) Entity).roundabout$getRoundaboutRenderMainHand();
                roundabout$RenderOffHand = ((IEntityAndData) Entity).roundabout$getRoundaboutRenderOffHand();
            } else {
                if (((IEntityAndData) Entity).roundabout$getRoundaboutRenderOffHand() != null){
                    ((IEntityAndData) Entity).roundabout$setRoundaboutRenderOffHand(null);
                } else if (((IEntityAndData) Entity).roundabout$getRoundaboutRenderMainHand() != null){
                    ((IEntityAndData) Entity).roundabout$setRoundaboutRenderMainHand(null);
                }
            }
        } else {
            roundabout$ModifyEntity = false;
        }

        LivingEntity host = Entity;
        if (Entity instanceof JojoNPC jnpc && ((StandUser)jnpc.host).roundabout$getEffectiveCombatMode()){
            host = jnpc.host;
        }
        if (host != null && ((StandUser)host).roundabout$getEffectiveCombatMode() && !host.isUsingItem()){

            /** Ultimately rendering the hand bubble in 2D is inconvenient because of all the places it has to be done,
             * so I wish to instead burn a new layer in 3d onto the hand
             *
            poseStack.pushPose();
            HumanoidArm mainArm = host.getMainArm();
            this.getParentModel().translateToHand(mainArm, poseStack);
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            boolean humanarm = mainArm == HumanoidArm.LEFT;
            poseStack.translate((float)(humanarm ? -1 : 1) / 16.0F, 0.125F, -0.625F);
            poseStack.translate(1,0,0);


            // Draw flat quad here
            Vector3f normal = Minecraft.getInstance().gameRenderer.getMainCamera().getLookVector();
            normal.normalize();
            Vector3f coursecorrect = new Vector3f(0.577f, 0.577f, 0.577f);
            if (normal.y > 0) {
                coursecorrect = new Vector3f(0.01f, 1f, 0.01f);
            }

            // Create vertices
            Matrix4f matrix = poseStack.last().pose();
            VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(roundabout$TEXTURE_1));
            float size = 1f;

            // Use a simple normal (optional, mostly lighting-related)
            vertexConsumer.vertex(matrix, -size, -size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, -size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, size, size, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();
            vertexConsumer.vertex(matrix, -size, size, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(coursecorrect.x, coursecorrect.y, coursecorrect.z).endVertex();


            poseStack.popPose();
             **/

            ci.cancel();
            return;
        }
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 0)
    public ItemStack roundabout$Render2(ItemStack it){
        if (roundabout$ModifyEntity) {
            if (!dominant$Hand) {
                if (roundabout$RenderMainHand != null) {
                    return roundabout$RenderMainHand;
                }
            } else {
                if (roundabout$RenderOffHand != null) {
                    return roundabout$RenderOffHand;
                }
            }
        }
        return it;
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 1)
    public ItemStack roundabout$Render3(ItemStack it){
        if (roundabout$ModifyEntity) {
            if (dominant$Hand) {
                if (roundabout$RenderMainHand != null) {
                    return roundabout$RenderMainHand;
                }
            } else {
                if (roundabout$RenderOffHand != null) {
                    return roundabout$RenderOffHand;
                }
            }
        }
        return it;
    }

    @Shadow
    protected void renderArmWithItem(LivingEntity $$0, ItemStack $$1, ItemDisplayContext $$2, HumanoidArm $$3, PoseStack $$4, MultiBufferSource $$5, int $$6){
    }
    @Shadow
    public void render(PoseStack var1, MultiBufferSource var2, int var3, T var4, float var5, float var6, float var7, float var8, float var9, float var10) {
    }
}

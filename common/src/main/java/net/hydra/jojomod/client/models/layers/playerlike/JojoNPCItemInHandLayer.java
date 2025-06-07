package net.hydra.jojomod.client.models.layers.playerlike;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.entity.visages.JojoNPC;
import net.hydra.jojomod.client.models.visages.PlayerLikeModel;
import net.hydra.jojomod.event.index.Poses;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class JojoNPCItemInHandLayer<T extends JojoNPC, M extends PlayerLikeModel<T> & ArmedModel & HeadedModel> extends ItemInHandLayer<T, M> {
    private final ItemInHandRenderer itemInHandRenderer;
    private static final float X_ROT_MIN = (float) (-Math.PI / 6);
    private static final float X_ROT_MAX = (float) (Math.PI / 2);

    @Nullable Player play;

    public JojoNPCItemInHandLayer(RenderLayerParent<T, M> $$0, ItemInHandRenderer $$1) {
        super($$0, $$1);
        this.itemInHandRenderer = $$1;
    }

    @Override
    public void render(PoseStack $$0, MultiBufferSource $$1, int $$2, T entity, float $$4, float $$5, float $$6, float $$7, float $$8, float $$9) {
        if (entity.host != null && ((StandUser)entity.host).roundabout$getEffectiveCombatMode() && !entity.host.isUsingItem()){
            return;
        }
        if (entity.standPos == Poses.NONE || entity.standPos == null) {
            play = entity.host;
            boolean $$10 = entity.getMainArm() == HumanoidArm.RIGHT;
            ItemStack $$11 = $$10 ? entity.getOffhandItem() : entity.getMainHandItem();
            ItemStack $$12 = $$10 ? entity.getMainHandItem() : entity.getOffhandItem();
            if (!$$11.isEmpty() || !$$12.isEmpty()) {
                $$0.pushPose();
                if (this.getParentModel().young) {
                    float $$13 = 0.5F;
                    $$0.translate(0.0F, 0.75F, 0.0F);
                    $$0.scale(0.5F, 0.5F, 0.5F);
                }

                this.renderArmWithItem(entity, $$12, ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, HumanoidArm.RIGHT, $$0, $$1, $$2);
                this.renderArmWithItem(entity, $$11, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, HumanoidArm.LEFT, $$0, $$1, $$2);
                $$0.popPose();
            }
        }
    }
    @Override
    protected void renderArmWithItem(LivingEntity $$0, ItemStack $$1, ItemDisplayContext $$2, HumanoidArm $$3, PoseStack $$4, MultiBufferSource $$5, int $$6) {
        if ($$1.is(Items.SPYGLASS) && $$0.getUseItem() == $$1 && $$0.swingTime == 0) {
            this.renderArmWithSpyglass($$0, $$1, $$3, $$4, $$5, $$6);
        } else {
            if (!$$1.isEmpty()) {
                $$4.pushPose();
                this.getParentModel().translateToHand($$3, $$4);
                $$4.mulPose(Axis.XP.rotationDegrees(-90.0F));
                $$4.mulPose(Axis.YP.rotationDegrees(180.0F));
                boolean $$7 = $$3 == HumanoidArm.LEFT;
                $$4.translate((float)($$7 ? -1 : 1) / 16.0F, 0.125F, -0.5F);
                //Roundabout.LOGGER.info("1: "+$$0.getName()+" 2: "+($$0.getItemInHand(InteractionHand.MAIN_HAND) ==
                        //$$0.getUseItem())+" 3: "+($$1==$$0.getUseItem()));
                if (play != null){
                    this.itemInHandRenderer.renderItem(play, $$1, $$2, $$7, $$4, $$5, $$6);
                } else {
                    this.itemInHandRenderer.renderItem($$0, $$1, $$2, $$7, $$4, $$5, $$6);
                }
                $$4.popPose();
            }
        }
    }

    private void renderArmWithSpyglass(LivingEntity $$0, ItemStack $$1, HumanoidArm $$2, PoseStack $$3, MultiBufferSource $$4, int $$5) {
        $$3.pushPose();
        ModelPart $$6 = this.getParentModel().getHead();
        float $$7 = $$6.xRot;
        $$6.xRot = Mth.clamp($$6.xRot, (float) (-Math.PI / 6), (float) (Math.PI / 2));
        $$6.translateAndRotate($$3);
        $$6.xRot = $$7;
        CustomHeadLayer.translateToHead($$3, false);
        boolean $$8 = $$2 == HumanoidArm.LEFT;
        $$3.translate(($$8 ? -2.5F : 2.5F) / 16.0F, -0.0625F, 0.0F);
        if (play != null) {
            this.itemInHandRenderer.renderItem(play, $$1, ItemDisplayContext.HEAD, false, $$3, $$4, $$5);
        } else {
            this.itemInHandRenderer.renderItem($$0, $$1, ItemDisplayContext.HEAD, false, $$3, $$4, $$5);
        }
        $$3.popPose();
    }
}

package net.hydra.jojomod.client.models.minions.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.minions.VillagerMinionModel;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.entity.zombie_minion.VillagerMinion;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

public class ChimeraHeadLayer<T extends LivingEntity, A extends EntityModel<T>> extends RenderLayer<T, A> {

        private final EntityRenderDispatcher dispatcher;
        public ChimeraHeadLayer(EntityRendererProvider.Context context, LivingEntityRenderer<T, A> livingEntityRenderer) {
            super(livingEntityRenderer);
            this.dispatcher = context.getEntityRenderDispatcher();
        }

        protected boolean getRenderT(boolean $$1, boolean $$2, boolean $$3) {
            if ($$2 || $$1) {
                return true;
            } else {
                return $$3 ? true : false;
            }
        }
        float scale = 1;
        @Override
        public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T entity, float xx, float yy, float zz, float partialTicks, float var9, float var10) {
            if (entity instanceof BaseMinion vm) {

                //stack.mulPose(Axis.ZP.rotationDegrees(180f));
                if (getParentModel() instanceof VillagerMinionModel<?> vdm) {
                    renderWithHead(vm,vdm.head,poseStack,bufferSource,packedLight,entity,xx,yy,zz,partialTicks,var9,var10);
                }
            }
        }

        public void renderWithHead(BaseMinion vm, ModelPart head, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
                                   T entity, float xx, float yy, float zz, float partialTicks, float var9, float var10){
            boolean isHurt = vm.hurtTime > 0;
            float r = isHurt ? 1.0F : 1.0F;
            float g = isHurt ? 0.6F : 1.0F;
            float b = isHurt ? 0.6F : 1.0F;
            ClientUtil.pushPoseAndCooperate(poseStack, 41);
            head.translateAndRotate(poseStack);
            //ModStrayModels.LlamaHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            //ModStrayModels.CatHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            //ModStrayModels.SilverfishHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            //ModStrayModels.PolarBearHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            ModStrayModels.GoatHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            ClientUtil.popPoseAndCooperate(poseStack, 41);
        }
}

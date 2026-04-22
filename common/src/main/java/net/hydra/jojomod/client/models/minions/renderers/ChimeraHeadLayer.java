package net.hydra.jojomod.client.models.minions.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModStrayModels;
import net.hydra.jojomod.client.models.minions.*;
import net.hydra.jojomod.entity.zombie_minion.BaseMinion;
import net.hydra.jojomod.entity.zombie_minion.VillagerMinion;
import net.hydra.jojomod.item.ModItems;
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
                } else if (getParentModel() instanceof AxolotlMinionModel<?> vdm) {
                    ClientUtil.pushPoseAndCooperate(poseStack, 41);
                    vdm.body.translateAndRotate(poseStack);
                    renderWithHead(vm,vdm.head,poseStack,bufferSource,packedLight,entity,xx,yy,zz,partialTicks,var9,var10);
                    ClientUtil.popPoseAndCooperate(poseStack, 41);
                } else if (getParentModel() instanceof ParrotMinionModel vdm) {
                    renderWithHead(vm,vdm.head,poseStack,bufferSource,packedLight,entity,xx,yy,zz,partialTicks,var9,var10);
                } else if (getParentModel() instanceof DogMinionModel<?> vdm) {
                    renderWithHead(vm,vdm.head,poseStack,bufferSource,packedLight,entity,xx,yy,zz,partialTicks,var9,var10);
                } else if (getParentModel() instanceof OcelotMinionModel<?> vdm) {
                    renderWithHead(vm,vdm.head,poseStack,bufferSource,packedLight,entity,xx,yy,zz,partialTicks,var9,var10);
                } else if (getParentModel() instanceof ChickenMinionModel<?> vdm) {
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
            if (vm.getHeadItem().is(ModItems.CAT_REMAINS)){
                ModStrayModels.CatHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            } else if (vm.getHeadItem().is(ModItems.GOAT_REMAINS)){
                ModStrayModels.GoatHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            } else if (vm.getHeadItem().is(ModItems.LLAMA_REMAINS)){
                ModStrayModels.LlamaHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            } else if (vm.getHeadItem().is(ModItems.SILVERFISH_REMAINS)){
                ModStrayModels.SilverfishHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            } else if (vm.getHeadItem().is(ModItems.POLAR_BEAR_REMAINS)){
                ModStrayModels.PolarBearHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            } else if (vm.getHeadItem().is(ModItems.MOOSHROOM_REMAINS)){
                ModStrayModels.MooshroomHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            } else {
                ModStrayModels.VillagerHeadPart.render(vm, partialTicks, poseStack, bufferSource, packedLight, r, g, b,1);
            }
            ClientUtil.popPoseAndCooperate(poseStack, 41);
        }
}

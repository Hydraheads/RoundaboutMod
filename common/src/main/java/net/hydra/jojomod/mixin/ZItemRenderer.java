package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.MatrixUtil;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IItemRenderer;
import net.hydra.jojomod.block.FogBlock;
import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.client.ModItemModels;
import net.hydra.jojomod.item.ModItems;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

@Mixin(value = ItemRenderer.class, priority = 100)
public abstract class ZItemRenderer implements IItemRenderer {

    @Shadow
    @Final
    private BlockEntityWithoutLevelRenderer blockEntityRenderer;
    @Shadow
    @Final
    private ItemModelShaper itemModelShaper;
    @Unique
    @Override
    public BlockEntityWithoutLevelRenderer roundabout$getBlockEntityRenderer(){
        return this.blockEntityRenderer;
    }
    @ModifyVariable(method = "render", at = @At(value = "HEAD"), argsOnly = true)
    public BakedModel roundabout$render(
            BakedModel value, ItemStack stack, ItemDisplayContext renderMode, boolean leftHanded, PoseStack matrices,
            MultiBufferSource vertexConsumers, int light, int overlay, BakedModel $$7) {
        if (stack.is(ModItems.HARPOON) && renderMode != ItemDisplayContext.GUI && renderMode != ItemDisplayContext.GROUND) {
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.HARPOON_IN_HAND);
        } if (stack.is(ModItems.STREET_SIGN_DIO_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_DIO_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_DIO_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_DIO_HELD);
        } if (stack.is(ModItems.STREET_SIGN_RIGHT_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_RIGHT_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_RIGHT_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_RIGHT_HELD);
        } if (stack.is(ModItems.STREET_SIGN_STOP_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_STOP_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_STOP_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_STOP_HELD);
        } if (stack.is(ModItems.STREET_SIGN_YIELD_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_YIELD_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_YIELD_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_YIELD_HELD);
        } if (stack.is(ModItems.STREET_SIGN_DANGER_BLOCK_ITEM) && renderMode != ItemDisplayContext.GUI) {

            CompoundTag ct = stack.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_DANGER_HELD_D);
            } if (ctd == 2){
                return this.itemModelShaper.getModelManager().
                        getModel(ModItemModels.STREET_SIGN_DANGER_HELD_D2);
            }
            return this.itemModelShaper.getModelManager().
                    getModel(ModItemModels.STREET_SIGN_DANGER_HELD);
        }

        return value;
    }

    @Shadow
    public void render(ItemStack $$0, ItemDisplayContext $$1, boolean $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, int $$6, BakedModel $$7) {
    }
    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V", at = @At(value = "TAIL"))
    public void roundabout$renderStatic(
            LivingEntity $$0, ItemStack $$1, ItemDisplayContext $$2, boolean $$3, PoseStack $$4, MultiBufferSource $$5, Level $$6, int $$7, int $$8, int $$9, CallbackInfo ci) {

    }

    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$renderHead(ItemStack $$0, ItemDisplayContext $$1, boolean $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, int $$6, BakedModel $$7, CallbackInfo ci) {
        if (ClientUtil.getThrowFadeToTheEther() != 1 && !ClientUtil.isFabulous()){
            /**Sodium removes this function sometimes sooo*/
            if (!$$0.isEmpty()) {
                $$3.pushPose();
                boolean $$8 = $$1 == ItemDisplayContext.GUI || $$1 == ItemDisplayContext.GROUND || $$1 == ItemDisplayContext.FIXED;
                if ($$8) {
                    if ($$0.is(Items.TRIDENT)) {
                        $$7 = this.itemModelShaper.getModelManager().getModel(TRIDENT_MODEL);
                    } else if ($$0.is(Items.SPYGLASS)) {
                        $$7 = this.itemModelShaper.getModelManager().getModel(SPYGLASS_MODEL);
                    }
                }

                $$7.getTransforms().getTransform($$1).apply($$2, $$3);
                $$3.translate(-0.5F, -0.5F, -0.5F);
                if (!$$7.isCustomRenderer() && (!$$0.is(Items.TRIDENT) || $$8)) {
                    boolean $$10;
                    if ($$1 != ItemDisplayContext.GUI && !$$1.firstPerson() && $$0.getItem() instanceof BlockItem) {
                        Block $$9 = ((BlockItem)$$0.getItem()).getBlock();
                        $$10 = !($$9 instanceof HalfTransparentBlock) && !($$9 instanceof StainedGlassPaneBlock);
                    } else {
                        $$10 = true;
                    }

                    RenderType $$12 = ItemBlockRenderTypes.getRenderType($$0, $$10);
                    VertexConsumer $$14;
                    if (hasAnimatedTexture($$0) && $$0.hasFoil()) {
                        $$3.pushPose();
                        PoseStack.Pose $$13 = $$3.last();
                        if ($$1 == ItemDisplayContext.GUI) {
                            MatrixUtil.mulComponentWise($$13.pose(), 0.5F);
                        } else if ($$1.firstPerson()) {
                            MatrixUtil.mulComponentWise($$13.pose(), 0.75F);
                        }

                        if ($$10) {
                            $$14 = getCompassFoilBufferDirect($$4, $$12, $$13);
                        } else {
                            $$14 = getCompassFoilBuffer($$4, $$12, $$13);
                        }

                        $$3.popPose();
                    } else if ($$10) {
                        $$14 = getFoilBufferDirect($$4, $$12, true, $$0.hasFoil());
                    } else {
                        $$14 = getFoilBuffer($$4, $$12, true, $$0.hasFoil());
                    }

                    this.renderModelLists($$7, $$0, $$5, $$6, $$3, $$14);
                } else {
                    this.blockEntityRenderer.renderByItem($$0, $$1, $$3, $$4, $$5, $$6);
                }

                $$3.popPose();
            }

            if (!$$0.isEmpty() && $$0.getItem() instanceof BlockItem BE && BE.getBlock() instanceof FogBlock) {
                roundabout$yesThis($$0,$$1,$$2,$$3,$$4,$$5,$$6);
            }
            ci.cancel();
        }


    }
    @Inject(method = "render(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;IILnet/minecraft/client/resources/model/BakedModel;)V", at = @At(value = "TAIL"))
    public void roundabout$renderTail(ItemStack $$0, ItemDisplayContext $$1, boolean $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, int $$6, BakedModel $$7, CallbackInfo ci) {
        if (!$$0.isEmpty() && $$0.getItem() instanceof BlockItem BE && BE.getBlock() instanceof FogBlock) {
            roundabout$yesThis($$0,$$1,$$2,$$3,$$4,$$5,$$6);
        }
    }

    @Unique
    public void roundabout$yesThis(ItemStack $$0, ItemDisplayContext $$1, boolean $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, int $$6){
        boolean $$8 = $$1 == ItemDisplayContext.GUI;
        if ($$8){
            Lighting.setupForFlatItems();
            BakedModel $$7 = this.itemModelShaper.getModelManager().getModel(ModItemModels.FOG_BLOCK_ICON);
            $$3.pushPose();

            $$7.getTransforms().getTransform($$1).apply($$2, $$3);
            $$3.translate(-0.5F, -0.5F, 0.5F);

            if (!$$7.isCustomRenderer()) {
                boolean $$10;
                $$10 = true;

                RenderType $$12 = ItemBlockRenderTypes.getRenderType($$0, $$10);
                VertexConsumer $$14;
                $$14 = getFoilBufferDirect($$4, $$12, true, $$0.hasFoil());

                this.renderModelLists($$7, $$0, $$5, $$6, $$3, $$14);
            } else {
                this.blockEntityRenderer.renderByItem($$0, $$1, $$3, $$4, $$5, $$6);
            }

            $$3.popPose();
        }
    }

    @Shadow
    public static VertexConsumer getFoilBufferDirect(MultiBufferSource $$0, RenderType $$1, boolean $$2, boolean $$3) {
        return null;
    }

    @Shadow
    private void renderModelLists(BakedModel $$0, ItemStack $$1, int $$2, int $$3, PoseStack $$4, VertexConsumer $$5) {
    }

    @Shadow @Final private ItemColors itemColors;

    @Shadow
    public static VertexConsumer getFoilBuffer(MultiBufferSource $$0, RenderType $$1, boolean $$2, boolean $$3) {
        return null;
    }

    @Shadow
    public static VertexConsumer getCompassFoilBuffer(MultiBufferSource $$0, RenderType $$1, PoseStack.Pose $$2) {
        return null;
    }

    @Shadow
    public static VertexConsumer getCompassFoilBufferDirect(MultiBufferSource $$0, RenderType $$1, PoseStack.Pose $$2) {
        return null;
    }

    @Shadow
    protected static boolean hasAnimatedTexture(ItemStack $$0) {
        return false;
    }

    @Shadow @Final private static ModelResourceLocation TRIDENT_MODEL;

    @Shadow @Final private static ModelResourceLocation SPYGLASS_MODEL;

    @Shadow protected abstract void renderQuadList(PoseStack $$0, VertexConsumer $$1, List<BakedQuad> $$2, ItemStack $$3, int $$4, int $$5);

    @ModifyVariable(method = "getModel", at = @At(value = "STORE"), ordinal = 0)
    public BakedModel roundabout$render(
            BakedModel value,ItemStack $$0, @Nullable Level $$1, @Nullable LivingEntity $$2, int $$3) {
        if ($$0.is(Items.BOW) && $$2 !=null){
            if ($$2.getProjectile($$0).getItem() == ModItems.STAND_ARROW){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_BOW);
            } else if ($$2.getProjectile($$0).getItem() == ModItems.STAND_BEETLE_ARROW){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_BEETLE_BOW);
            } else if ($$2.getProjectile($$0).getItem() == ModItems.WORTHY_ARROW){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.WORTHY_BOW);
            }
        } if ($$0.is(Items.CROSSBOW) && $$2 !=null){
            if (CrossbowItem.containsChargedProjectile($$0,ModItems.STAND_ARROW)){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_ARROW_CROSSBOW);
            } else if (CrossbowItem.containsChargedProjectile($$0,ModItems.STAND_BEETLE_ARROW)){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_BEETLE_CROSSBOW);
            } else if (CrossbowItem.containsChargedProjectile($$0,ModItems.WORTHY_ARROW)){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STAND_WORTHY_CROSSBOW);
            }
        }if ($$0.is(Items.BOOK)){
            if (MainUtil.isDreadBook($$0)){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.DREAD_BOOK);
            }
        } if ($$0.is(ModItems.STREET_SIGN_DIO_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_DIO_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_DIO_D2);
            }
        } if ($$0.is(ModItems.STREET_SIGN_RIGHT_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_RIGHT_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_RIGHT_D2);
            }
        } if ($$0.is(ModItems.STREET_SIGN_STOP_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_STOP_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_STOP_D2);
            }
        } if ($$0.is(ModItems.STREET_SIGN_YIELD_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_YIELD_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_YIELD_D2);
            }
        } if ($$0.is(ModItems.STREET_SIGN_DANGER_BLOCK_ITEM)){
            CompoundTag ct = $$0.getOrCreateTagElement("BlockStateTag");
            int ctd = ct.getInt("damaged");
            if (ctd == 1){
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_DANGER_D);
            }
            if (ctd == 2) {
                return this.itemModelShaper.getModelManager().getModel(ModItemModels.STREET_SIGN_DANGER_D2);
            }
        }

        return value;
    }



    @Unique
    public void roundabout$putBulkData(VertexConsumer vc, PoseStack.Pose $$0, BakedQuad $$1, float $$2, float $$3, float $$4, int $$5, int $$6) {
        this.roundabout$putBulkData(vc, $$0, $$1, new float[]{1.0F, 1.0F, 1.0F, 1.0F}, $$2, $$3, $$4, new int[]{$$5, $$5, $$5, $$5}, $$6, false);
    }


    @Inject(method = "renderModelLists", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$renderModelLists(BakedModel $$0, ItemStack $$1, int $$2, int $$3, PoseStack $$4, VertexConsumer $$5, CallbackInfo ci) {
        /**Copy the original because sodium deletes it*/
        if (ClientUtil.getThrowFadeToTheEther() != 1 && !ClientUtil.isFabulous()){
            RandomSource $$6 = RandomSource.create();
            long $$7 = 42L;

            for (Direction $$8 : Direction.values()) {
                $$6.setSeed(42L);
                this.renderQuadList($$4, $$5, $$0.getQuads(null, $$8, $$6), $$1, $$2, $$3);
            }

            $$6.setSeed(42L);
            this.renderQuadList($$4, $$5, $$0.getQuads(null, null, $$6), $$1, $$2, $$3);
            ci.cancel();
        }
    }
    @Inject(method = "renderQuadList", at = @At(value = "HEAD"), cancellable = true)
    public void roundabout$renderQuadList(PoseStack $$0, VertexConsumer $$1, List<BakedQuad> $$2, ItemStack $$3, int $$4, int $$5, CallbackInfo ci) {
        if (ClientUtil.getThrowFadeToTheEther() != 1 && !ClientUtil.isFabulous()){
            boolean $$6 = !$$3.isEmpty();
            PoseStack.Pose $$7 = $$0.last();

            for (BakedQuad $$8 : $$2) {
                int $$9 = -1;
                if ($$6 && $$8.isTinted()) {
                    $$9 = this.itemColors.getColor($$3, $$8.getTintIndex());
                }

                float $$10 = (float)($$9 >> 16 & 0xFF) / 255.0F;
                float $$11 = (float)($$9 >> 8 & 0xFF) / 255.0F;
                float $$12 = (float)($$9 & 0xFF) / 255.0F;
                roundabout$putBulkData($$1,$$7, $$8, $$10, $$11, $$12, $$4, $$5);
            }
            ci.cancel();
        }
    }

    @Unique
    public void roundabout$putBulkData(VertexConsumer vc, PoseStack.Pose $$0, BakedQuad $$1, float[] $$2, float $$3, float $$4, float $$5, int[] $$6, int $$7, boolean $$8) {
        float[] $$9 = new float[]{$$2[0], $$2[1], $$2[2], $$2[3]};
        int[] $$10 = new int[]{$$6[0], $$6[1], $$6[2], $$6[3]};
        int[] $$11 = $$1.getVertices();
        Vec3i $$12 = $$1.getDirection().getNormal();
        Matrix4f $$13 = $$0.pose();
        Vector3f $$14 = $$0.normal().transform(new Vector3f((float)$$12.getX(), (float)$$12.getY(), (float)$$12.getZ()));
        int $$15 = 8;
        int $$16 = $$11.length / 8;

        try (MemoryStack $$17 = MemoryStack.stackPush()) {
            ByteBuffer $$18 = $$17.malloc(DefaultVertexFormat.BLOCK.getVertexSize());
            IntBuffer $$19 = $$18.asIntBuffer();

            for (int $$20 = 0; $$20 < $$16; $$20++) {
                $$19.clear();
                $$19.put($$11, $$20 * 8, 8);
                float $$21 = $$18.getFloat(0);
                float $$22 = $$18.getFloat(4);
                float $$23 = $$18.getFloat(8);
                float $$27;
                float $$28;
                float $$29;
                if ($$8) {
                    float $$24 = (float)($$18.get(12) & 255) / 255.0F;
                    float $$25 = (float)($$18.get(13) & 255) / 255.0F;
                    float $$26 = (float)($$18.get(14) & 255) / 255.0F;
                    $$27 = $$24 * $$9[$$20] * $$3;
                    $$28 = $$25 * $$9[$$20] * $$4;
                    $$29 = $$26 * $$9[$$20] * $$5;
                } else {
                    $$27 = $$9[$$20] * $$3;
                    $$28 = $$9[$$20] * $$4;
                    $$29 = $$9[$$20] * $$5;
                }

                int $$33 = $$10[$$20];
                float $$34 = $$18.getFloat(16);
                float $$35 = $$18.getFloat(20);
                Vector4f $$36 = $$13.transform(new Vector4f($$21, $$22, $$23, 1.0F));
                //Roundabout.LOGGER.info("HM?");
                vc.vertex($$36.x(), $$36.y(), $$36.z(), $$27, $$28, $$29, ClientUtil.getThrowFadeToTheEther(), $$34, $$35, $$7, $$33, $$14.x(), $$14.y(), $$14.z());
            }
        }
    }

}

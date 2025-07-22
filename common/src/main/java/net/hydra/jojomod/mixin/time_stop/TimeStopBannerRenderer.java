package net.hydra.jojomod.mixin.time_stop;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BannerRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BannerRenderer.class)
public class TimeStopBannerRenderer {
    /**
     * Banners need to freeze in stopped time, but they are set up in a complicated way in the code,
     * so they freeze in a complicated way.
     */

    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/BannerBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At("HEAD"), cancellable = true)
    public void roundabout$render(BannerBlockEntity $$0, float $$1, PoseStack $$2,
                                  MultiBufferSource $$3, int $$4, int $$5, CallbackInfo ci) {
        if ($$0.getLevel() != null && ((TimeStop)$$0.getLevel()).inTimeStopRange($$0.getBlockPos())) {
            ci.cancel();
            List<Pair<Holder<BannerPattern>, DyeColor>> $$6 = $$0.getPatterns();
            float $$7 = 0.6666667F;
            boolean $$8 = $$0.getLevel() == null;
            $$2.pushPose();
            long $$9;
            if ($$8) {
                $$9 = 0L;
                $$2.translate(0.5F, 0.5F, 0.5F);
                this.pole.visible = true;
            } else {
                $$9 = $$0.getLevel().getGameTime();
                BlockState $$11 = $$0.getBlockState();
                if ($$11.getBlock() instanceof BannerBlock) {
                    $$2.translate(0.5F, 0.5F, 0.5F);
                    float $$12 = -RotationSegment.convertToDegrees($$11.getValue(BannerBlock.ROTATION));
                    $$2.mulPose(Axis.YP.rotationDegrees($$12));
                    this.pole.visible = true;
                } else {
                    $$2.translate(0.5F, -0.16666667F, 0.5F);
                    float $$13 = -$$11.getValue(WallBannerBlock.FACING).toYRot();
                    $$2.mulPose(Axis.YP.rotationDegrees($$13));
                    $$2.translate(0.0F, -0.3125F, -0.4375F);
                    this.pole.visible = false;
                }
            }

            $$2.pushPose();
            $$2.scale(0.6666667F, -0.6666667F, -0.6666667F);
            VertexConsumer $$14 = ModelBakery.BANNER_BASE.buffer($$3, RenderType::entitySolid);
            this.pole.render($$2, $$14, $$4, $$5);
            this.bar.render($$2, $$14, $$4, $$5);
            BlockPos $$15 = $$0.getBlockPos();
            this.flag.y = -32.0F;
            renderPatterns($$2, $$3, $$4, $$5, this.flag, ModelBakery.BANNER_BASE, true, $$6);
            $$2.popPose();
            $$2.popPose();
        }
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    @Final
    private ModelPart flag;
    @Shadow
    @Final
    private ModelPart pole;
    @Shadow
    @Final
    private ModelPart bar;
    @Shadow
    public static void renderPatterns(
            PoseStack $$0, MultiBufferSource $$1, int $$2, int $$3, ModelPart $$4, Material $$5, boolean $$6, List<Pair<Holder<BannerPattern>, DyeColor>> $$7
    ) {
    }
}

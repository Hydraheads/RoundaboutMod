package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.hydra.jojomod.client.StandIcons;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandPowers;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(ScreenEffectRenderer.class)
public abstract class ZScreenEffectRenderer {
    /**Use this to hide the block effect covering your screen when your player is suffocating during pilot*/
    @Shadow
    @Nullable
    private static BlockState getViewBlockingState(Player $$0) {
        return null;
    }

    @Shadow
    private static void renderTex(TextureAtlasSprite $$0, PoseStack $$1) {
    }

    @Shadow
    private static void renderWater(Minecraft $$0, PoseStack $$1) {
    }

    @Shadow
    private static void renderFire(Minecraft $$0, PoseStack $$1) {
    }

    @Unique
    private static BlockState roundabout$getViewBlockingState(LivingEntity $$0) {
        BlockPos.MutableBlockPos $$1 = new BlockPos.MutableBlockPos();

        for (int $$2 = 0; $$2 < 8; $$2++) {
            double $$3 = $$0.getX() + (double)(((float)(($$2 >> 0) % 2) - 0.5F) * $$0.getBbWidth() * 0.8F);
            double $$4 = $$0.getEyeY() + (double)(((float)(($$2 >> 1) % 2) - 0.5F) * 0.1F);
            double $$5 = $$0.getZ() + (double)(((float)(($$2 >> 2) % 2) - 0.5F) * $$0.getBbWidth() * 0.8F);
            $$1.set($$3, $$4, $$5);
            BlockState $$6 = $$0.level().getBlockState($$1);
            if ($$6.getRenderShape() != RenderShape.INVISIBLE && $$6.isViewBlocking($$0.level(), $$1)) {
                return $$6;
            }
        }

        return null;
    }

    /**Anti xrayx-ray measures for justice pilot mode block clipping*/
    @Inject(method = "renderScreenEffect(Lnet/minecraft/client/Minecraft;Lcom/mojang/blaze3d/vertex/PoseStack;)V", at = @At(value = "HEAD"),cancellable = true)
    private static void roundabout$renderScreenEffect(Minecraft $$0, PoseStack $$1, CallbackInfo ci) {

        Player $$2 = $$0.player;
        if ($$2 != null) {
            roundabout$renderFire($$0,$$1,$$2);
            StandUser standComp = ((StandUser) $$2);
            StandPowers powers = standComp.roundabout$getStandPowers();
            StandEntity piloting = powers.getPilotingStand();
            if (powers.isPiloting() && piloting != null && piloting.isAlive() && !piloting.isRemoved() ) {

                if (!$$2.noPhysics) {
                    BlockState $$3 = roundabout$getViewBlockingState(piloting);
                    if ($$3 != null) {
                        renderTex($$0.getBlockRenderer().getBlockModelShaper().getParticleIcon($$3), $$1);
                    }
                }

                if (!$$0.player.isSpectator()) {
                    if ($$0.player.isEyeInFluid(FluidTags.WATER)) {
                        renderWater($$0, $$1);
                    }

                    if ($$0.player.isOnFire()) {
                        renderFire($$0, $$1);
                    }
                }
                ci.cancel();
            }
        }

    }

    @Unique
    private static void roundabout$renderFire(Minecraft mc, PoseStack pose, Player pl) {
        byte bt = ((StandUser)pl).roundabout$getOnStandFire();
        if (bt > 0){

        }
        /**
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.depthFunc(519);
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        TextureAtlasSprite textureatlassprite = StandIcons.STAND_FIRE.sprite();
        RenderSystem.setShaderTexture(0, textureatlassprite.atlasLocation());
        float f = textureatlassprite.getU0();
        float f1 = textureatlassprite.getU1();
        float f2 = (f + f1) / 2.0F;
        float f3 = textureatlassprite.getV0();
        float f4 = textureatlassprite.getV1();
        float f5 = (f3 + f4) / 2.0F;
        float f6 = textureatlassprite.uvShrinkRatio();
        float f7 = Mth.lerp(f6, f, f2);
        float f8 = Mth.lerp(f6, f1, f2);
        float f9 = Mth.lerp(f6, f3, f5);
        float f10 = Mth.lerp(f6, f4, f5);
        float f11 = 1.0F;

        for(int i = 0; i < 2; ++i) {
            pose.pushPose();
            float f12 = -0.5F;
            float f13 = 0.5F;
            float f14 = -0.5F;
            float f15 = 0.5F;
            float f16 = -0.5F;
            pose.translate((float)(-(i * 2 - 1)) * 0.24F, -0.3F, 0.0F);
            pose.mulPose(Axis.YP.rotationDegrees((float)(i * 2 - 1) * 10.0F));
            Matrix4f matrix4f = pose.last().pose();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            bufferbuilder.vertex(matrix4f, -0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f8, f10).endVertex();
            bufferbuilder.vertex(matrix4f, 0.5F, -0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f7, f10).endVertex();
            bufferbuilder.vertex(matrix4f, 0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f7, f9).endVertex();
            bufferbuilder.vertex(matrix4f, -0.5F, 0.5F, -0.5F).color(1.0F, 1.0F, 1.0F, 0.9F).uv(f8, f9).endVertex();
            BufferUploader.drawWithShader(bufferbuilder.end());
            pose.popPose();
        }

        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
        RenderSystem.depthFunc(515);
         **/
    }
}

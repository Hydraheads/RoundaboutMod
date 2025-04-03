package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.corpses.FallenMob;
import net.hydra.jojomod.entity.corpses.FallenSpider;
import net.hydra.jojomod.entity.projectile.CrossfireHurricaneEntity;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.index.StandFireType;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersMagiciansRed;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntityRenderer.class)
public abstract class ZLivingEntityRenderer<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> implements RenderLayerParent<T, M> {


    protected ZLivingEntityRenderer(EntityRendererProvider.Context $$0) {
        super($$0);
    }

    @Unique
    private static int roundabout$PackRed(int $$0, int $$1) {
        return $$0 | $$1 << 16;
    }
    @Unique
    private static int roundabout$PackGreen(int $$0, int $$1) {
        return $$0 | $$1 << 8;
    }
    @Unique
    private static int roundabout$PackBlue(int $$0, int $$1) {
        return $$0 | $$1;
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$render(T $$0, float $$1, float $$2, PoseStack $$3, MultiBufferSource $$4, int $$5, CallbackInfo ci) {

        Entity entity = ((StandUser)$$0).roundabout$getBoundTo();
        if (entity != null) {
            this.roundabout$renderBound($$0, $$2, $$3, $$4, entity);
        }
    }

    /**This is where red bind and other string-like moves will be rendered*/
    @Unique
    private <E extends Entity> void roundabout$renderBound(T victim, float p_115463_, PoseStack p_115464_, MultiBufferSource mb, E binder) {
        p_115464_.pushPose();
        Vec3 vec3 = binder.getRopeHoldPosition(p_115463_);
        if (binder instanceof LivingEntity lv){
            StandUser su = ((StandUser)lv);
            StandEntity stand = su.roundabout$getStand();
            if (stand != null){
                vec3 = stand.getRopeHoldPosition(p_115463_);
            }
        }
        double d0 = (double)(Mth.lerp(p_115463_, victim.yBodyRotO, victim.yBodyRot) * ((float)Math.PI / 180F)) + (Math.PI / 2D);
        Vec3 vec31 = victim.getLeashOffset(p_115463_);
        double d1 = Math.cos(d0) * vec31.z + Math.sin(d0) * vec31.x;
        double d2 = Math.sin(d0) * vec31.z - Math.cos(d0) * vec31.x;
        double d3 = Mth.lerp((double)p_115463_, victim.xo, victim.getX()) + d1;
        double d4 = Mth.lerp((double)p_115463_, victim.yo, victim.getY()) + vec31.y;
        double d5 = Mth.lerp((double)p_115463_, victim.zo, victim.getZ()) + d2;
        p_115464_.translate(d1, vec31.y, d2);
        float f = (float)(vec3.x - d3);
        float f1 = (float)(vec3.y - d4);
        float f2 = (float)(vec3.z - d5);
        float f3 = 0.025F;
        VertexConsumer vertexconsumer = mb.getBuffer(RenderType.leash());
        Matrix4f matrix4f = p_115464_.last().pose();
        float f4 = Mth.invSqrt(f * f + f2 * f2) * 0.025F / 2.0F;
        float f5 = f2 * f4;
        float f6 = f * f4;
        BlockPos blockpos = BlockPos.containing(victim.getEyePosition(p_115463_));
        BlockPos blockpos1 = BlockPos.containing(binder.getEyePosition(p_115463_));
        int i = this.getBlockLightLevel(victim, blockpos);
        int j = roundabout$getBlockLightLevel(binder, blockpos1);
        int k = victim.level().getBrightness(LightLayer.SKY, blockpos);
        int l = victim.level().getBrightness(LightLayer.SKY, blockpos1);

        for(int i1 = 0; i1 <= 24; ++i1) {
            roundabout$addVertexPair(binder, vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.025F, f5, f6, i1, false);
        }

        for(int j1 = 24; j1 >= 0; --j1) {
            roundabout$addVertexPair(binder, vertexconsumer, matrix4f, f, f1, f2, i, j, k, l, 0.025F, 0.0F, f5, f6, j1, true);
        }

        p_115464_.popPose();
    }

    @Unique
    protected <E extends Entity>  int roundabout$getBlockLightLevel(E $$0, BlockPos $$1) {
        return $$0.isOnFire() ? 15 : $$0.level().getBrightness(LightLayer.BLOCK, $$1);
    }
    private static void roundabout$addVertexPair(Entity binder, VertexConsumer p_174308_, Matrix4f p_254405_, float p_174310_, float p_174311_, float p_174312_, int p_174313_, int p_174314_, int p_174315_, int p_174316_, float p_174317_, float p_174318_, float p_174319_, float p_174320_, int p_174321_, boolean p_174322_) {
        float f = (float)p_174321_ / 24.0F;
        int i = (int)Mth.lerp(f, (float)p_174313_, (float)p_174314_);
        int j = (int)Mth.lerp(f, (float)p_174315_, (float)p_174316_);
        int k = LightTexture.pack(i, j);
        int tc = binder.tickCount % 9;
        float f1 = 0.7F + (float)(Math.random()*0.3);
        if (tc > 5) {
            f1*=0.92F;
        }
        if (tc > 2) {
            f1*=0.84F;
        }
        Vec3 color = roundabout$getBindColor(binder);
        float f2 = (float) (color.x() * f1);
        float f3 = (float) (color.y() * f1);
        float f4 = (float) (color.z() * f1);
        float f5 = p_174310_ * f;
        float f6 = p_174311_ > 0.0F ? p_174311_ * f * f : p_174311_ - p_174311_ * (1.0F - f) * (1.0F - f);
        float f7 = p_174312_ * f;
        float width = 0.05F;
        p_174308_.vertex(p_254405_, f5 - p_174319_ - width, f6 + p_174318_ + width, f7 + p_174320_ + width).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
        p_174308_.vertex(p_254405_, f5 + p_174319_ + width, f6 + p_174317_ - p_174318_ - width, f7 - p_174320_ - width).color(f2, f3, f4, 1.0F).uv2(k).endVertex();
    }

    @Unique
    private static Vec3 roundabout$getBindColor(Entity binder) {
        if (binder instanceof LivingEntity LE && ((StandUser)binder).roundabout$getStandPowers() instanceof PowersMagiciansRed PMR) {
            byte sft = PMR.getFireColor();
            if (sft == StandFireType.BLUE.id){
                return new Vec3(0,0.8F,0.949F);
            } else if (sft == StandFireType.PURPLE.id){
                return new Vec3(0.839F,0.122F,0.949F);
            } else if (sft == StandFireType.GREEN.id){
                return new Vec3(0.345F,1F,0.2F);
            } else if (sft == StandFireType.DREAD.id){
                return new Vec3(0.788F,0,0);
            }
        }
        return new Vec3(0.969F,0.569F,0.102F);
    }
    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$rotations(T $$0, PoseStack $$1, float $$2, float $$3, float $$4, CallbackInfo ci) {
        if ($$0 instanceof Player) {
            byte playerP = ((IPlayerEntity)$$0).roundabout$GetPos();

            /*Dodge makes you lean forward visually*/
            if (playerP == PlayerPosIndex.DODGE_FORWARD || playerP == PlayerPosIndex.DODGE_BACKWARD) {
                int dodgeTime = ((IPlayerEntity)$$0).roundabout$getDodgeTime();
                float $$5;
                if (dodgeTime > -1) {
                    if (dodgeTime > 5) {
                        $$5 = ((11 - ((float) dodgeTime + 1 + $$4 - 1.0F)) / 20.0F * 1.6F);
                    } else {
                        $$5 = ((float) dodgeTime + 1 + $$4 - 1.0F) / 20.0F * 1.6F;
                    }
                    $$5 = Mth.sqrt($$5);
                    if ($$5 > 1.0F) {
                        $$5 = 1.0F;
                    }

                    if (playerP == PlayerPosIndex.DODGE_FORWARD) {
                        $$5 *= -1;
                    }

                    $$1.mulPose(Axis.XP.rotationDegrees($$5 * 45));
                }
            }
        } else if ($$0 instanceof FallenMob FM){
            /*fog corpse / corpse control*/
            int tickTock = FM.ticksThroughPhases;
            if (FM.getPhasesFull()){
                tickTock = 10;
                FM.ticksThroughPhases = 10;
            }
            float yes = Math.min(10, tickTock + $$4);
            if (FM.getActivated()) {
                yes = Math.max(0,tickTock- $$4);
            }
            float $$5 = (yes /10);
            if ($$0 instanceof FallenSpider FS){
                $$1.mulPose(Axis.XP.rotationDegrees($$5 * 180));
                $$1.translate(0,-$$5*(1*FM.getBbHeight()),0);
            } else {
                $$1.mulPose(Axis.XP.rotationDegrees($$5 * 90));
                $$1.translate(0,-$$5*(0.5*FM.getBbHeight()),-($$5*0.15));
            }
        }
    }



    @Inject(method = "getOverlayCoords", at = @At(value = "HEAD"), cancellable = true)
    private static void roundabout$GetOverlayCoords(LivingEntity $$0, float $$1, CallbackInfoReturnable<Integer> ci) {
        if (((StandUser)$$0).roundabout$getStoredDamageByte() > 0) {
            ci.setReturnValue(roundabout$PackRed(
                    ((StandUser)$$0).roundabout$getStoredDamageByte(),
                    10));
        }
    }
    @Shadow
    public M getModel() {
        return null;
    }

    @Shadow
    protected float getFlipDegrees(T $$0) {
        return 90.0F;
    }

}

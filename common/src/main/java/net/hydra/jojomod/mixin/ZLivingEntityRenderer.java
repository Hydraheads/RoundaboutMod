package net.hydra.jojomod.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
    private static int roundaboutPackRed(int $$0, int $$1) {
        return $$0 | $$1 << 16;
    }
    @Unique
    private static int roundaboutPackGreen(int $$0, int $$1) {
        return $$0 | $$1 << 8;
    }
    @Unique
    private static int roundaboutPackBlue(int $$0, int $$1) {
        return $$0 | $$1;
    }
    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At(value = "TAIL"), cancellable = true)
    private void roundabout$render(T $$0, PoseStack $$1, float $$2, float $$3, float $$4, CallbackInfo ci) {
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
        }
    }



    @Inject(method = "getOverlayCoords", at = @At(value = "HEAD"), cancellable = true)
    private static void roundaboutGetOverlayCoords(LivingEntity $$0, float $$1, CallbackInfoReturnable<Integer> ci) {
        if (((StandUser)$$0).roundabout$getStoredDamageByte() > 0) {
            ci.setReturnValue(roundaboutPackRed(
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

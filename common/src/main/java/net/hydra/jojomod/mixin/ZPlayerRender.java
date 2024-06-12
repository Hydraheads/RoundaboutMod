package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IEntityAndData;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class ZPlayerRender extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public ZPlayerRender(EntityRendererProvider.Context $$0, PlayerModel<AbstractClientPlayer> $$1, float $$2) {
        super($$0, $$1, $$2);
    }

    private static AbstractClientPlayer ACP;
    private static InteractionHand IH;

    @Inject(method = "getArmPose", at = @At(value = "HEAD"))
    private static void roundaboutGetArmPose(AbstractClientPlayer $$0, InteractionHand $$1, CallbackInfoReturnable<HumanoidModel.ArmPose> ci) {
        ACP = $$0;
        IH = $$1;
    }
    @ModifyVariable(method = "getArmPose", at = @At(value = "STORE"),ordinal = 0)
    private static ItemStack roundaboutGetArmPose2(ItemStack $$0) {
        if (IH == InteractionHand.MAIN_HAND && ((IEntityAndData)ACP).getRoundaboutRenderMainHand() != null){
            $$0 = ((IEntityAndData)ACP).getRoundaboutRenderMainHand();
        } if (IH == InteractionHand.OFF_HAND && ((IEntityAndData)ACP).getRoundaboutRenderOffHand() != null){
            $$0 = ((IEntityAndData)ACP).getRoundaboutRenderOffHand();
        }
        return $$0;
    }

    @Shadow
    public ResourceLocation getTextureLocation(AbstractClientPlayer var1) {
        return null;
    }
}

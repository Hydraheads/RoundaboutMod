package net.hydra.jojomod.mixin.items;

import com.mojang.blaze3d.vertex.PoseStack;
import net.hydra.jojomod.item.BowlerHatItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.UUID;

import static org.apache.logging.log4j.ThreadContext.isEmpty;

@Mixin(PlayerRenderer.class)
public class BowlerHatPlayerRendererMixin {
    /**Reward a winner of a raid with the executioner axe template*/
    @Unique
    public boolean roundabout$hasRewarded = false;

    @Shadow
    protected void renderHand(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, AbstractClientPlayer abstractClientPlayer, ModelPart modelPart, ModelPart modelPart2) {

    }

    @Inject(method = "getArmPose(Lnet/minecraft/client/player/AbstractClientPlayer;Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/client/model/HumanoidModel$ArmPose;", at = @At("RETURN"), cancellable = true)
    private static void bowlerHatPlayerHandRenderer(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack stack = player.getItemInHand(hand);

        if (!stack.isEmpty() && stack.getItem() instanceof BowlerHatItem) {
            cir.setReturnValue(HumanoidModel.ArmPose.EMPTY);
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Final
    private Set<UUID> heroesOfTheVillage;
    @Final
    private ServerLevel level;
}
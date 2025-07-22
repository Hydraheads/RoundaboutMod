package net.hydra.jojomod.mixin.justice;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.client.models.corpses.renderers.FallenSkeletonRenderer;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersJustice;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkeletonRenderer.class)
public class JusticeSkeletonRenderer {

    /**Justice makes a skeleton render differently. Why? Well, you can morph into a skeleton, and it uses the skeleton
     * renderer. But you can also make a skeleton look like a team member with the justice team colors. Lets say you
     * select green team- your skeleton morph will look like a green team skeleton.*/

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/monster/AbstractSkeleton;)Lnet/minecraft/resources/ResourceLocation;", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$getTexture(AbstractSkeleton $$0, CallbackInfoReturnable<ResourceLocation> cir) {
        if ($$0 != null){
            StandUser user = (StandUser) $$0;
            if (user.roundabout$getEmulator() instanceof Player PE){
                StandUser user2 = ((StandUser) PE);
                if (user2.roundabout$getStandPowers() instanceof PowersJustice PJ){
                    IPlayerEntity ipe = ((IPlayerEntity) PE);
                    byte bt = ipe.roundabout$getTeamColor();
                    if (bt == 1){
                        cir.setReturnValue(FallenSkeletonRenderer.FALLEN_SKELETON_LOCATION_B);
                    } else if (bt == 2){
                        cir.setReturnValue(FallenSkeletonRenderer.FALLEN_SKELETON_LOCATION_R);
                    } else if (bt == 3){
                        cir.setReturnValue(FallenSkeletonRenderer.FALLEN_SKELETON_LOCATION_G);
                    }
                }
            }
        }
    }
}

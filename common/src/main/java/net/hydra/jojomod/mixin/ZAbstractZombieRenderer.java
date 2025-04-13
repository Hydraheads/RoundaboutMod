package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.entity.corpses.FallenZombieRenderer;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.event.powers.stand.PowersJustice;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractZombieRenderer.class)
public class ZAbstractZombieRenderer {

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/monster/Zombie;)Lnet/minecraft/resources/ResourceLocation;", at = @At(value = "HEAD"), cancellable = true)
    private void roundabout$getTexture(Zombie $$0, CallbackInfoReturnable<ResourceLocation> cir) {
        if ($$0 != null){
            StandUser user = (StandUser) $$0;
            if (user.roundabout$getEmulator() instanceof Player PE){
                StandUser user2 = ((StandUser) PE);
                if (user2.roundabout$getStandPowers() instanceof PowersJustice PJ){
                    IPlayerEntity ipe = ((IPlayerEntity) PE);
                    byte bt = ipe.roundabout$getTeamColor();
                    if (bt == 1){
                        cir.setReturnValue(FallenZombieRenderer.FALLEN_ZOMBIE_LOCATION_B);
                    } else if (bt == 2){
                        cir.setReturnValue(FallenZombieRenderer.FALLEN_ZOMBIE_LOCATION_R);
                    } else if (bt == 3){
                        cir.setReturnValue(FallenZombieRenderer.FALLEN_ZOMBIE_LOCATION_G);
                    }
                }
            }
        }
    }
}

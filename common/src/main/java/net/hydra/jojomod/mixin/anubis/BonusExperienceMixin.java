package net.hydra.jojomod.mixin.anubis;

import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.item.MaxStandDiscItem;
import net.hydra.jojomod.stand.powers.PowersAnubis;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ExperienceOrb.class)
public abstract class BonusExperienceMixin {


    @ModifyVariable(method = "playerTouch",
            at = @At(value = "STORE"))
    private int roundabout$addBonusXp(int value, Player instance) {
        StandUser SU = (StandUser) instance;
        float extra = 1F;
        if (SU.roundabout$getStandPowers() instanceof PowersAnubis PA ) {
            boolean bypass = (SU.roundabout$getStandDisc().getItem() instanceof MaxStandDiscItem) || instance.isCreative();
            extra = 1 + (bypass ? 5F : 0.5F * ((IPlayerEntity) instance).roundabout$getStandLevel() / PA.getMaxLevel()  ) ;

            return (int)(value*extra);
        }

        return value;
    }

}

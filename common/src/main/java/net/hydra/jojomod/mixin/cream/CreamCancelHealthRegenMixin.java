package net.hydra.jojomod.mixin.cream;

import net.hydra.jojomod.access.IFoodData;
import net.hydra.jojomod.event.ModEffects;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersCream;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = FoodData.class, priority = 100)
public class CreamCancelHealthRegenMixin {
    /**Bleed limits natural regen*/

    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    private void CreamCancelHealthRegen(Player $$0, CallbackInfo ci) {
        if ($$0 instanceof StandUser) {
            if (((StandUser) $$0).roundabout$getStandPowers() instanceof PowersCream PC && PC.insideVoidInt > 0) {
                ci.cancel();
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
}

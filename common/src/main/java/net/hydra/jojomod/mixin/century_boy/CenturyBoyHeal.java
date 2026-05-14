package net.hydra.jojomod.mixin.century_boy;

import net.hydra.jojomod.access.IFoodData;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.Powers20thCenturyBoy;
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

@Mixin(FoodData.class)
public class CenturyBoyHeal {

    @Inject(method = "tick", at=@At(value = "HEAD"), cancellable = true)
    private void FoodHeal(Player player, CallbackInfo ci){
        if (player instanceof StandUser user && user.roundabout$getStandPowers() instanceof Powers20thCenturyBoy pCB) {
            if (pCB.invincibleState) {
                ci.cancel();
            }
        }
    }



}

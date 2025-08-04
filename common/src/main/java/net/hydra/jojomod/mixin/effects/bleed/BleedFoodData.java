package net.hydra.jojomod.mixin.effects.bleed;

import net.hydra.jojomod.access.IFoodData;
import net.hydra.jojomod.event.ModEffects;
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
public class BleedFoodData implements IFoodData {
    /**Bleed limits natural regen*/

    @Unique
    @Override
    public void roundabout$setFoodLevel(int food){
        foodLevel = food;
    }
    @Unique
    @Override
    public void roundabout$setExhaustionLevel(float exhaustion){
        exhaustionLevel = exhaustion;
    }
    @Unique
    @Override
    public void roundabout$setSaturationLevel(float saturation){
        saturationLevel = saturation;
    }
    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$foodData(Player $$0, CallbackInfo ci) {
        if ($$0.hasEffect(ModEffects.BLEED)) {
            int amp = $$0.getEffect(ModEffects.BLEED).getAmplifier();
            float multiplier = 0.5F;
            if (amp == 1){
                multiplier = 0.25F;
            }
            boolean $$2 = $$0.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
            if ($$2 && this.saturationLevel > 0.0F && $$0.isHurt() && this.foodLevel >= 20) {
                roundabout$p1($$0);
                this.tickTimer++;
                if (this.tickTimer >= 10) {
                    float $$3 = Math.min(this.saturationLevel, 6.0F);
                    if (amp < 2) {
                        $$0.heal(($$3 / 6.0F)*multiplier);
                        this.addExhaustion($$3);
                    }
                    this.tickTimer = 0;
                }
                ci.cancel();
            } else if ($$2 && this.foodLevel >= 18 && $$0.isHurt()) {
                roundabout$p1($$0);
                this.tickTimer++;
                if (this.tickTimer >= 80) {
                    if (amp < 2) {
                        $$0.heal(multiplier);
                        this.addExhaustion(6.0F);
                    }
                    this.tickTimer = 0;
                }
                ci.cancel();
            }
        }
    }

    @Unique
    public void roundabout$p1(Player $$0){
        Difficulty $$1 = $$0.level().getDifficulty();
        this.lastFoodLevel = this.foodLevel;
        if (this.exhaustionLevel > 4.0F) {
            this.exhaustionLevel -= 4.0F;
            if (this.saturationLevel > 0.0F) {
                this.saturationLevel = Math.max(this.saturationLevel - 1.0F, 0.0F);
            } else if ($$1 != Difficulty.PEACEFUL) {
                this.foodLevel = Math.max(this.foodLevel - 1, 0);
            }
        }
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    private int foodLevel;
    @Shadow
    private float saturationLevel;
    @Shadow
    private float exhaustionLevel;
    @Shadow
    private int tickTimer;
    @Shadow
    private int lastFoodLevel;
    @Shadow
    public void addExhaustion(float $$0) {
    }
}

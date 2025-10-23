package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.access.AccessFateFoodData;
import net.hydra.jojomod.util.MainUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FoodData.class)
public abstract class FateFoodDataMixin implements AccessFateFoodData {
    @Shadow public abstract void eat(int i, float f);

    @Shadow private int foodLevel;
    @Shadow private int lastFoodLevel;
    @Shadow private float exhaustionLevel;
    @Shadow private float saturationLevel;
    @Shadow private int tickTimer;

    @Shadow public abstract void addExhaustion(float f);

    /**Makes vampires not replen from food*/
    @Unique
    public Player rdbt$player = null;
    @Unique
    public float rdbt$alternateSaturation;
    @Unique
    @Override
    public void rdbt$setPlayer(Player pl){
        rdbt$player = pl;
    }

    @Inject(method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$eat(Item $$0, ItemStack $$1, CallbackInfo ci) {
        if (rdbt$player != null){
            if (FateTypes.hasBloodHunger(rdbt$player)){
                if ($$0.isEdible()) {
                    this.eat(MainUtil.getBloodAmount($$1), MainUtil.getSaturationAmount($$1));
                }
                ci.cancel();
            }
        }
    }

    @Inject(method = "eat(IF)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$eat2(int $$0, float $$1, CallbackInfo ci) {
        if (rdbt$player != null){
            if (FateTypes.hasBloodHunger(rdbt$player)){
                this.foodLevel = Math.min($$0 + this.foodLevel, 20);
                this.rdbt$alternateSaturation = Math.min(this.rdbt$alternateSaturation + (float)$$0 * $$1 * 2.0F, (float)this.foodLevel);
                ci.cancel();
            }
        }
    }

    @Inject(method = "getSaturationLevel()F", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$getSaturationLevel(CallbackInfoReturnable<Float> cir) {
        if (rdbt$player != null){
            if (FateTypes.hasBloodHunger(rdbt$player)){
                cir.setReturnValue(this.rdbt$alternateSaturation);
            }
        }
    }
    @Inject(method = "setSaturation(F)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$setSaturation(float $$0, CallbackInfo ci) {
        if (rdbt$player != null){
            if (FateTypes.hasBloodHunger(rdbt$player)){
                this.rdbt$alternateSaturation = $$0;
                ci.cancel();
            }
        }
    }
    @Inject(method = "addAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$addAdditionalSaveData(CompoundTag $$0, CallbackInfo ci) {
        if (rdbt$player != null){
            if (FateTypes.hasBloodHunger(rdbt$player)){
                $$0.putInt("foodLevel", this.foodLevel);
                $$0.putInt("foodTickTimer", this.tickTimer);
                $$0.putFloat("foodSaturationLevel", this.rdbt$alternateSaturation);
                $$0.putFloat("foodExhaustionLevel", this.exhaustionLevel);
                ci.cancel();
            }
        }
    }
    @Inject(method = "readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$readAdditionalSaveData(CompoundTag $$0, CallbackInfo ci) {
        if (rdbt$player != null){
            if (FateTypes.hasBloodHunger(rdbt$player)){
                if ($$0.contains("foodLevel", 99)) {
                    this.foodLevel = $$0.getInt("foodLevel");
                    this.tickTimer = $$0.getInt("foodTickTimer");
                    this.rdbt$alternateSaturation = $$0.getFloat("foodSaturationLevel");
                    this.exhaustionLevel = $$0.getFloat("foodExhaustionLevel");
                }
                ci.cancel();
            }
        }
    }
    /**Vampires do not starve, remove the starve code here*/
    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$tickVamp(Player $$0, CallbackInfo ci) {
        if (FateTypes.hasBloodHunger(rdbt$player)){
            ci.cancel();
            this.saturationLevel = 0;
            Difficulty $$1 = $$0.level().getDifficulty();
            this.lastFoodLevel = this.foodLevel;
            if (this.exhaustionLevel > 4.0F) {
                this.exhaustionLevel -= 4.0F;
                if (this.rdbt$alternateSaturation > 0.0F) {
                    this.rdbt$alternateSaturation = Math.max(this.rdbt$alternateSaturation - 1.0F, 0.0F);
                } else if ($$1 != Difficulty.PEACEFUL) {
                    this.foodLevel = Math.max(this.foodLevel - 1, 0);
                }
            }

            boolean $$2 = $$0.level().getGameRules().getBoolean(GameRules.RULE_NATURAL_REGENERATION);
            if ($$2 && this.rdbt$alternateSaturation > 0.0F && $$0.isHurt() && this.foodLevel >= 20) {
                this.tickTimer++;
                if (this.tickTimer >= 10) {
                    float $$3 = Math.min(this.rdbt$alternateSaturation, 6.0F);
                    $$0.heal($$3 / 6.0F);
                    this.addExhaustion($$3);
                    this.tickTimer = 0;
                }
            } else if ($$2 && this.foodLevel >= 18 && $$0.isHurt()) {
                this.tickTimer++;
                if (this.tickTimer >= 80) {
                    $$0.heal(1.0F);
                    this.addExhaustion(6.0F);
                    this.tickTimer = 0;
                }
            } else if (this.foodLevel <= 0) {
                this.tickTimer++;
                /**Vampires do not starve, remove the starve code here*/
                if (this.tickTimer >= 80) {
                    //if ($$0.getHealth() > 10.0F || $$1 == Difficulty.HARD || $$0.getHealth() > 1.0F && $$1 == Difficulty.NORMAL) {
                        //$$0.hurt($$0.damageSources().starve(), 1.0F);
                    //}

                    this.tickTimer = 0;
                }
            } else {
                this.tickTimer = 0;
            }
        }
    }
}

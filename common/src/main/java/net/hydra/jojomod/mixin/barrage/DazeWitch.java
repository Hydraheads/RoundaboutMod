package net.hydra.jojomod.mixin.barrage;

import net.hydra.jojomod.access.IMob;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Witch.class)
public abstract class DazeWitch extends Raider {

    /**This mixin is in relation to barrages disabling mobs from attacking or doing things.
     * The daze that barrages inflict prevent witches from using and drinking potions*/

    @Inject(method = "isDrinkingPotion", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$isDrinkingPotion(CallbackInfoReturnable<Boolean> ci) {
        if (((StandUser) this).roundabout$isDazed() ||
                (!((StandUser)this).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this).roundabout$getStandPowers().disableMobAiAttack())) {
            ci.setReturnValue(false);
        }
    }

    @Inject(method = "setUsingItem", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$setUsingItem(boolean $$0, CallbackInfo ci) {
        if (((StandUser) this).roundabout$isDazed() ||
                (!((StandUser)this).roundabout$getStandDisc().isEmpty() &&
                        ((StandUser)this).roundabout$getStandPowers().disableMobAiAttack())) {
            ((Witch) (Object) this).getEntityData().set(DATA_USING_ITEM, false);
            ci.cancel();
            return;
        }

        if (((IMob)this).rdbt$getStolen()){
            Potion $$3 = Potions.POISON;
            if (Math.random() < 0.5){
                $$3 = Potions.HARMING;
            }
            this.setItemSlot(EquipmentSlot.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), $$3));
        }
    }



    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    protected DazeWitch(EntityType<? extends Raider> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Shadow
    private static final EntityDataAccessor<Boolean> DATA_USING_ITEM = SynchedEntityData.defineId(Witch.class, EntityDataSerializers.BOOLEAN);

}

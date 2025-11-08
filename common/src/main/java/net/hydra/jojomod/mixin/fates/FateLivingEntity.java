package net.hydra.jojomod.mixin.fates;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class FateLivingEntity extends Entity {


    /**vampires can't get direct potion effects from food*/
    @Inject(method = "addEatEffect(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;)V", at = @At(value = "HEAD"), cancellable = true)
    protected void rdbt$addEatEffect(ItemStack $$0, Level $$1, LivingEntity $$2, CallbackInfo ci) {
        if (FateTypes.hasBloodHunger((LivingEntity) (Object) this)){
            if ($$0.is(Items.GOLDEN_APPLE) || $$0.is(Items.ENCHANTED_GOLDEN_APPLE))
                return;
            ci.cancel();
        }
    }
    /**vampires getSlowerWhileDrowning*/
    @Inject(method = "getFluidFallingAdjustedMovement(DZLnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;", at = @At(value = "HEAD"), cancellable = true)
    protected void rdbt$getFluidFallingAdjustedMovement(double $$0, boolean $$1, Vec3 $$2,CallbackInfoReturnable<Vec3> cir) {
        if (FateTypes.hasBloodHunger((LivingEntity) (Object) this)) {
            if (((StandUser)this).roundabout$getDrowning() || getAirSupply() <= 0) {
                if (!this.isNoGravity() && !this.isSprinting()) {
                    return;
                } else {
                    cir.setReturnValue($$2.scale(ClientNetworking.getAppropriateConfig().vampireSettings.drownSpeedModifier));
                }
            }
        }
    }

    public FateLivingEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
}

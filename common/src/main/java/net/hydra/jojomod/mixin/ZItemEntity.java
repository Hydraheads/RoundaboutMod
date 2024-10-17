package net.hydra.jojomod.mixin;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IItemEntityAccess;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ZItemEntity extends Entity implements IItemEntityAccess {
    @Shadow
    private int pickupDelay;

    @Shadow
    private int age;

    @Shadow
    public ItemStack getItem() {
        return null;
    }

    public ZItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }
    @Inject(method = "tick", at = @At(value = "HEAD"))
    protected void roundabout$tick(CallbackInfo ci) {
        if (((IEntityAndData)this).roundabout$getNoGravTicks() > 0){
            ((IEntityAndData)this).roundabout$setNoGravTicks(((IEntityAndData)this).roundabout$getNoGravTicks()-1);
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.04, 0.0));
            }
        }
        ((IEntityAndData)this).roundabout$tickQVec();
    }

    @Override
    public void roundabout$TickPickupDelay(){
        if (this.getItem().isEmpty()) {
            this.discard();
        } else {
            if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
                this.pickupDelay--;
            }
            if (!this.level().isClientSide && this.age >= 6000) {
                this.discard();
            } else if (this.age >= 5999){
                this.age++;
            }
        }

    }
}

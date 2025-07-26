package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.IEntityAndData;
import net.hydra.jojomod.access.IItemEntityAccess;
import net.hydra.jojomod.access.IItemEntityTimeStopAccess;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.item.FogBlockItem;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class TimeStopItemEntity extends Entity implements IItemEntityTimeStopAccess {

    /**This mixin makes items tick in age so they can be picked up after being thrown even while time is stopped*/
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


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */


    @Shadow
    private int pickupDelay;

    @Shadow
    private int age;

    @Shadow
    public ItemStack getItem() {
        return null;
    }

    @Shadow public abstract int getAge();


    public TimeStopItemEntity(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

}

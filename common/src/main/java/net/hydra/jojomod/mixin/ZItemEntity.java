package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IItemEntityAccess;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemEntity.class)
public class ZItemEntity implements IItemEntityAccess {
    @Shadow
    private int pickupDelay;
    public void RoundaboutTickPickupDelay(){
        if (this.pickupDelay > 0 && this.pickupDelay != 32767) {
            this.pickupDelay--;
        }
    }
}

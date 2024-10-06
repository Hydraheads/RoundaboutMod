package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IBoatAccess;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(Boat.class)
public class ZBoatAccess implements IBoatAccess {
    @Shadow
    private void tickLerp(){}
    @Shadow
    private Boat.Status status;
    @Shadow
    private Boat.Status oldStatus;
    @Shadow
    private float outOfControlTicks;
    @Shadow
    private Boat.Status getStatus() {
        return null;
    }

    @Unique
    public void roundabout$TickLerp(){
        this.oldStatus = this.status;
        this.status = this.getStatus();
        if (this.status != Boat.Status.UNDER_WATER && this.status != Boat.Status.UNDER_FLOWING_WATER) {
            this.outOfControlTicks = 0.0F;
        }
        tickLerp();
        ((Boat)(Object)this).setDeltaMovement(Vec3.ZERO);
    }
}

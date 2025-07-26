package net.hydra.jojomod.mixin.time_stop;

import net.hydra.jojomod.access.IBoatAccess;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Boat.class)
public class TimeStopBoatAccess implements IBoatAccess {

    /**A mixin to make boats freeze properly in stopped time instead of going crazy*/

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


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

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

}

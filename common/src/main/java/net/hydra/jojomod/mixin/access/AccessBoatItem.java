package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IBoatItemAccess;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.item.BoatItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(BoatItem.class)
public class AccessBoatItem implements IBoatItemAccess {

    /**There is no reason for these to be private or protected, we should be able to tap into them.*/

    @Unique
    @Override
    public Boat.Type roundabout$getType(){
        return type;
    }

    @Unique
    @Override
    public Boat roundabout$getBoat(Level $$0, Vec3 $$1){

        return (Boat)(this.hasChest
                ? new ChestBoat($$0, $$1.x, $$1.y, $$1.z)
                : new Boat($$0, $$1.x, $$1.y, $$1.z));
    }

    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    @Final
    private Boat.Type type;
    @Shadow
    @Final
    private boolean hasChest;
}

package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IMinecartItemAccess;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.MinecartItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecartItem.class)
public class ZMinecartItem implements IMinecartItemAccess {
    /**SP/TW throw access to minecarts*/
    @Shadow
    @Final
    AbstractMinecart.Type type;

    @Override
    public AbstractMinecart.Type roundabout$getType(){
        return type;
    }
}

package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IMinecartItemAccess;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.MinecartItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(MinecartItem.class)
public class AccessMinecartItem implements IMinecartItemAccess {
    /**SP/TW throw access to minecarts*/
    @Override
    public AbstractMinecart.Type roundabout$getType(){
        return type;
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */
    @Shadow
    @Final
    AbstractMinecart.Type type;
}

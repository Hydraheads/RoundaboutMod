package net.hydra.jojomod.mixin.access;

import net.hydra.jojomod.access.IMinecartTNT;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.vehicle.MinecartTNT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import javax.annotation.Nullable;

@Mixin(MinecartTNT.class)
public class AccessMinecartTNT implements IMinecartTNT {
    /**There is no reason for these to be private or protected, we should be able to tap into them.*/
    @Override
    public void roundabout$explode(@Nullable DamageSource $$0, double $$1){
        explode($$0,$$1);
    }


    /**Shadows, ignore
     * -------------------------------------------------------------------------------------------------------------
     * */

    @Shadow
    protected void explode(@Nullable DamageSource $$0, double $$1){
    }

}

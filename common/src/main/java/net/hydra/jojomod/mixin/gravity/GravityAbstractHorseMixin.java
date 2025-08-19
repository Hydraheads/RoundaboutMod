package net.hydra.jojomod.mixin.gravity;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractHorse.class)
public class GravityAbstractHorseMixin {
    @ModifyVariable(method = "calculateFallDamage(FF)I", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float rdbt$diminishFallDamage(float value) {
        return value * (float) Math.sqrt(GravityAPI.getGravityStrength(((Entity) (Object) this)));
    }
}

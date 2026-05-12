package net.hydra.jojomod.mixin.tusk;

import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.event.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Projectile.class)
public abstract class OffBalanceMixin extends Entity implements TraceableEntity {

    public OffBalanceMixin(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @ModifyVariable(method = "shoot", at = @At("HEAD"), index = 8, argsOnly = true)
    private float roundabout$modifyAccuracy(float h) {
        if (this.getOwner() != null) {
            if (this.getOwner() instanceof LivingEntity LE) {
                if (LE.hasEffect(ModEffects.UNBALANCED)) {
                    return h * (3 * (LE.getEffect(ModEffects.UNBALANCED).getAmplifier()+1) );
                }
            }
        }

        return h;
    }
}

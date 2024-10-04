package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Warden.class)
public abstract class ZWarden extends Monster {
    protected ZWarden(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundabout$tick(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel $$0) {
            if (((TimeStop)this.level()).inTimeStopRange(this)){
                ((ServerLevel) this.level()).sendParticles(ParticleTypes.SCULK_SOUL, this.getX(), this.getY()+(this.getBbHeight()/2), this.getZ(),
                        5, this.getBbWidth()/4, this.getBbHeight()/4, this.getBbWidth()/4, 0.1);
            }
        }
    }
}

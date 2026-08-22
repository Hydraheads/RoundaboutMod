package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.ClientUtil;
import net.hydra.jojomod.event.index.PowerTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Blaze.class)
public abstract class ZBlaze extends Monster {
    @Inject(method = "aiStep", at = @At("HEAD"), cancellable = true)
    private void roundabout$aiStepBlaze(CallbackInfo ci) {
        //Blazes only emit sounds and particles in the same dimensional layer
        if (PowerTypes.isExistentiallyElsewhere(this) ||
                (this.level().isClientSide() && PowerTypes.isInADifferentExistenceNoTE(this, ClientUtil.getPlayer()))){
            if (!this.onGround() && this.getDeltaMovement().y < 0.0) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
            }
            if (this.level().isClientSide() &&
                    !PowerTypes.isInADifferentExistenceNoTE(this, ClientUtil.getPlayer())) {
                if (this.random.nextInt(24) == 0 && !this.isSilent()) {
                    this.level()
                            .playLocalSound(
                                    this.getX() + 0.5,
                                    this.getY() + 0.5,
                                    this.getZ() + 0.5,
                                    SoundEvents.BLAZE_BURN,
                                    this.getSoundSource(),
                                    1.0F + this.random.nextFloat(),
                                    this.random.nextFloat() * 0.7F + 0.3F,
                                    false
                            );
                }

                for (int $$0 = 0; $$0 < 2; $$0++) {
                    this.level().addParticle(ParticleTypes.LARGE_SMOKE, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0.0, 0.0, 0.0);
                }
            }
            super.aiStep();
            ci.cancel();
            return;
        }
    }
    protected ZBlaze(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

}

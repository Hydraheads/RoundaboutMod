package net.hydra.jojomod.mixin.green_day;

import net.hydra.jojomod.Roundabout;


import net.hydra.jojomod.access.IPermaCasting;
import net.hydra.jojomod.entity.projectile.KnifeEntity;
import net.hydra.jojomod.entity.projectile.MatchEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.PermanentZoneCastInstance;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;

import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.entity.projectile.Projectile;
;
import net.minecraft.world.entity.projectile.SpectralArrow;
import net.minecraft.world.entity.projectile.ThrownEgg;
import net.minecraft.world.level.Level;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(Projectile.class)
public abstract class ZProjectile extends Entity implements TraceableEntity {

    protected ZProjectile(EntityType<? extends Projectile> $$0, Level $$1) {super($$0, $$1);}
    int moldedTicks = 0;

    @Inject(method = "tick", at = @At(value = "HEAD"))
    public void DestroyProjectilesInMoldField(CallbackInfo info) {
        if (((IPermaCasting) this.level()).roundabout$inPermaCastRange(this.getOnPos(), PermanentZoneCastInstance.MOLD_FIELD)) {
            LivingEntity glumbo = ((IPermaCasting)this.level()).roundabout$inPermaCastRangeEntity(this.getOnPos(), PermanentZoneCastInstance.MOLD_FIELD);
            Boolean Moldable = ((Projectile)(Object)this instanceof ThrownEgg) ||
            ((Projectile)(Object)this instanceof KnifeEntity)||
            ((Projectile)(Object)this instanceof MatchEntity)||
            ((Projectile)(Object)this instanceof Arrow)||
            ((Projectile)(Object)this instanceof SpectralArrow);
            Boolean lower = glumbo.getY() > this.getY();
            if(Moldable && lower) {
                for (int i = 0; i < 1; i = i + 1) {

                    this.level().addParticle(ModParticles.MOLD_DUST,
                            this.getX(),
                            this.getY(),
                            this.getZ(),
                            0, 0, 0);
                }
                moldedTicks = moldedTicks + 1;
                if (moldedTicks > 3) {
                    for (int i = 0; i < 43; i = i + 1) {
                        float randXf = Roundabout.RANDOM.nextFloat(-0.1f, 0.1f);
                        float randYf = Roundabout.RANDOM.nextFloat(-0.1f, 0.1f);
                        float randZf = Roundabout.RANDOM.nextFloat(-0.1f, 0.1f);
                        double randX = (double) randXf;
                        double randy = (double) randYf;
                        double randz = (double) randZf;
                        this.level().addParticle(ModParticles.MOLD,
                                this.getX() + randX * 2,
                                this.getY() + randy * 2,
                                this.getZ() + randz * 2,
                                randXf, randYf, randZf);


                    }
                    this.setDeltaMovement(0,0,0);
                }
                if(moldedTicks >4){
                    this.discard();
                }
            }
        }
    }



}

package net.hydra.jojomod.chocolatedisco.mixin;

import net.hydra.jojomod.chocolatedisco.IChocolateDiscoProjectileAccess;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class ChocolateDiscoProjectileMixin
        implements IChocolateDiscoProjectileAccess {

    @Unique
    private float roundabout$standDamage = 0.0F;

    @Unique
    private boolean roundabout$standDamagePlayersOnly = false;

    @Override
    public float roundabout$getStandDamage() {
        return roundabout$standDamage;
    }

    @Override
    public void roundabout$setStandDamage(
            float damage
    ) {
        roundabout$standDamage = damage;
    }

    @Override
    public boolean roundabout$isStandDamagePlayersOnly() {
        return roundabout$standDamagePlayersOnly;
    }

    @Override
    public void roundabout$setStandDamagePlayersOnly(
            boolean playersOnly
    ) {
        roundabout$standDamagePlayersOnly = playersOnly;
    }

    @Inject(
            method = "onHit",
            at = @At("TAIL")
    )
    private void roundabout$applyStandDamage(
            HitResult hitResult,
            CallbackInfo ci
    ) {

        if (roundabout$standDamage <= 0.0F) {
            return;
        }

        if (!(hitResult instanceof EntityHitResult entityHitResult)) {
            return;
        }

        Entity target =
                entityHitResult.getEntity();

        if (roundabout$standDamagePlayersOnly
                && !(target instanceof ServerPlayer)) {
            return;
        }

        Projectile projectile =
                (Projectile) (Object) this;

        target.invulnerableTime = 0;

        target.hurt(
                ModDamageTypes.of(
                        projectile.level(),
                        ModDamageTypes.STAND,
                        projectile,
                        projectile.getOwner()
                ),
                roundabout$standDamage
        );
    }
}
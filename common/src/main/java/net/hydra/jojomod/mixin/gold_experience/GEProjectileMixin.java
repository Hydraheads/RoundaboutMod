package net.hydra.jojomod.mixin.gold_experience;

import net.hydra.jojomod.access.IGEProjectile;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Projectile.class)
public abstract class GEProjectileMixin implements IGEProjectile {

    @Unique
    private boolean roundabout$geTreeImbue = false;

    @Override
    public boolean roundabout$hasGETreeImbue() {
        return this.roundabout$geTreeImbue;
    }

    @Override
    public void roundabout$setGETreeImbue(boolean value) {
        this.roundabout$geTreeImbue = value;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void roundabout$saveGETreeImbue(CompoundTag tag, CallbackInfo ci) {
        if (this.roundabout$geTreeImbue) {
            tag.putBoolean("GETreeImbue", true);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void roundabout$loadGETreeImbue(CompoundTag tag, CallbackInfo ci) {
        this.roundabout$geTreeImbue = tag.getBoolean("GETreeImbue");
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void roundabout$onGEImbuedProjectileHit(EntityHitResult result, CallbackInfo ci) {
        Projectile self = (Projectile) (Object) this;
        PowersGoldExperience.applyTreeImbueOnProjectileHit(self, result.getEntity());
    }
}
package net.hydra.jojomod.mixin.gold_experience;

import net.hydra.jojomod.access.IGEProjectile;
import net.hydra.jojomod.stand.powers.PowersGoldExperience;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(Projectile.class)
public abstract class GEProjectileMixin implements IGEProjectile {

    @Unique
    private boolean roundabout$geTreeImbue = false;

    @Unique
    @Nullable
    private UUID roundabout$geTreeImbueOwner = null;

    @Override
    public boolean roundabout$hasGETreeImbue() {
        return this.roundabout$geTreeImbue;
    }

    @Override
    public void roundabout$setGETreeImbue(boolean value) {
        this.roundabout$geTreeImbue = value;
    }

    @Nullable
    public UUID roundabout$getGETreeImbueOwner() {
        return this.roundabout$geTreeImbueOwner;
    }

    public void roundabout$setGETreeImbueOwner(@Nullable UUID ownerId) {
        this.roundabout$geTreeImbueOwner = ownerId;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void roundabout$saveGETreeImbue(CompoundTag tag, CallbackInfo ci) {
        if (this.roundabout$geTreeImbue) {
            tag.putBoolean("GETreeImbue", true);
        }
        if (this.roundabout$geTreeImbueOwner != null) {
            tag.putUUID("GETreeImbueOwner", this.roundabout$geTreeImbueOwner);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void roundabout$loadGETreeImbue(CompoundTag tag, CallbackInfo ci) {
        this.roundabout$geTreeImbue = tag.getBoolean("GETreeImbue");
        this.roundabout$geTreeImbueOwner = tag.hasUUID("GETreeImbueOwner") ? tag.getUUID("GETreeImbueOwner") : null;
    }

    @Inject(method = "onHitBlock", at = @At("HEAD"))
    private void roundabout$onGEImbuedProjectileHitBlock(BlockHitResult result, CallbackInfo ci) {
        Projectile self = (Projectile) (Object) this;
        PowersGoldExperience.applyTreeImbueOnBlockHit(self, result);
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void roundabout$onGEImbuedProjectileHit(EntityHitResult result, CallbackInfo ci) {
        Projectile self = (Projectile) (Object) this;
        PowersGoldExperience.applyTreeImbueOnProjectileHit(self, result.getEntity());
    }
}
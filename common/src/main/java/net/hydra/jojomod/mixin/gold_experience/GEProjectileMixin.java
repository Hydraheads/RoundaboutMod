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
    private boolean sba$geTreeImbue = false;

    @Unique
    @Nullable
    private UUID sba$geTreeImbueOwner = null;

    @Override
    public boolean sba$hasGETreeImbue() {
        return this.sba$geTreeImbue;
    }

    @Override
    public void sba$setGETreeImbue(boolean value) {
        this.sba$geTreeImbue = value;
    }

    @Nullable
    public UUID sba$getGETreeImbueOwner() {
        return this.sba$geTreeImbueOwner;
    }

    public void sba$setGETreeImbueOwner(@Nullable UUID ownerId) {
        this.sba$geTreeImbueOwner = ownerId;
    }

    @Inject(method = "addAdditionalSaveData", at = @At("HEAD"))
    private void sba$saveGETreeImbue(CompoundTag tag, CallbackInfo ci) {
        if (this.sba$geTreeImbue) {
            tag.putBoolean("GETreeImbue", true);
        }
        if (this.sba$geTreeImbueOwner != null) {
            tag.putUUID("GETreeImbueOwner", this.sba$geTreeImbueOwner);
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("HEAD"))
    private void sba$loadGETreeImbue(CompoundTag tag, CallbackInfo ci) {
        this.sba$geTreeImbue = tag.getBoolean("GETreeImbue");
        this.sba$geTreeImbueOwner = tag.hasUUID("GETreeImbueOwner") ? tag.getUUID("GETreeImbueOwner") : null;
    }

    @Inject(method = "onHitBlock", at = @At("HEAD"))
    private void sba$onGEImbuedProjectileHitBlock(BlockHitResult result, CallbackInfo ci) {
        Projectile self = (Projectile) (Object) this;
        PowersGoldExperience.applyTreeImbueOnBlockHit(self, result);
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void sba$onGEImbuedProjectileHit(EntityHitResult result, CallbackInfo ci) {
        Projectile self = (Projectile) (Object) this;
        PowersGoldExperience.applyTreeImbueOnProjectileHit(self, result.getEntity());
    }
}
package net.hydra.jojomod.mixin;

import net.hydra.jojomod.access.IFishingRodAccess;
import net.hydra.jojomod.access.IProjectileAccess;
import net.hydra.jojomod.entity.TimeMovingProjectile;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(FishingHook.class)
public abstract class ZFishingHook extends Entity implements IFishingRodAccess {

    public ZFishingHook(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }


    @Shadow
    @Final
    private RandomSource syncronizedRandom;

    @Shadow
    @Nullable
    public Player getPlayerOwner() {
        return null;
    }

    @Shadow
    @Nullable
    private boolean shouldStopFishing(Player $$0) {
        return true;
    }

    @Inject(method = "tick", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$SetPosForTS(CallbackInfo ci) {
        if (((TimeStop)this.level()).inTimeStopRange(((FishingHook)(Object)this)) && ((IProjectileAccess) this).roundabout$getRoundaboutIsTimeStopCreated()) {
            this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level().getGameTime());
            Player $$0 = this.getPlayerOwner();
            if ($$0 == null) {
                this.discard();
            } else if (this.level().isClientSide || !this.shouldStopFishing($$0)) {
                super.tick();
                TimeMovingProjectile.tick((FishingHook) (Object) this);
                this.checkInsideBlocks();
                this.reapplyPosition();
            }
            ci.cancel();
        }
    }

    public void roundabout$UpdateRodInTS(){
        Player $$0 = this.getPlayerOwner();
        if ($$0 == null) {
            this.discard();
        } else if (this.level().isClientSide || !this.shouldStopFishing($$0)) {
            this.checkInsideBlocks();
            this.reapplyPosition();
        }
    }

}

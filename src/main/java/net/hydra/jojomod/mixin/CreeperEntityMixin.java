package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.powers.StandUser;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public class CreeperEntityMixin extends HostileEntity {
    @Shadow
    private int lastFuseTime;
    @Shadow
    private int currentFuseTime;

    protected CreeperEntityMixin(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    protected void RoundaboutTick(CallbackInfo ci) {
        if (((StandUser)this).isDazed()) {
            if (((CreeperEntity)(Object)this).isAlive()) {
                lastFuseTime = currentFuseTime;
            }

            this.currentFuseTime -= 1;
            if (this.currentFuseTime < 0) {
                this.currentFuseTime = 0;
            }
            super.tick();
            ci.cancel();
        }
    }
}

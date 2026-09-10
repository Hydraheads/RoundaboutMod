package net.hydra.jojomod.mixin;

import net.hydra.jojomod.event.index.PowerTypes;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersKingCrimson;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ShulkerBullet;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.UUID;

@Mixin(ShulkerBullet.class)
public abstract class ZShulkerBullet extends Entity {
    @Shadow
    @Nullable
    private Entity finalTarget;

    @Shadow
    @Nullable
    private UUID targetId;

    public ZShulkerBullet(EntityType<?> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void rdbt$tickTEShu(CallbackInfo ci) {
        if (finalTarget != null){
            if (PowerTypes.isInADifferentExistenceNoTE(finalTarget,this)){
                if (finalTarget instanceof LivingEntity LE &&
                        ((StandUser)LE).roundabout$getStandPowers() instanceof
                        PowersKingCrimson pkc && pkc.timeEraseActive
                && pkc.activeClone != null){
                    this.finalTarget = pkc.activeClone;
                    this.targetId = pkc.activeClone.getUUID();
                } else {
                    this.finalTarget = null;
                    this.targetId = null;
                }
            }
        }
    }

}

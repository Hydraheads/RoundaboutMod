package net.hydra.jojomod.mixin;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(WitherBoss.class)
public abstract class ZWitherBoss extends Monster implements PowerableMob, RangedAttackMob {
    protected ZWitherBoss(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Shadow
    public void setAlternativeTarget(int i, int j) {
    }
    private boolean roundabout$antiRecurse = false;
    @Inject(method = "setAlternativeTarget", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$setAlternativeTarget(int $$0, int $$1, CallbackInfo ci) {
        if (!roundabout$antiRecurse) {
            Entity ent = this.level().getEntity($$1);
            if (ent instanceof StandEntity SE) {
                if (SE.getUser() != null && !(SE.getUser() instanceof StandEntity)) {
                    if (SE.getUser() instanceof ServerPlayer PE && !PE.gameMode.isCreative() && !PE.isSpectator()){
                        ci.cancel();
                    }
                    $$1 = SE.getUser().getId();
                    roundabout$antiRecurse = true;
                    setAlternativeTarget($$0,$$1);
                    roundabout$antiRecurse = false;
                    ci.cancel();
                } else if (SE.getUser() != null) {
                    if (SE.getUser() instanceof ServerPlayer PE && !PE.gameMode.isCreative() && !PE.isSpectator()){
                        ci.cancel();
                    }
                    $$1 = SE.getUser().getId();
                    roundabout$antiRecurse = true;
                    setAlternativeTarget($$0,$$1);
                    roundabout$antiRecurse = false;
                    ci.cancel();
                }
            }
        }
    }
}
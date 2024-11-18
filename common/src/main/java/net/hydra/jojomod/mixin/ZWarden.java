package net.hydra.jojomod.mixin;

import net.hydra.jojomod.client.ClientNetworking;
import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.powers.TimeStop;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Warden.class)
public abstract class ZWarden extends Monster {
    protected ZWarden(EntityType<? extends Monster> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Unique
    private boolean roundabout$hasSentTSMessage = false;
    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void roundabout$tick(CallbackInfo ci) {
        if (ClientNetworking.getAppropriateConfig().wardenMovesInStoppedTime) {
            if (this.level() instanceof ServerLevel $$0) {
                if (((TimeStop) this.level()).inTimeStopRange(this)) {
                    if (!roundabout$hasSentTSMessage) {
                        roundabout$hasSentTSMessage = true;
                        for (int j = 0; j < $$0.players().size(); ++j) {
                            ServerPlayer serverPlayerEntity = ((ServerLevel) this.level()).players().get(j);
                            if (serverPlayerEntity.distanceTo(this) < 120) {
                                serverPlayerEntity.displayClientMessage(Component.translatable("roundabout.warden_ts_movement").withStyle(ChatFormatting.RED), true);
                            }
                        }
                    }
                    ((ServerLevel) this.level()).sendParticles(ParticleTypes.SCULK_SOUL, this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ(),
                            2, this.getBbWidth() / 4, this.getBbHeight() / 4, this.getBbWidth() / 4, 0.1);
                    if (this.tickCount %3 == 1) {
                        ((ServerLevel) this.level()).sendParticles(ModParticles.WARDEN_CLOCK, this.getX(), this.getY() + (this.getBbHeight() / 2), this.getZ(),
                                1, this.getBbWidth()*1.3, this.getBbHeight() / 4, this.getBbWidth()*1.3, 0.02);
                    }
                }
            }
        }
    }
    @Inject(method = "canTargetEntity", at = @At(value = "HEAD"),cancellable = true)
    private void roundabout$canTargetEntity(Entity $$0x, CallbackInfoReturnable<Boolean> cir) {
        if ($$0x instanceof StandEntity) {
            cir.setReturnValue(false);
        }
    }
}

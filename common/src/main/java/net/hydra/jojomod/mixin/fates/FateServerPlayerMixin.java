package net.hydra.jojomod.mixin.fates;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.ModParticles;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class FateServerPlayerMixin extends Player {

    /**Cancel death animation*/
    @Inject(method = "die", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$die(DamageSource $$0, CallbackInfo ci) {
        if (FateTypes.takesSunlightDamage(this)) {
            if ($$0.is(ModDamageTypes.SUNLIGHT)){
                ((IPlayerEntity) this).roundabout$SetPos(PlayerPosIndex.SUNLIGHT);
                if (this.level() instanceof ServerLevel SL){
                    Vec3 position = this.getPosition(1);
                    Vec3 position2 = this.getEyePosition();
                    Vec3 position3 = this.getEyePosition().subtract(this.getPosition(1)).multiply(new Vec3(0.5F,
                            0.5F,0.5F));
                    position3 = position3.add(this.getPosition(1));
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position.x, position.y, position.z,
                            0, 0.2, 0.2, 0.2, 0.1);
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.2, 0.2, 0.1);
                    SL.sendParticles(ModParticles.FIRE_CRUMBLE,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.2, 0.2, 0.1);


                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position.x, position.y, position.z,
                            0, 0.2, 0.5, 0.2, 0.5);
                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position2.x, position2.y, position2.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                    SL.sendParticles(ModParticles.DUST_CRUMBLE,
                            position3.x, position3.y, position3.z,
                            0, 0.2, 0.5, 0.2, 0.2);
                }
            } else {
                if (FateTypes.isVampire(this)){

                }
            }
        }
    }
    public FateServerPlayerMixin(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
        super($$0, $$1, $$2, $$3);
    }
}

package net.hydra.jojomod.mixin.fates;

import com.mojang.authlib.GameProfile;
import net.hydra.jojomod.Roundabout;
import net.hydra.jojomod.access.IPlayerEntity;
import net.hydra.jojomod.event.index.FateTypes;
import net.hydra.jojomod.event.index.PlayerPosIndex;
import net.hydra.jojomod.event.powers.ModDamageTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class FateServerPlayerMixin extends Player {

    /**Cancel death animation*/
    @Inject(method = "die", at = @At(value = "HEAD"), cancellable = true)
    protected void roundabout$die(DamageSource $$0, CallbackInfo ci) {
        if (FateTypes.takesSunlightDamage(this) && $$0.is(ModDamageTypes.SUNLIGHT)) {
            ((IPlayerEntity) this).roundabout$SetPos(PlayerPosIndex.SUNLIGHT);
        }
    }
    public FateServerPlayerMixin(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
        super($$0, $$1, $$2, $$3);
    }
}

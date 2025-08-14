package net.hydra.jojomod.mixin.gravity.client;

import java.util.Map;
import java.util.UUID;

import net.hydra.jojomod.util.gravity.GravityAPI;
import net.hydra.jojomod.util.gravity.RotationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class, priority = 1001)
public abstract class GravityClientPlayNetworkHandlerMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private Map<UUID, PlayerInfo> playerInfoMap;

    @Shadow private ClientLevel level;

    @Inject(
            method = "handleGameEvent(Lnet/minecraft/network/protocol/game/ClientboundGameEventPacket;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void redirect_onGameStateChange_getEyeY_0(ClientboundGameEventPacket $$0, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread($$0, (ClientPacketListener)(Object)this, this.minecraft);
        Player playerEntity = this.minecraft.player;
        Direction gravityDirection = GravityAPI.getGravityDirection(playerEntity);
        if (gravityDirection == Direction.DOWN)
            return;
        ClientboundGameEventPacket.Type $$2 = $$0.getEvent();
        if ($$2 == ClientboundGameEventPacket.ARROW_HIT_PLAYER) {
            ci.cancel();
            this.level.playSound(playerEntity, playerEntity.getEyePosition().x, playerEntity.getEyePosition().y,playerEntity.getEyePosition().x, SoundEvents.ARROW_HIT_PLAYER, SoundSource.PLAYERS, 0.18F, 0.45F);
        }
    }

    @Inject(
            method = "handleExplosion",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true)
    private void wrapOperation_onExplosion_add_0(ClientboundExplodePacket $$0, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread($$0, (ClientPacketListener)(Object)this, this.minecraft);
        Player playerEntity = this.minecraft.player;
        Direction gravityDirection = GravityAPI.getGravityDirection(playerEntity);
        if (gravityDirection == Direction.DOWN)
            return;

        ci.cancel();
        Vec3 player = RotationUtil.vecWorldToPlayer((double)$$0.getKnockbackX(), (double)$$0.getKnockbackY(), (double)$$0.getKnockbackZ(), gravityDirection);
        Explosion $$1 = new Explosion(this.minecraft.level, null, $$0.getX(), $$0.getY(), $$0.getZ(), $$0.getPower(), $$0.getToBlow());
        $$1.finalizeExplosion(true);
        this.minecraft
                .player
                .setDeltaMovement(
                        this.minecraft.player.getDeltaMovement().add(player.x,player.y,player.z)
                        );
    }
}

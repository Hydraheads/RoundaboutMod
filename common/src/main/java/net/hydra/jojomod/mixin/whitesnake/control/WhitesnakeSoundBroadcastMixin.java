package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class WhitesnakeSoundBroadcastMixin {
    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V", at = @At("TAIL"))
    private void roundaboutWhitesnake$sendPositionSound(Player excluded, double x, double y, double z,
                                                        Holder<SoundEvent> sound, SoundSource source,
                                                        float volume, float pitch, long seed, CallbackInfo ci) {
        sendToControllers(excluded, new Vec3(x, y, z), sound.value().getRange(volume),
                new ClientboundSoundPacket(sound, source, x, y, z, volume, pitch, seed));
    }

    @Inject(method = "playSeededSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/core/Holder;Lnet/minecraft/sounds/SoundSource;FFJ)V", at = @At("TAIL"))
    private void roundaboutWhitesnake$sendEntitySound(Player excluded, Entity sourceEntity,
                                                      Holder<SoundEvent> sound, SoundSource source,
                                                      float volume, float pitch, long seed, CallbackInfo ci) {
        sendToControllers(excluded, sourceEntity.position(), sound.value().getRange(volume),
                new ClientboundSoundEntityPacket(sound, source, sourceEntity, volume, pitch, seed));
    }

    private void sendToControllers(Player excluded, Vec3 source, float range, Packet<?> packet) {
        ServerLevel level = (ServerLevel) (Object) this;
        double rangeSqr = (double) range * range;
        for (ServerPlayer player : level.players()) {
            if (player == excluded || player.distanceToSqr(source) <= rangeSqr) continue;
            if (!(((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers)
                    || !powers.isPiloting()) continue;
            LivingEntity stand = powers.getPilotingStand();
            if (stand != null && stand.isAlive() && !stand.isRemoved()
                    && stand.distanceToSqr(source) <= rangeSqr) {
                player.connection.send(packet);
            }
        }
    }
}

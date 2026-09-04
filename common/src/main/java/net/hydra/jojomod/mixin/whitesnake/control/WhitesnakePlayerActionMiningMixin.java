package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.util.MainUtil;
import net.hydra.jojomod.access.WhitesnakePilotMiningHandler;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class WhitesnakePlayerActionMiningMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    private void roundaboutWhitesnake$handlePlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        ServerboundPlayerActionPacket.Action action = packet.getAction();
        if (action != ServerboundPlayerActionPacket.Action.START_DESTROY_BLOCK
                && action != ServerboundPlayerActionPacket.Action.STOP_DESTROY_BLOCK
                && action != ServerboundPlayerActionPacket.Action.ABORT_DESTROY_BLOCK) return;
        if (!(((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers)
                || !powers.isPiloting() || !MainUtil.getIsGamemodeApproriateForGrief(player)) return;
        ((WhitesnakePilotMiningHandler) player.gameMode).roundaboutWhitesnake$handleMining(
                packet.getPos(), action, packet.getDirection(), player.level().getMaxBuildHeight(), packet.getSequence());
        ci.cancel();
    }
}

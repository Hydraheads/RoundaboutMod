package net.hydra.jojomod.mixin.whitesnake.control;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class WhitesnakeControlChatMixin {
    @Shadow
    public ServerPlayer player;

    @ModifyArg(method = "broadcastChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;broadcastChatMessage("
                    + "Lnet/minecraft/network/chat/PlayerChatMessage;"
                    + "Lnet/minecraft/server/level/ServerPlayer;"
                    + "Lnet/minecraft/network/chat/ChatType$Bound;)V"), index = 2)
    private ChatType.Bound roundaboutWhitesnake$useControlModeName(ChatType.Bound original) {
        if (!(((StandUser) player).roundabout$getStandPowers() instanceof PowersWhitesnake powers)
                || !powers.isPiloting()) {
            return original;
        }
        StandEntity stand = ((StandUser) player).roundabout$getStand();
        String name = stand instanceof WhitesnakeEntity whitesnake && whitesnake.isDisguised()
                ? whitesnake.getDisguiseName() : "Whitesnake";
        return new ChatType.Bound(original.chatType(), Component.literal(name), original.targetName());
    }
}

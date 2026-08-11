package net.hydra.jojomod.mixin.whitesnake;

import net.hydra.jojomod.entity.stand.StandEntity;
import net.hydra.jojomod.event.powers.StandUser;
import net.hydra.jojomod.entity.stand.WhitesnakeEntity;
import net.hydra.jojomod.stand.powers.PowersWhitesnake;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class WhitesnakeControlChatMixin {
    @Shadow
    public ServerPlayer player;

    @Redirect(method = "broadcastChatMessage", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/network/chat/ChatType;bind(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/network/chat/ChatType$Bound;"))
    private ChatType.Bound roundaboutWhitesnake$useControlModeName(ResourceKey<ChatType> chatType, Entity sender) {
        ChatType.Bound original = ChatType.bind(chatType, sender);
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
